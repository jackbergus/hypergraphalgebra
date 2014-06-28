/*
 * Dovetailing.java
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

package it.giacomobergami.relational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

/**
 *
 * @author gyankos
 */
public class Dovetailing {
    
    public static final BigInteger TWO = BigInteger.ONE.add(BigInteger.ONE);
    public static final BigInteger EIGHT = BigInteger.TEN.subtract(TWO);
    
    public static BigInteger summate(BigInteger k) {
        return k.multiply(k.add(BigInteger.ONE)).divide(TWO);
    }
    
    public static BigInteger dt(BigInteger i, BigInteger j) {
        BigInteger ij = i.add(j);
        return summate(ij).add(j);
    }
    
    public static BigInteger index(int i) {
        return new BigInteger(Integer.toString(i));
    }
    
private static final BigDecimal SQRT_DIG = new BigDecimal(150);
private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

/**
 * Private utility method used to compute the square root of a BigDecimal.
 * 
 * @author Luciano Culacciatti 
 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
 */
private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
    BigDecimal fx = xn.pow(2).add(c.negate());
    BigDecimal fpx = xn.multiply(new BigDecimal(2));
    BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN);
    xn1 = xn.add(xn1.negate());
    BigDecimal currentSquare = xn1.pow(2);
    BigDecimal currentPrecision = currentSquare.subtract(c);
    currentPrecision = currentPrecision.abs();
    if (currentPrecision.compareTo(precision) <= -1){
        return xn1;
    }
    return sqrtNewtonRaphson(c, xn1, precision);
}

/**
 * Uses Newton Raphson to compute the square root of a BigDecimal.
 * 
 * @author Luciano Culacciatti 
 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
 */
public static BigDecimal bigSqrt(BigDecimal c){
    return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
}
    
    public static BigInteger[] dtInv(BigInteger couple) {
        BigInteger toret[] = new BigInteger[2];
        BigInteger tosquare = BigInteger.ONE.add(EIGHT.multiply(couple));
        BigDecimal sq = bigSqrt(new BigDecimal(tosquare)).subtract(BigDecimal.ONE).divide(BigDecimal.ONE.add(BigDecimal.ONE),RoundingMode.FLOOR);
        BigInteger dmax = sq.toBigInteger();
        toret[1] = couple.subtract(summate(dmax));
        toret[0] = dmax.subtract(toret[1]);
        return toret;
    }
    
    private static BigInteger dtvecr(List<BigInteger> r) {
        if (r==null)
            return index(0);
        if (r.isEmpty())
            return index(0);
        if (r.size()==1)
            return r.get(0);
        else {
            Stack<BigInteger> s = new Stack<>();
            for (int j = r.size()-1; j>=0; j--) {
                BigInteger toadd = r.get(j);
                s.push(toadd);
                if (s.size()==2) {
                    BigInteger first = s.pop();
                    BigInteger last = s.pop();
                    s.push(dt(first,last));
                }
            }
            return s.pop();
        }
            
    }
    
    public static BigInteger dtVec(List<BigInteger> l) {
        return dt(index(l.size()),dtvecr(l));
    }
    
    public static List<BigInteger> dtVecInv(BigInteger bi) {
        BigInteger sl[] = dtInv(bi);
        int size = sl[0].intValue();
        List<BigInteger> lbi = new LinkedList<>();
        BigInteger prev = sl[1];
        for (int n=size; n>0; n--) {
            if (n==1) 
                lbi.add(prev);
            else {
                BigInteger asl[] = dtInv(prev);
                lbi.add(asl[0]);
                prev = asl[1];
            }
        }
        return lbi;
    }
    
    
}
