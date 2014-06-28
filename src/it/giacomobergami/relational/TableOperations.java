/*
 * TableOperations.java
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

package it.giacomobergami.relational;

import it.giacomobergami.functional.IProperty;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.functional.Void;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author gyankos
 */
public class TableOperations {
    
    private static Tuple projectTuple(Tuple t, Class... L) {
        Object o[] = new Object[L.length];
        int index = 0;
        for (Class c: L) 
            for (int i=0; i<t.size(); i++)
                if (t.get(i).getClass().equals(c))
                    o[index++] = t.get(i);
        Tuple toret = new Tuple(L);
        toret.set(o);
        toret.setIndex(t.getIndex());
        toret.setWeight(t.getWeight());
        return toret;
    }
    
    public static Table project(Table t,Class... L) {
        Table toret = new Table(t.getName()+" over L", L);
        Map<Integer,List<Double>> wmap = new HashMap<>();
        Map<Integer,List<BigInteger>> imap = new HashMap<>();
        
        int i = 0;

        for (Tuple y : t) {
            Tuple x = projectTuple(y, L);
            int pos = toret.containsValuesPos(x);
            if (pos==-1) {
                toret.addRow(1,x.get().clone());
                LinkedList<Double> weight = new LinkedList<>();
                weight.add(x.getWeight());
                wmap.put(i,weight);
                LinkedList<BigInteger> index = new LinkedList<>();
                index.add(x.getIndex());
                imap.put(i,index);
                i++;
            } else {
                wmap.get(pos).add(x.getWeight());
                imap.get(pos).add(x.getIndex());
            }
        }
        
        for (int j=0; j<toret.size(); j++) {
            double size = wmap.get(j).size();
            double avg = 0;
            for (double d : wmap.get(j))
                avg += d;
            avg = avg / size;
            toret.get(j).setWeight(avg);
            //System.out.println("~~~~~");
            /*for (BigInteger x: imap.get(j)) {
                System.out.println(x);
            }*/
            //System.out.println("~~~~~");
            toret.get(j).setIndex( Dovetailing.dtVec(imap.get(j)));
            toret.add(toret.remove(j));
        }
        
        return toret;
    }
    
    public static Table select(Table t, IProperty prop){
        Table toret = new Table(t.getName(),t.getSchema());
        Iterator<Tuple> li = t.iterator();
        while (li.hasNext()) {
            Tuple current = li.next();
            if (prop.prop(current))
                toret.add(current);
        }
        return toret;
    }
    
    public static Table union(Collection<Table> lt) {
        if (lt==null)
            return null;
        if (lt.isEmpty())
            return new Table("empty table on union"); //with no size
        Class cls[] = null;
        String name = "";
        for (Table t : lt) {
            if (cls==null)
                cls = t.getSchema();
            else
                if (!Arrays.equals(cls, t.getSchema()))
                    throw new RuntimeException("Tables have different schemas");
            name = name + t.getName() + " v ";
        }
        name = name.substring(0,name.length()-2);
        Table toret = new Table(name, cls);
        for (Table t : lt) {
            for (BigInteger i : t.getAllKeys())
                toret.add(t.get(i));
        }
        return project(toret,cls);
    }
    
    public static <T> Table calc(Table t, Class<T> clazz, ICalc<T> op) {
        Class clazzes[] = new Class[t.size()+1];
        System.arraycopy(t.getSchema(),0,clazzes,0,t.getSchema().length);
        clazzes[t.size()] = clazz;
        Table toret = new Table(t.getName()+" Calc with "+clazz.getCanonicalName(),clazzes);
        for (Tuple tup : t) {
            Object objs[] = new Object[t.size()+1];
            System.arraycopy(tup, 0, objs, 0, t.size());
            objs[t.size()] = op.calcResult(tup);
            toret.addRow(tup.getWeight(), tup.getIndex(), null, objs);
        }
        return toret;
    }
    
    public static Table join(Table left, IJoinProperty prop, Table right) {
        LinkedList<Class> ll = new LinkedList<>();
        ll.addAll(Arrays.asList(left.getSchema()));
        ll.addAll(Arrays.asList(right.getSchema()));
        Table toret = new Table(left.getName()+"|><|"+right.getName(), ll.toArray(new Class[0]));
        for (Tuple lt : left) {
            for (Tuple rt : right) {
                if (prop.property(lt, rt)) {
                    Object toadd[] = new Object[ll.size()];
                    for (int i=0;i<left.getSchema().length; i++)
                        toadd[i] = lt.get(i);
                    for (int i=0;i<right.getSchema().length; i++)
                        toadd[left.getSchema().length+i] = rt.get(i);
                    toret.addRow(lt.getWeight()*rt.getWeight(), Dovetailing.dt(lt.getIndex(), rt.getIndex()), null, toadd);
                }
            }
        }
        return toret;
    }
    
    private static <T> Table groupBy(Table t, Class<T> clazz, IMapFunction<T> map) {
        Class[] oldschema = t.getSchema();
        Table toret = new Table(t.getName()+" Group by "+clazz.getName(), oldschema);
        LinkedList<Class> llc = new LinkedList<>();
        int clazzIndex = -1;
        int cidx = 0;
        for (Class c : oldschema) {
            if (!c.equals(clazz))
                llc.add(c);
            else
                if (clazzIndex ==-1)
                    clazzIndex = cidx;
            cidx++;
        }
        Class clazzes[] = llc.toArray(new Class[0]);
        Table tmap = new Table("tmp",clazzes);
        int newschemaleng = tmap.getSchema().length;
        Map<Integer,List<Double>> wmap = new HashMap<>();
        Map<Integer,List<BigInteger>> imap = new HashMap<>();
        Map<Integer,List<T>> vmap = new HashMap<>();
        
        int i = 0;

        for (Tuple x : t) {
            Tuple pt = projectTuple(x, clazzes);
            int pos = tmap.containsValuesPos(pt);
            if (pos==-1) {
                tmap.addRow(1, pt.get().clone());
                LinkedList<Double> weight = new LinkedList<>();
                weight.add(x.getWeight());
                wmap.put(i,weight);
                LinkedList<BigInteger> index = new LinkedList<>();
                index.add(x.getIndex());
                imap.put(i,index);
                LinkedList<T> value = new LinkedList<>();
                value.add((T)x.get(clazzIndex));
                vmap.put(i, value);
                i++;
            } else {
                wmap.get(pos).add(x.getWeight());
                imap.get(pos).add(x.getIndex());
                vmap.get(pos).add((T)x.get(clazzIndex));
            }
        }
        
        for (int j=0; j<tmap.size(); j++) {
            double size = wmap.get(j).size();
            double avg = 0;
            for (double d : wmap.get(j))
                avg += d;
            avg = avg / size;
            
            BigInteger index = Dovetailing.dtVec(imap.get(j));
            T calculated = map.collectOf(vmap.get(j));
            Object obj[] = new Object[oldschema.length];
            System.arraycopy(tmap.get(j).get(), 0, obj, 0, newschemaleng);
            obj[oldschema.length-1] = calculated;
            toret.addRow(avg, index, null, obj);
        }
        
        return toret;
    }

    public static <T> Table projectAndGroupBy(Table one, Class<T> aClass, IMapFunction<T> iMapFunction, Class... braket) {
        Class array[] = new Class[braket.length+1];
        System.arraycopy(braket, 0, array, 0, braket.length);
        array[braket.length] = aClass;
        return groupBy(project(one,array), aClass, iMapFunction);
    }
    
}
