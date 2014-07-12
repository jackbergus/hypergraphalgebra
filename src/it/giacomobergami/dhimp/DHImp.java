/*
 * DHImp.java
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

import it.giacomobergami.database.Database;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.relational.Table;
import it.giacomobergami.tensor.BinaryRelationsTensor;
import it.giacomobergami.tensor.GuavaBinaryTensorLayer;
import it.giacomobergami.tensor.ITensorLayer;
import it.giacomobergami.tensor.Tensor;
import it.giacomobergami.types.Type;
import java.math.BigInteger;

/**
 *
 * @author gyankos
 * @param <T>
 */
public class DHImp<T extends ITensorLayer> {
    
    private Database first;
    private final Tensor<T> second;
    private BigInteger lastIndex = BigInteger.ONE;
    
    public static DHImp newGuavaBinaryTensorDHImp() {
        return new DHImp<>(new Database(),new BinaryRelationsTensor());
    }
    
    public DHImp(Database db, Tensor<T> tensor) {
        first = db;
        second = tensor;
    }
    
    public void createTable(String name, BigInteger startIndex, Type ... clazzes) {
        first.createTable(name,startIndex, clazzes);
    }
    
    public void createTable(String name,Type ... clazzes) {
        first.createTable(name, clazzes);
    }
    
    public void setRelation(BigInteger src, BigInteger dst, String layer, double value) {
        second.set(src, dst, layer, value);
    }
    
    public Database getDB() {
        return first;
    }
    public Tensor<T> getT() {
        return second;
    }
    
    public Class<T> getTClass() {
        return second.getLayersClass();
    }

    public BigInteger addPojoRow(String name, double weight, Object...objs) {
        BigInteger toret =  first.getTable(name).addPojoRow(weight, lastIndex, objs);
        //System.out.println(toret);
        if (toret.signum()>=0) {
            lastIndex = lastIndex.add(BigInteger.ONE);
        }
        return toret;
    }
    
    @Override
    public String toString() {
        String toprint = "";
        for (String x:getDB().getTableNames()) {
            toprint = x+" instanceof  [";
            Table tb = getDB().getTable(x);
            Type schema[] = tb.getSchema();
            for (int i=0; i<schema.length; i++) {
                toprint += schema[i].getName();
                if (i!=schema.length-1)
                    toprint += ", ";
                else
                    toprint += "]\n";
            }
            for (Tuple t:tb)
                toprint= toprint+t.toString()+"\n";
            toprint = toprint +"\n";
        }
        return toprint;
    }
    
}
