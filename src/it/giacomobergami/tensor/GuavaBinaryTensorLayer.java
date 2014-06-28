/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.tensor;

/**
 *
 * @author gyankos
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import it.giacomobergami.utils.NonCompPair;
import java.math.BigInteger;

/**
 *
 * @author gyankos
 */
public class GuavaBinaryTensorLayer implements ITensorLayer, Serializable {

	private static final long serialVersionUID = 1942775687702205962L;
	private final Table<BigInteger,BigInteger,Double> p;
	private BigInteger row_min = BigInteger.ZERO;
	private BigInteger row_max = BigInteger.ZERO;
	private BigInteger col_min = BigInteger.ZERO;
	private BigInteger col_max = BigInteger.ZERO;

	
    public GuavaBinaryTensorLayer() {
        p =  HashBasedTable.create();
    }

    public GuavaBinaryTensorLayer(ITensorLayer cpy) {
        p =  HashBasedTable.create();
        for (Cell<BigInteger, BigInteger, Double> db:cpy.getValueRange()) {
            p.put(db.getRowKey(), db.getColumnKey(), db.getValue());//set
        }
    }
    
    @Override
    public BigInteger nCols() {
        return col_max.add(BigInteger.ONE);
    }

    @Override
    public BigInteger nRows() {
        return row_max.add(BigInteger.ONE);
    }

    @Override
    public double get(BigInteger i, BigInteger j) {
    	if (p.contains(i, j))
    		return p.get(i, j);
    	else
    		return 0;
    }

    @Override
    public double get(NonCompPair<BigInteger, BigInteger> x) {
        return get(x.getFirst(),x.getSecond());
    }

    @Override
    public void incr(NonCompPair<BigInteger, BigInteger> x, double val) {
        incr(x.getFirst(),x.getSecond(),val);
    }

    @Override
    public void rem(BigInteger i, BigInteger j) {
    	remIndexUpdate(i, j);
        p.remove(i, j);
    }

    @Override
    public void incr(BigInteger i, BigInteger j, double val) {        
        double tmp = val;
        if (p.contains(i,j))
            tmp = get(i,j)+val;
        set(i,j, val);
    }
    
    private void setIndexUpdate(BigInteger i, BigInteger j) {
        if (row_min.subtract(i).signum()==1) {
            row_min = i;
        }
        if (i.subtract(row_max).signum()==1) {
            row_max = i;
        }
        if (col_min.subtract(j).signum()==1) {
            col_min = j;
        }
        if (j.subtract(col_max).signum()==1) {
            col_max = j;
        }
    }
    
    private void remIndexUpdate(BigInteger i, BigInteger j) {
    	if (i == row_max)
    		row_max = row_max.subtract(BigInteger.ONE);
    	if (j == col_max)
    		col_max = col_max.subtract(BigInteger.ONE);
    	if (i == row_min)
    		row_min = row_min.add(BigInteger.ONE);
    	if (j == col_min)
    		col_min = col_min.add(BigInteger.ONE);
    }

    @Override
    public void set(BigInteger i, BigInteger j, double val) {
    	if (val==0 && p.contains(i, j)) {
    		rem(i, j);
    	} else if (val!=0) {
    		setIndexUpdate(i, j);
    		p.put(i,j,val);
    	}
    }

    @Override
    public void set(NonCompPair<BigInteger, BigInteger> x, double val) {
        set(x.getFirst(),x.getSecond(),val);
    }
    
    public void set(Table.Cell<BigInteger,BigInteger,Double> db) {
        p.put(db.getRowKey(), db.getColumnKey(), db.getValue());
    }

    @Override
    public Set<Table.Cell<BigInteger, BigInteger, Double>> getValueRange() {
        return p.cellSet();
    }

    @Override
    public void removeRow(BigInteger i) {
        for (BigInteger j : p.row(i).keySet()) {
            rem(i, j);
        }
    }

    @Override
    public void removeCol(BigInteger j) {
        for (BigInteger i: p.column(j).keySet()) {
        	rem(i, j);
        }
    }

    @Override
    public void removeEnt(BigInteger elem) {
        removeRow(elem);
        removeCol(elem);
    }

    @Override
    public void sum(ITensorLayer right) {
        for (Table.Cell<BigInteger, BigInteger, Double> x:right.getValueRange()) {
            incr(x.getRowKey(),x.getColumnKey(), x.getValue());
        }
    }

    @Override
    public void diff(ITensorLayer right) {
        for (Table.Cell<BigInteger, BigInteger, Double> x:right.getValueRange()) {
            incr(x.getRowKey(),x.getColumnKey(), -x.getValue());
        }
    }
    
    @Override
    public Set<BigInteger> getInSet(BigInteger ent) {
        return p.column(ent).keySet();
    }
    
    @Override
    public Set<BigInteger> getOutSet(BigInteger ent) {
        return p.row(ent).keySet();
    }

    @Override
    public ITensorLayer newInstance() {
        return new GuavaBinaryTensorLayer();
    }
    
}