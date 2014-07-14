/*
 * ITensorLayer.java
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



import java.util.Set;

import com.google.common.collect.Table;
import it.giacomobergami.utils.NonCompPair;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
/**
 *
 * @author gyankos
 */
public interface ITensorLayer {
   
   public ITensorLayer newInstance();
    
   public BigInteger nCols();
   public BigInteger nRows();
    
   public int nEntities();
   public HashSet<BigInteger> Entities();
   
   public double get(BigInteger i, BigInteger j);
   /**
    * Returns the cell's value
    * @param x cell coordinates
    * @return 
    */
   public double get(NonCompPair<BigInteger,BigInteger> x);
   
   /**
    * Adds val to the x cell value
    * @param x      Cell coordinates
    * @param val    Value
    * @ 
    */
   public void incr(NonCompPair<BigInteger,BigInteger> x, double val) ;
   
   /**
    * Removes the cell (i,j)s
    * @param i  Row
    * @param j  Column
    */
   public void rem(BigInteger i, BigInteger j);
   
   /**
    * Add val to the cell (i,j)
    * @param i  row
    * @param j  Column
    * @param val    Value
    * @ 
    */
   public void incr(BigInteger i, BigInteger j, double val) ;
   
   
   public void set(BigInteger i, BigInteger j,double val) ;
   
   public void set(NonCompPair<BigInteger,BigInteger> x,double val) ;
   
   /**
    * Removes cells that have non-empty values
    * @return
    * @ 
    */
   public Set<Table.Cell<BigInteger,BigInteger,Double>> getValueRange() ;
   
   /**
    * Removes the whole row i
    * @param i 
    */
   public void removeRow(BigInteger i);
   
   /**
    * Removes the whole column j
    * @param j 
    */
   public void removeCol(BigInteger j);
   
   /**
    * Removes both row and columns with the same number
    * @param elem 
    */
   public void removeEnt(BigInteger elem);
   
   public void sum(ITensorLayer right);
   public void diff(ITensorLayer right);
   
   public Set<BigInteger> getInSet(BigInteger ent);
   
   public Set<BigInteger> getOutSet(BigInteger ent);
    
}
