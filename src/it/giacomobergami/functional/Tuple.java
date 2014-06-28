/*
 * Tuple.java
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

package it.giacomobergami.functional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author gyankos
 */
public class Tuple {
    
    private Class classes[];
    private Object elems[];
    double w;
    BigInteger index;
    
    public Tuple(Class... clazzes) {
        classes = clazzes;
        elems = new Object[clazzes.length];
    }
    
    public double getWeight() {return w;}
    public void setWeight(double d) { w = d; }
    public BigInteger getIndex() { return index; }
    public void setIndex(BigInteger i) { this.index = i; }
    public Class[] getSchema() { return classes; }
    
    public Tuple(double weight, BigInteger index, Object... objs) {
        elems = objs;
        classes = new Class[objs.length];
        for (int i=0; i<objs.length; i++) {
            classes[i] = objs[i].getClass();
        }
        w = weight;
        this.index = index;
    }
    
    
    public int size() {
        return elems.length;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple))
            return false;
        Tuple tup = (Tuple)o;
        if (w!=tup.getWeight())
            return false;
        if (!index.equals(tup.getIndex()))
            return false;
        return equalsToTuple(tup);
    }
    
    public boolean equalsToTuple(Tuple tup) {
        if (size()!=tup.size())
            return false;
        for (int i=0; i<size(); i++) {
            //System.out.println(get((int)i) + "~" + tup.get((int)i));
            if (!get(i).equals(tup.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Arrays.deepHashCode(this.classes);
        hash = 43 * hash + Arrays.deepHashCode(this.elems);
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.w) ^ (Double.doubleToLongBits(this.w) >>> 32));
        hash = 43 * hash + Objects.hashCode(this.index);
        return hash;
    }
    
    public Object get(int index) {
        return elems[index];
    }
    
    public Object[] get() {
        return elems;
    }
    
    public boolean set(int index, Object val) {
        if (val.getClass().equals(classes[index])) {
            elems[index] = val;
            return true;
        }
        return false;
    }
    
    public boolean set(Object ... elem) {
        if (elem.length!=classes.length)
            return false;
        for (int i=0; i<elem.length; i++) {
            if (!elem[i].getClass().equals(classes[i]))
                return false;
        }
        for (int i=0; i<elem.length; i++) {
            set(i,elem[i]);
        }
        return true;
    }
    
    @Override 
    public String toString() {
        String toret = "<";
        for (Object x: elems) {
            toret += x.toString()+",";
        }
        toret = toret.substring(0, toret.length()-1);
        return toret +"> w="+w+" ~ idx="+index.toString();
    }
    
}
