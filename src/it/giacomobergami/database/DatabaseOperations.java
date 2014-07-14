/*
 * DatabaseOperations.java
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

package it.giacomobergami.database;

import it.giacomobergami.functional.AbstractProperty;
import it.giacomobergami.utils.Dovetailing;
import it.giacomobergami.relational.ICalc;
import it.giacomobergami.relational.AbstractJoinProperty;
import it.giacomobergami.relational.IMapFunction;
import it.giacomobergami.relational.Table;
import it.giacomobergami.relational.TableOperations;
import it.giacomobergami.tensor.ITensorLayer;
import it.giacomobergami.tensor.Tensor;
import it.giacomobergami.types.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gyankos
 */
public class DatabaseOperations {

    public static Database Reindex(Database d, IPhi reindex) {
        Database toret = new Database();
        for (String x:d.getTableNames()) {
            toret.add(TableOperations.reindexing(d.getTable(x), reindex));
        }
        return toret;
    }

    
    public static Database Union(Collection<Database> d) {
        Map<String,List<Table>> collapse = new HashMap<>();
        for (Database x:d) {
            for (String k:x.getTableNames())
                if (!collapse.containsKey(k)) {
                    List<Table> ltb = new LinkedList<>();
                    ltb.add(x.getTable(k));
                    collapse.put(k, ltb);
                } else
                    collapse.get(k).add(x.getTable(k));
        }
        //ALERT
        Database db = new Database();
        for (String x:collapse.keySet()) {
            System.out.println(x);
            Table res = TableOperations.union(collapse.get(x));
            res.forceName(x);
            db.update(x,res);
        }
        return db;
    }
    
    public static Database Project(Database db, Type... cls) {
        Database toret = new Database();
        Set<String> ks = db.getTableNames();
        for (String x : ks) {
            List<Type> aList = new LinkedList<>(Arrays.asList(db.getTable(x).getSchema()));
            List<Type> bList =  new LinkedList<>(Arrays.asList(cls)); 
            aList.retainAll(bList);
            if (!aList.isEmpty()) {
                toret.add(TableOperations.project(db.getTable(x), cls));
            }
        }
        return toret;
    }
    
    public static <T> Database Select(Database db, AbstractProperty prop) {
        Database toret = new Database();
        Set<String> ks = db.getTableNames();
        for (String x : ks) {
            toret.update(x, TableOperations.select(db.getTable(x), prop));
        }
        return toret;
    }
    
    public static  Database Calc(Database db, Type clazz, ICalc op) {
        Database toret = new Database();
        Set<String> ks = db.getTableNames();
        for (String x : ks) {
            List<Type> aList =  Arrays.asList(db.getTable(x).getSchema());
            if (!aList.contains(clazz)) {
                Table tab = TableOperations.calc(db.getTable(x),clazz,op);
                toret.update(tab.getName(), tab);
            } else
                toret.update(x, TableOperations.reindexing(db.getTable(x), TableOperations.dtvecPhi));
        }
        return toret;
    }
    
    public static <T> Database ProjectAndGroupBy(Database db, Type clazz, IMapFunction iMapFunction, Type... braket) {
        Database toret = new Database();
        Set<String> ks = db.getTableNames();
        for (String x : ks) {
            List<Type> aList =  new LinkedList<>(Arrays.asList(db.getTable(x).getSchema()));
            List<Type> bList =  new LinkedList<>(Arrays.asList(braket));
            aList.retainAll(bList);
            if (aList.size()==braket.length) {
                Table tab = TableOperations.projectAndGroupBy(db.getTable(x),clazz,iMapFunction,braket);
                toret.update(tab.getName(), tab);
            } else
                toret.update(x, TableOperations.reindexing(db.getTable(x), TableOperations.dtvecPhi));
        }
        return toret;
    }
    
    public static <T> Database GroupBy(Database db, Type clazz, IMapFunction iMapFunction, Type... braket) {
        Database toret = new Database();
        Set<String> ks = db.getTableNames();
        for (String x : ks) {
            List<Type> aList =  new LinkedList<>(Arrays.asList(db.getTable(x).getSchema()));
            List<Type> bList =  new LinkedList<>(Arrays.asList(braket));
            aList.retainAll(bList);
            if (aList.size()==braket.length) {
                Table tab = TableOperations.groupBy(db.getTable(x),clazz,iMapFunction);
                toret.update(tab.getName(), tab);
            } else
                toret.update(x, TableOperations.reindexing(db.getTable(x), TableOperations.dtvecPhi));
        }
        return toret;
    }
    
    public static Database Join(Database left,AbstractJoinProperty prop, Database right) {
        Database toret = new Database();
        for (String x: left.getTableNames()) {
            for (String y:right.getTableNames()) {
                Table result = TableOperations.join(left.getTable(x), prop, right.getTable(y));
                toret.update(result.getName(),result);
            }
        }
        return toret;
        
    }
    
    public static <T extends ITensorLayer> Database LeftTiedJoin(Database left,AbstractJoinProperty prop, Database right, Tensor<T> tleft, Tensor<T> tright) {
        Database toret = new Database();
        for (String x: left.getTableNames()) {
            for (String y:right.getTableNames()) {
                Table result = TableOperations.leftTiedJoin(left.getTable(x), prop, right.getTable(y),tleft,tright);
                toret.update(result.getName(),result);
            }
        }
        return toret;
    }
    
    public static Database Rename(Database db, Type source, Type dest) {
        Database toret = new Database();
        for (String x : db.getTableNames()) {
             Table result = TableOperations.rename(db.getTable(x), source, dest);
             result.forceName(x);
             toret.update(x, result);
        }
        return toret;
    }
    
}
