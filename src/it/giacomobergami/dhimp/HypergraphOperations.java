/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.giacomobergami.dhimp;

import com.google.common.collect.Table;
import it.giacomobergami.database.Database;
import it.giacomobergami.database.DatabaseOperations;
import it.giacomobergami.functional.IProperty;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.relational.Dovetailing;
import it.giacomobergami.relational.ICalc;
import it.giacomobergami.relational.IJoinProperty;
import it.giacomobergami.relational.IMapFunction;
import it.giacomobergami.tensor.HyperEdge;
import it.giacomobergami.tensor.IHedgeProp;
import it.giacomobergami.tensor.ITensorLayer;
import it.giacomobergami.tensor.Tensor;
import it.giacomobergami.tensor.TensorOperations;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gyankos
 * @param <T>
 */
public class HypergraphOperations<T extends ITensorLayer> {

    public DHImp<T> HSelect(DHImp<T> rdb, IProperty dbProp, IHedgeProp tProp) {
        Database oldb = rdb.getDB();
        Database ndb = DatabaseOperations.Select(oldb, dbProp);
        Tensor<T> tensor = rdb.getT();
        Tensor<T> nt = new Tensor<>(rdb.getTClass());
        for (String x : tensor.keySet()) {
            for (Table.Cell<BigInteger, BigInteger, Double> cell : tensor.get(x).getValueRange()) {
                Tuple left = oldb.getEntityByIndex(cell.getRowKey());
                Tuple right = oldb.getEntityByIndex(cell.getColumnKey());
                HyperEdge he = new HyperEdge(right, left, x, cell.getValue());
                if (dbProp.prop(left) && dbProp.prop(right) && tProp.prop(he)) {
                    nt.set(cell.getRowKey(), cell.getColumnKey(), x, cell.getValue());
                }
            }
        }
        return new DHImp<>(ndb, nt);
    }

    public DHImp<T> HJoin(DHImp<T> left, IJoinProperty prop, DHImp<T> right) {
        Database ndb = DatabaseOperations.Join(left.getDB(), prop, right.getDB());

        Tensor<T> tLeft = left.getT();
        Tensor<T> tRight = right.getT();
        Tensor<T> nt = new Tensor<>(left.getTClass());

        Set<String> commonLayers = new HashSet<>(tLeft.keySet());
        commonLayers.addAll(tRight.keySet());

        Set<BigInteger> allKeys = ndb.getAllKeys();

        for (String x : commonLayers) {
            T xtLeft = tLeft.get(x);
            T xtRight = tRight.get(x);
            for (BigInteger idx : allKeys) {
                BigInteger dtx[] = Dovetailing.dtInv(idx);
                for (BigInteger idy : allKeys) {
                    BigInteger dty[] = Dovetailing.dtInv(idy);
                    double avg = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            avg = avg + xtLeft.get(dtx[i], dty[j]) + xtRight.get(dtx[i], dty[j]);
                        }
                    }
                    avg = avg / 8.0;
                    nt.set(idx, idy, x, avg);
                }
            }
        }
        return new DHImp<>(ndb, nt);
    }

    public <K> DHImp<T> HCalc(DHImp<T> rdb, Class<K> claxx, ICalc<K> ic) {
        return new DHImp<>(DatabaseOperations.Calc(rdb.getDB(), claxx, ic), rdb.getT());
    }

    public DHImp<T> HUnion(Collection<DHImp<T>> union) {
        //XXX: sdb, memory bound
        Set<Database> sdb = new HashSet<>();
        Set<String> commonLayers = new HashSet<>();
        Tensor<T> nt = null;
        Class<T> cl = null;
        boolean fst = true;
        for (DHImp<T> rdb : union) {
            if (fst) {
                cl = rdb.getTClass();
                nt = new Tensor<>(cl);
                fst = false;
            }
            sdb.add(rdb.getDB());
            commonLayers.addAll(rdb.getT().keySet());
        }
        Database ndb = DatabaseOperations.Union(sdb);

        Set<BigInteger> allKeys = ndb.getAllKeys();

        for (String layer : commonLayers) {
            for (BigInteger idx : allKeys) {
                List<BigInteger> vx = Dovetailing.dtVecInv(idx);
                for (BigInteger idy : allKeys) {
                    List<BigInteger> vy = Dovetailing.dtVecInv(idy);
                    double avg = 0;
                    double size = vx.size() * vy.size() * union.size();
                    if (size == 0)
                        continue;
                    for (DHImp<T> it : union)
                        for (BigInteger x : vx)
                            for (BigInteger y : vy)
                                avg += it.getT().get(layer).get(x,y);
                    avg = avg / size;
                    nt.set(idx, idy, layer, avg);
                }
            }
        }
        
        return new DHImp<>(ndb,nt);

    }
    
    public DHImp<T> HProject(DHImp<T> rdb, Class... es) {
        Database ndb = DatabaseOperations.Project(rdb.getDB(), es);
        Tensor<T> nt = TensorOperations.Update(ndb, rdb.getT());
        return new DHImp<>(ndb,nt);
    }
    
    public DHImp<T> HGroupBy(DHImp<T> rdb, Class<T> clazz, IMapFunction<T> iMapFunction, Class... braket) {
        Database ndb = DatabaseOperations.ProjectAndGroupBy(rdb.getDB(), clazz, iMapFunction, braket);
        Tensor<T> nt = TensorOperations.Update(ndb, rdb.getT());
        return new DHImp<>(ndb,nt);
    }

}
