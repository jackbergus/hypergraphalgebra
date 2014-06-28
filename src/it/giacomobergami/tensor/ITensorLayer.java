/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.tensor;



import java.util.Set;

import com.google.common.collect.Table;
import it.giacomobergami.utils.NonCompPair;
import java.math.BigInteger;
/**
 *
 * @author gyankos
 */
public interface ITensorLayer {
   
   public ITensorLayer newInstance();
    
   public BigInteger nCols();
   public BigInteger nRows();
    
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