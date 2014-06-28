/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.tensor;

import it.giacomobergami.functional.Tuple;
import java.math.BigInteger;

/**
 *
 * @author gyankos
 */
public class HyperEdge  {
    
    private Tuple data;
    private String lay;
    private double wei;
    
    public HyperEdge(Tuple source, Tuple destin, String Layer, double weight) {
        Class  []claz = new Class[source.size()+destin.size()];
        System.arraycopy(source.getSchema(),0,claz,0,source.size());
        System.arraycopy(destin.getSchema(),0,claz,source.size(),destin.size());
        data = new Tuple(claz);
        
        Object []vals = new Object[source.size()+destin.size()];
        System.arraycopy(source.get(), 0, vals, 0, source.size());
        System.arraycopy(destin.get(),0,vals,source.size(),destin.size());
        data.set(vals);
        
        lay = Layer;
        wei = weight;
    }
    
    public Object[] get() { return data.get(); }
    public Class[] getSchema() { return data.getSchema(); }
    public String getLayer() { return lay; }
    public double getWeight() { return wei; }
    
}
