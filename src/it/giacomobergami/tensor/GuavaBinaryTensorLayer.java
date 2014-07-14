/*
 * GuavaBinaryTensorLayer.java
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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
    
    @Override
    public String toString() {
        String toret = "";
        for (BigInteger row : p.rowKeySet()) {
            for (BigInteger col : p.row(row).keySet()) {
                toret += "M["+row+","+col+"]="+p.get(row, col)+"\n";
            }
        }
        return toret;
    }

    @Override
    public int nEntities() {
        return Entities().size();
    }

    @Override
    public HashSet<BigInteger> Entities() {
        HashSet<BigInteger> ls = new HashSet<>();
        ls.addAll(p.columnKeySet());
        ls.addAll(p.rowKeySet());
        return ls;
    }
    
}
