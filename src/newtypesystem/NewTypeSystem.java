/*
 * NewTypeSystem.java
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



package newtypesystem;

import it.giacomobergami.database.Database;
import it.giacomobergami.database.DatabaseOperations;
import it.giacomobergami.dhimp.DHImp;
import it.giacomobergami.dhimp.HypergraphOperations;
import it.giacomobergami.functional.IProperty;
import it.giacomobergami.relational.Dovetailing;
import it.giacomobergami.relational.Table;
import it.giacomobergami.relational.TableOperations;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.relational.IJoinProperty;
import it.giacomobergami.relational.IMapFunction;
import it.giacomobergami.tensor.BinaryRelationsTensor;
import it.giacomobergami.tensor.GuavaBinaryTensorLayer;
import it.giacomobergami.tensor.HyperEdge;
import it.giacomobergami.tensor.IHedgeProp;
import it.giacomobergami.tensor.Tensor;
import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gyankos
 */
public class NewTypeSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*BigInteger i = Dovetailing.index(2399);
        BigInteger j = Dovetailing.index(6680);
        BigInteger dt = Dovetailing.dt(i, j);
        System.out.println(dt);
        BigInteger retdt[] = Dovetailing.dtInv(dt);
        System.out.println(retdt[0]+" "+retdt[1]);
        
        BigInteger k = Dovetailing.index(8);
        BigInteger h = Dovetailing.index(12);
        LinkedList<BigInteger> libi = new LinkedList<>();
        libi.add(i);
        libi.add(j);
        libi.add(k);
        libi.add(h);
        BigInteger vdt = Dovetailing.dtVec(libi);
        List<BigInteger> rlbi = Dovetailing.dtVecInv(vdt);
        for (BigInteger x: rlbi) {
            System.out.println(x);
        }*/
        
        Table one = new Table("t1",Dovetailing.index(1),String.class, Integer.class,Double.class);
        one.addRow(0.1, "A",2,4.0);
        one.addRow(0.2, "B",8,10.0);
        one.addRow(0.3, "C",15,20.0);
        one.addRow(0.4, "A",28,3.0);
        one.addRow(0.5, "C",1,3.0);
        
        Table two = new Table("t1",Dovetailing.index(6),String.class, Integer.class,Double.class);
        two.addRow(0.2, "A",7,11.0);
        two.addRow(0.1,"A",2,4.0);
        
        Database db1 = new Database();
        db1.add(one);
        Database db2 = new Database();
        db2.add(two);
        
        BinaryRelationsTensor tensor1 = new BinaryRelationsTensor();
        tensor1.set(1, 1, "A", 0.1);
        tensor1.set(1, 3, "A", 0.3);
        tensor1.set(2, 2, "A", 0.2);
        tensor1.set(2, 4, "A", 0.5);
        tensor1.set(2, 5, "A", 0.1);
        tensor1.set(3, 3, "A", 0.5);
        tensor1.set(3, 4, "A", 0.6);
        tensor1.set(4, 1, "A", 0.1);
        tensor1.set(4, 4, "A", 0.2);
        tensor1.set(5, 2, "A", 0.3);
        tensor1.set(5, 5, "A", 0.5);
        
        BinaryRelationsTensor tensor2 = new BinaryRelationsTensor();
        tensor2.set(6,7,"A",1);
        tensor2.set(7,6,"A",1);
        
        DHImp<GuavaBinaryTensorLayer> dh1 = new DHImp<>(db1,tensor1);
        DHImp<GuavaBinaryTensorLayer> dh2 = new DHImp<>(db2,tensor2);
        HypergraphOperations<GuavaBinaryTensorLayer> go = new HypergraphOperations<>();
        /*DHImp<GuavaBinaryTensorLayer> result = go.HSelect(dh, new IProperty() {
            @Override
            public boolean prop(Tuple tup) {
                return tup.get(0).equals("A");
            }
        }, new IHedgeProp() {
            @Override
            public boolean prop(HyperEdge e) {
                return true;
            }
        });*/
        /*DHImp<GuavaBinaryTensorLayer> result = go.HJoin(dh1, new IJoinProperty() {
            @Override
            public boolean property(Tuple left, Tuple right) {
                return (left.get(0).equals(right.get(0)));
            }
        }, dh2);*/
        LinkedList<DHImp<GuavaBinaryTensorLayer>> l = new LinkedList<>();
        l.add(dh1);
        l.add(dh2);
        DHImp<GuavaBinaryTensorLayer> result =  go.HUnion(l);
        
        for (com.google.common.collect.Table.Cell<BigInteger, BigInteger, Double> cell:result.getT().get("A").getValueRange()) {
            System.out.println(cell);
        }
        for (Tuple x:result.getDB().get("t1")) {
            System.out.println(x);
        }
        
        /*Table proj = TableOperations.projectAndGroupBy(one, Integer.class, new IMapFunction<Integer>() {
            @Override
            public Integer collectOf(Collection<Integer> collection) {
                int sum = 0;
                for (Integer x : collection)
                    sum += x;
                return sum;
            }
        }, String.class);*/
        
        /*Table one = new Table("t1",Dovetailing.index(1),Integer.class, Integer.class);
        one.addRow(1, 1,2);
        one.addRow(1, 1,4);
        Table two = new Table("t2",Dovetailing.index(3),Integer.class, Integer.class);
        two.addRow(1, 2,3);
        two.addRow(0.8, 2,6);
        two.addRow(0.8, 2,1);*/
        /*LinkedList<Table> tomerge = new LinkedList<>();
        tomerge.add(one);
        tomerge.add(two);
        Table proj = TableOperations.union(tomerge);*/
        /*Table proj = TableOperations.join(one, new IJoinProperty() {
            @Override
            public boolean property(Tuple left, Tuple right) {
                return (left.get(1).equals(right.get(0)));
            }
        }, two);*/
        /*for (Tuple x : proj) {
            System.out.println(x);
        }*/
    }
    
}
