/*
 * Table.java
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
import it.giacomobergami.types.PojoGenerator;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.functional.Void;
import it.giacomobergami.types.Type;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gyankos
 */
public class Table implements List<Tuple> {
    
    private Type types[];
    private Class classes[];
    private String tName;
    private BigInteger countFrom;
    private Map<BigInteger,Tuple> tupz;
    public static BigInteger ERROR = new BigInteger(Integer.toString(-1));
    //private boolean isPojo;
    
    public Set<BigInteger> getAllKeys() {
        return tupz.keySet();
    }
    
    public Table(String name) {
        this.types = new Type[0];
        this.classes = new Class[0];
        tupz = new HashMap<>();
        tName = name;
        countFrom = BigInteger.ZERO;
        //isPojo = false;
    }
    
    /*public Table(String name, Class ... clazzes) {
        this.classes = clazzes;
        tupz = new HashMap<>();
        tName = name;
        countFrom = BigInteger.ZERO;
        isPojo = false;
    }*/
    
    public Table(String name, Type  ... clazzes) {
        this.types = new Type[clazzes.length];
        this.classes = new Class[clazzes.length];
        System.arraycopy(clazzes, 0, this.types, 0, clazzes.length);
        for (int i=0; i<clazzes.length; i++)
            this.classes[i] = PojoGenerator.getPojoClass(clazzes[i]);
        tupz = new HashMap<>();
        tName = name;
        countFrom = BigInteger.ZERO;
        //isPojo = true;
    }
    
    public Table(String name, BigInteger startIndex, Type ... clazzes) {
        this.types = new Type[clazzes.length];
        this.classes = new Class[clazzes.length];
        System.arraycopy(clazzes, 0, this.types, 0, clazzes.length);
        for (int i=0; i<clazzes.length; i++)
            this.classes[i] = PojoGenerator.getPojoClass(clazzes[i]);
        tupz = new HashMap<>();
        tName = name;
        countFrom = startIndex;
        //isPojo = true;
    }
    
    /*public Table(String name, BigInteger startIndex, Class ... clazzes) {
        this.classes = clazzes;
        tupz = new HashMap<>();
        tName = name;
        countFrom = startIndex;
        isPojo = false;
    }*/
    
    public Type[] getSchema() {
        return types;
    }
    
    public Class[] getSchemaClasses() {
        return classes;
    }
    
    public String getName() {
        return tName;
    }
    
    public void forceName(String s){
        tName = s;
    }
    
    public Tuple createEmptyRow() {
        return new Tuple(classes);
    }
    
    public Tuple createRow(double weight, BigInteger idx, Object... objs) {
        Tuple toret = new Tuple(true,classes);
        toret.set(objs);
        toret.setWeight(weight);
        toret.setIndex(idx);
        return toret;
    }
    
    public Tuple createPojoRow(double weight, BigInteger idx, Object... objs) {
        Tuple toret = new Tuple(true,classes);
        Object array[] = new Object[objs.length];
        for (int i=0; i<objs.length; i++)
            array[i] = PojoGenerator.getNewPojoInstance(types[i], objs[i]);
        toret.set(array);
        toret.setWeight(weight);
        toret.setIndex(idx);
        return toret;
    }
    
    public boolean addRow(double weight, BigInteger idx, Void v, Object...objs) {
        return add(createRow(weight,idx,objs));
    }
    
    public boolean addPojoRow(double weight, BigInteger idx, Void v, Object...objs) {
        return add(createPojoRow(weight,idx,objs));
    }
    
    public boolean addRow(double weight, int idx, Void v, Object...objs) {
        return add(createRow(weight,Dovetailing.index(idx),objs));
    }
    
    public boolean addPojoRow(double weight, int idx, Void v, Object...objs) {
        return add(createPojoRow(weight,Dovetailing.index(idx),objs));
    }
    
    public boolean addRow(double weight, Object...objs) {
        boolean toret = add(createRow(weight,countFrom,objs));
        countFrom = countFrom.add(BigInteger.ONE);
        return toret;
    }
    
    public BigInteger addPojoRow(double weight, BigInteger index, Object...objs) {
        add(createPojoRow(weight,index,objs));
        BigInteger toret = index;
        countFrom = index.add(BigInteger.ONE);
        return toret;
    }
    
    public BigInteger addPojoRow(double weight, Object...objs) {
        boolean result = add(createPojoRow(weight,countFrom,objs));
        BigInteger toret = countFrom;
        countFrom = countFrom.add(BigInteger.ONE);
        if (!result)
            return ERROR;
        else
            return toret;
    }

    @Override
    public int size() {
        return tupz.size();
    }

    @Override
    public boolean isEmpty() {
        return tupz.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o==null)
            return false;
        if (!(o instanceof Tuple))
            return false;
        return tupz.containsValue((Tuple)o);
    }
    
    public BigInteger containsValuesPos(Tuple tup) {
        for (BigInteger x:tupz.keySet()) {
            if (tup.equalsToTuple(tupz.get(x)))
                return x;
        }
        return ERROR;
    }
    
    public boolean containsValues(Tuple tup) {
        return (!containsValuesPos(tup).equals(ERROR));
    }
    
    public boolean containsKey(BigInteger i) {
        return tupz.containsKey(i);
    }

    @Override
    public Iterator<Tuple> iterator() {
        return tupz.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return tupz.values().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return tupz.values().toArray(a);
    }

    @Override
    public boolean add(Tuple e) {
        if (!tupz.containsValue(e)) {
            return (this.set(e.getIndex(), e)!=null);
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (!contains(o))
            return false;
        return (tupz.remove(((Tuple)o).getIndex()).equals(o));
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean toret = true;
        for (Object x : c) 
            toret = toret && contains(x);
        return toret;
    }

    @Override
    public boolean addAll(Collection<? extends Tuple> c) {
        boolean toret = true;
        for (Tuple x : c)
            toret = toret && add(x);
        return toret;
    }

    @Override @Deprecated
    public boolean addAll(int index, Collection<? extends Tuple> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean toret = true;
        for (Object x : c)
            toret = toret && remove(x);
        return toret;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c==null)
            return false;
        boolean toret = true;
        for (BigInteger i : tupz.keySet()) {
            if (!c.contains(tupz.get(i))) 
                remove(i);
        }
        return (tupz.size()>=c.size());
    }

    @Override
    public void clear() {
        tupz.clear();
    }

    @Override
    public Tuple get(int index) {
        return get(Dovetailing.index(index));
    }
    
    public Tuple get(BigInteger bi) {
        return tupz.get(bi);
    }

    public Tuple set(BigInteger index, Tuple element) {
        if (element.getIndex().equals(index) && Arrays.equals(element.getSchema(), classes)) {
            return tupz.put(index, element);
        }
        else return null;
    }
    
    @Override
    public Tuple set(int index, Tuple element) {
        return set(Dovetailing.index(index),element);
    }

    @Override
    public void add(int index, Tuple element) {
        set(index, element);
    }

    public Tuple remove(BigInteger index) {
        return tupz.remove(index);
    }
    
    @Override
    public Tuple remove(int index) {
        return remove(Dovetailing.index(index));
    }
    
    public BigInteger indexOfBigInteger(Object o) {
        
        if (o==null)
            return ERROR;
        if (!(o instanceof Tuple))
            return ERROR;
        Tuple tup = (Tuple)o;
        if (tupz.get(tup.getIndex()).equals(tup)) 
            return tup.getIndex();
        return new BigInteger(Integer.toString(-1));
    }

    @Override
    public int indexOf(Object o) {
        return (indexOfBigInteger(o).intValue());
    }

    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    @Override @Deprecated
    public ListIterator<Tuple> listIterator() {
        return null;
    }

    @Override @Deprecated
    public ListIterator<Tuple> listIterator(int index) {
        return null;
    }

    @Override
    public List<Tuple> subList(int fromIndex, int toIndex) {
        List<Tuple> toret = new LinkedList<>();
        BigInteger fib = new BigInteger(Integer.toString(fromIndex));
        BigInteger tib = new BigInteger(Integer.toString(toIndex));
        for (BigInteger bi : tupz.keySet()) {
            if (bi.subtract(fib).signum()>=0 && tib.subtract(bi).signum()>=0) {
                toret.add(tupz.get(bi));
            }
        }
        return toret;
    }
    
    
    
}
