/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.database;

import it.giacomobergami.functional.IProperty;
import it.giacomobergami.relational.Dovetailing;
import it.giacomobergami.relational.ICalc;
import it.giacomobergami.relational.IJoinProperty;
import it.giacomobergami.relational.IMapFunction;
import it.giacomobergami.relational.Table;
import it.giacomobergami.relational.TableOperations;
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
    
    public static Table reindexing(Table t, IPhi reindex) {
        for (int i=0; i<t.size(); i++) {
            t.get(i).setIndex(reindex.reindex(t.get(i).getIndex()));
        }
        return t;
    }
    
    public static final IPhi dtvecPhi = new IPhi() {
        @Override
        public BigInteger reindex(BigInteger index) {
            LinkedList<BigInteger> llbi = new LinkedList<>();
            llbi.add(index);
            return Dovetailing.dtVec(llbi);
        }
    };
    
    public static Database Union(Collection<Database> d) {
        Map<String,List<Table>> collapse = new HashMap<>();
        for (Database x:d) {
            for (String k:x.keySet())
                if (!collapse.containsKey(k)) {
                    List<Table> ltb = new LinkedList<>();
                    ltb.add(x.get(k));
                    collapse.put(k, ltb);
                } else
                    collapse.get(k).add(x.get(k));
        }
        //ALERT
        Database db = new Database();
        for (String x:collapse.keySet()) {
            System.out.println(x);
            Table res = TableOperations.union(collapse.get(x));
            res.forceName(x);
            db.put(x,res);
        }
        return db;
    }
    
    public static Database Project(Database db, Class... cls) {
        Database toret = new Database();
        Set<String> ks = db.keySet();
        for (String x : ks) {
            List<Class> aList = new LinkedList<>(Arrays.asList(db.get(x).getSchema()));
            List<Class> bList =  new LinkedList<>(Arrays.asList(cls)); 
            aList.retainAll(bList);
            if (!aList.isEmpty()) {
                toret.add(TableOperations.project(db.get(x), cls));
            }
        }
        return toret;
    }
    
    public static <T> Database Select(Database db, IProperty prop) {
        Database toret = new Database();
        Set<String> ks = db.keySet();
        for (String x : ks) {
            toret.put(x, TableOperations.select(db.get(x), prop));
        }
        return toret;
    }
    
    public static <T> Database Calc(Database db, Class<T> clazz, ICalc<T> op) {
        Database toret = new Database();
        Set<String> ks = db.keySet();
        for (String x : ks) {
            List<Class> aList =  Arrays.asList(db.get(x).getSchema());
            if (!aList.contains(clazz)) {
                toret.put(x, TableOperations.calc(db.get(x),clazz,op));
            } else
                toret.put(x, reindexing(db.get(x), dtvecPhi));
        }
        return toret;
    }
    
    public static <T> Database ProjectAndGroupBy(Database db, Class<T> clazz, IMapFunction<T> iMapFunction, Class... braket) {
        Database toret = new Database();
        Set<String> ks = db.keySet();
        for (String x : ks) {
            List<Class> aList =  Arrays.asList(db.get(x).getSchema());
            List<Class> bList =  Arrays.asList(braket);
            aList.retainAll(bList);
            if (aList.size()==braket.length) {
                toret.put(x, TableOperations.projectAndGroupBy(db.get(x),clazz,iMapFunction,braket));
            } else
                toret.put(x, reindexing(db.get(x), dtvecPhi));
        }
        return toret;
    }
    
    public static Database Join(Database left,IJoinProperty prop, Database right) {
        Database toret = new Database();
        for (String x: left.keySet()) {
            for (String y:right.keySet()) {
                Table result = TableOperations.join(left.get(x), prop, right.get(y));
                toret.put(result.getName(),result);
            }
        }
        return toret;
        
    }
    
}
