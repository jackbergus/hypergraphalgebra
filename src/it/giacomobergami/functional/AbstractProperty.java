/*
 * IProperty.java
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

import it.giacomobergami.tensor.ITensorLayer;
import it.giacomobergami.tensor.Tensor;

/**
 *
 * @author gyankos
 */
public abstract class AbstractProperty<T  extends ITensorLayer> {
    
   private Tensor<T> ten;
   public AbstractProperty(Tensor<T> t) {
       this.ten = t;
   }
   
   public abstract boolean prop(Tuple tup,Tensor<T> ten);
    
   public boolean prop(Tuple tup) {
       return prop(tup,this.ten);
   }
    
}