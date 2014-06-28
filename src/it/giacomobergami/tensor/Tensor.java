/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
