/*
 * HyperEdge.java
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
