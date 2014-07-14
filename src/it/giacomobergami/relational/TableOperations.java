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

import it.giacomobergami.utils.Dovetailing;
import it.giacomobergami.database.IPhi;
import it.giacomobergami.functional.AbstractProperty;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.tensor.ITensorLayer;
import it.giacomobergami.tensor.Tensor;
import it.giacomobergami.types.PojoGenerator;
import it.giacomobergami.types.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gyankos
 */
public class TableOperations {
    
    public static final IPhi dtvecPhi = new IPhi() {
        @Override
        public BigInteger reindex(BigInteger index) {
            LinkedList<BigInteger> llbi = new LinkedList<>();
            llbi.add(index);
            return Dovetailing.dtVec(llbi);
        }
    };
    
    
    public static final IPhi invdtvecPhi = new IPhi() {
        @Override
        public BigInteger reindex(BigInteger index) {
            return Dovetailing.dtVecInv(index).get(0);
        }
    };
        
    public static Table reindexing(Table t, IPhi reindex) {
        for (BigInteger i : t.getAllKeys()) {
            t.get(i).setIndex(reindex.reindex(t.get(i).getIndex()));
        }
        return t;
    }
    
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
    
    public static Table project(Table t,Type... L) {
        Table tmp = new Table("tmp", L);
        Table toret = new Table(t.getName()+" over L", L);
        Map<BigInteger,List<Double>> wmap = new HashMap<>();
        Map<BigInteger,List<BigInteger>> imap = new HashMap<>();
        Class vec[] = new Class[L.length];
        for (int j=0; j<L.length; j++)
            vec[j] = L[j].getPojoClass();
        
        BigInteger i = BigInteger.ZERO;

        for (Tuple y : t) {
            Tuple x = projectTuple(y, vec);
            BigInteger pos = tmp.containsValuesPos(x);
            if (pos.equals(Table.ERROR)) {
                tmp.addRow(1,x.get().clone());
                LinkedList<Double> weight = new LinkedList<>();
                weight.add(x.getWeight());
                wmap.put(i,weight);
                LinkedList<BigInteger> index = new LinkedList<>();
                index.add(x.getIndex());
                imap.put(i,index);
                i = i.add(BigInteger.ONE);
            } else {
                wmap.get(pos).add(x.getWeight());
                imap.get(pos).add(x.getIndex());
            }
        }
        
        for (BigInteger j : tmp.getAllKeys()) {
            double size = wmap.get(j).size();
            double avg = 0;
            for (double d : wmap.get(j))
                avg += d;
            avg = avg / size;
            tmp.get(j).setWeight(avg);
            //System.out.println("~~~~~");
            /*for (BigInteger x: imap.get(j)) {
                System.out.println(x);
            }*/
            //System.out.println("~~~~~");
            tmp.get(j).setIndex( Dovetailing.dtVec(imap.get(j)));
            toret.add(tmp.get(j));
        }
        
        return toret;
    }
    
    public static Table select(Table t, AbstractProperty prop){
        Table toret = new Table(t.getName(),t.getSchema());
        Iterator<Tuple> li = t.iterator();
        while (li.hasNext()) {
            Tuple current = li.next();
            if (prop == null)
                toret.add(current);
            else if (prop.prop(current))
                toret.add(current);
        }
        return toret;
    }
    
    public static Table union(Collection<Table> lt) {
        if (lt==null)
            return null;
        if (lt.isEmpty())
            return new Table("empty table on union"); //with no size
        Type cls[] = null;
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
    
    public static <T> Table calc(Table t, Type clazz, ICalc op) {
        int len = t.getSchema().length;
        Type clazzes[] = new Type[len+1];
        System.arraycopy(t.getSchema(),0,clazzes,0,len);
        clazzes[len] = clazz;
        Table toret = new Table(t.getName()+" Calc with "+clazz.getName(),clazzes);
        for (Tuple tup : t) {
            Object objs[] = new Object[len+1];
            System.arraycopy(tup.get(), 0, objs, 0, len);
            objs[len] = op.calcResult(tup);
            toret.addRow(tup.getWeight(), tup.getIndex(), null, objs);
        }
        return toret;
    }
    

    
    public static <T  extends ITensorLayer> Table join(Table left, AbstractJoinProperty<T> prop, Table right) {
        LinkedList<Type> ll = new LinkedList<>();
        ll.addAll(Arrays.asList(left.getSchema()));
        ll.addAll(Arrays.asList(right.getSchema()));
        Table toret = new Table(left.getName()+"|><|"+right.getName(), ll.toArray(new Type[0]));
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
    
    public static <T extends ITensorLayer> Table leftTiedJoin(Table left, AbstractJoinProperty prop, Table right, Tensor<T> tleft, Tensor<T> tright) {
        LinkedList<Type> ll = new LinkedList<>();
        ll.addAll(Arrays.asList(left.getSchema()));
        ll.addAll(Arrays.asList(right.getSchema()));
        Table toret = new Table(left.getName()+"|><|"+right.getName(), ll.toArray(new Type[0]));
        for (Tuple lt : left) {
            for (Tuple rt : right) {
                if (prop.property(lt, rt)) {
                    Object toadd[] = new Object[ll.size()];
                    for (int i=0;i<left.getSchema().length; i++)
                        toadd[i] = lt.get(i);
                    for (int i=0;i<right.getSchema().length; i++)
                        toadd[left.getSchema().length+i] = rt.get(i);
                    
                    LinkedList<String> commonLayers = new LinkedList<>(tleft.keySet());
                    commonLayers.retainAll(tright.keySet());
                    if (commonLayers.size()==0)
                        continue;
                    double avg = 0;
                    for (String k : commonLayers)
                        avg = tright.get(k).get(lt.getIndex(),rt.getIndex()) + tleft.get(k).get(lt.getIndex(),rt.getIndex()) + avg;
                    avg = avg / (commonLayers.size()*2.0);
                    
                    toret.addRow(lt.getWeight()*avg, Dovetailing.dt(lt.getIndex(), rt.getIndex()), null, toadd);
                }
            }
        }
        return toret;
    }
    
    public static Table groupBy(Table t, Type clazz, IMapFunction map) {
        Type[] oldschema = t.getSchema();
        Table toret = new Table(t.getName()+" Group by "+clazz.getName(), oldschema);
        LinkedList<Type> llc = new LinkedList<>();
        int clazzIndex = -1;
        int cidx = 0;
        for (Type c : oldschema) {
            if (!c.equals(clazz))
                llc.add(c);
            else
                if (clazzIndex ==-1)
                    clazzIndex = cidx;
            cidx++;
        }
        Type clazzes[] = llc.toArray(new Type[0]);
        Table tmap = new Table("tmp",clazzes);
        int newschemaleng = tmap.getSchema().length;
        Map<BigInteger,List<Double>> wmap = new HashMap<>();
        Map<BigInteger,List<BigInteger>> imap = new HashMap<>();
        Map<BigInteger,List<Object>> vmap = new HashMap<>();
        
        BigInteger i = BigInteger.ZERO;

        for (Tuple x : t) {
            Tuple pt = projectTuple(x, tmap.getSchemaClasses());
            BigInteger pos = tmap.containsValuesPos(pt);
            if (pos.equals(Table.ERROR)) {
                tmap.addRow(1, pt.get().clone());
                LinkedList<Double> weight = new LinkedList<>();
                weight.add(x.getWeight());
                wmap.put(i,weight);
                LinkedList<BigInteger> index = new LinkedList<>();
                index.add(x.getIndex());
                imap.put(i,index);
                LinkedList<Object> value = new LinkedList<>();
                value.add(x.get(clazzIndex));
                vmap.put(i, value);
                i = i.add(BigInteger.ONE);
            } else {
                wmap.get(pos).add(x.getWeight());
                imap.get(pos).add(x.getIndex());
                vmap.get(pos).add(x.get(clazzIndex));
            }
        }
        
        for (BigInteger j : tmap.getAllKeys()) {
            double size = wmap.get(j).size();
            double avg = 0;
            for (double d : wmap.get(j))
                avg += d;
            avg = avg / size;
            
            BigInteger index = Dovetailing.dtVec(imap.get(j));
            Object calculated = map.collectOf(vmap.get(j));
            Object obj[] = new Object[oldschema.length];
            System.arraycopy(tmap.get(j).get(), 0, obj, 0, newschemaleng);
            obj[oldschema.length-1] = calculated;
            toret.addRow(avg, index, null, obj);
        }
        
        return toret;
    }

    public static <T> Table projectAndGroupBy(Table one, Type aClass, IMapFunction iMapFunction, Type... braket) {
        Type array[] = new Type[braket.length+1];
        System.arraycopy(braket, 0, array, 0, braket.length);
        array[braket.length] = aClass;
        return groupBy(project(one,array), aClass, iMapFunction);
    }
    
    /*public static Table projectAndGroupBy(Table one, Type aType, IMapFunction iMapFunction, Type... braket){
        Class array[] = new Class[braket.length];
        for (int i=0; i<braket.length; i++) 
            array[i] = braket[i].getJavaClass();
        return projectAndGroupBy(one, aType.getJavaClass(), iMapFunction, array);
    }*/
    
    public static Table rename(Table one, final Type source, final Type dest) {
        ICalc renameop = new ICalc() {
            @Override
            public Object calcResult(Tuple t) {
                return dest.create(PojoGenerator.getPojoValue(t.get(source)));
            }
        };
        Table calced = calc(one, dest, renameop);
        LinkedList<Type> toproj = new LinkedList<>(Arrays.asList(one.getSchema()));
        toproj.remove(source);
        toproj.add(dest);
        return project(calced, toproj.toArray(new Type[0]));
    }
    
}
