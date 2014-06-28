/*
 * HypergraphOperations.java
 * This file is part of HypergraphAlgebra
 *
 * Copyright (C) 2014 - Giacomo Bergami
 *
 * HypergraphAlgebra is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * HypergraphAlgebra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HypergraphAlgebra. If not, see <http://www.gnu.org/licenses/>.
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

        Set<String> commonLayers = new HashSet<>(tLeft.keySet());
        commonLayers.addAll(tRight.keySet());

        Tensor<T> nt = TensorOperations.TJoin(ndb, commonLayers, tLeft, tRight);
        return new DHImp<>(ndb, nt);
    }
    
    public DHImp<T> HLeftTiedJoin (DHImp<T> left, IJoinProperty prop, DHImp<T> right) {
        Tensor<T> tLeft = left.getT();
        Tensor<T> tRight = right.getT();
        Database ndb = DatabaseOperations.LeftTiedJoin(left.getDB(), prop, right.getDB(), tLeft, tRight);


        Set<String> commonLayers = new HashSet<>(tLeft.keySet());
        commonLayers.addAll(tRight.keySet());

        Tensor<T> nt = TensorOperations.TJoin(ndb, commonLayers, tLeft, tRight);
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
        Tensor<T> nt = TensorOperations.TUpdate(ndb, rdb.getT());
        return new DHImp<>(ndb,nt);
    }
    
    public DHImp<T> HGroupBy(DHImp<T> rdb, Class<T> clazz, IMapFunction<T> iMapFunction, Class... braket) {
        Database ndb = DatabaseOperations.ProjectAndGroupBy(rdb.getDB(), clazz, iMapFunction, braket);
        Tensor<T> nt = TensorOperations.TUpdate(ndb, rdb.getT());
        return new DHImp<>(ndb,nt);
    }

}
