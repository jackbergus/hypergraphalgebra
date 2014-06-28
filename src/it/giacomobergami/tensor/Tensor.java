/*
 * Tensor.java
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


package it.giacomobergami.tensor;

import it.giacomobergami.relational.Dovetailing;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gyankos
 * @param <T>
 */
public class Tensor<T extends ITensorLayer> extends HashMap<String,T>  {
    
    private Class<T> clazck;
    public Tensor(Class<T> clz) {
        clazck = clz;
    }
    
    public Class<T> getLayersClass() {
        return clazck;
    }
    
    public void set(BigInteger i, BigInteger j, String layer, double value) {
          if (!containsKey(layer))
                createNewLayer(layer);
          get(layer).set(i,j,value);
    }
    
    public void set(int i, int j, String layer, double value) {
        set(Dovetailing.index(i),Dovetailing.index(j),layer,value);
    }
    
    public boolean createNewLayer(String name) {
        T layer = newLayer();
        if (containsKey(name) || (layer==null))
            return false;
        put(name,layer);
        return true;
    }
    
    @Override 
    public T get(Object k) {
        if (k==null)
            return null;
        if (!(k instanceof String))
            return null;
        if (containsKey((String)k))
            return super.get(k);
        else
            return newLayer();
    }
    
    private T newLayer() {
        try {
            try {
                return clazck.getConstructor().newInstance();
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Tensor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Tensor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(Tensor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Tensor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Tensor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Tensor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
