/*
 * NonCompPair.java
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


package it.giacomobergami.utils;

/**
 * This class implements a pair where both the arguments are not necessairly
 * comparable.
 *
 * @author gyankos
 * @param <A>
 * @param <B>
 */
public class NonCompPair<A, B> {

    private A first;
    private B second;

    /**
     *
     * @param first
     * @param second
     */
    public NonCompPair(A first, B second) {
        super();
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NonCompPair) {
            @SuppressWarnings("rawtypes")
            NonCompPair otherPair = (NonCompPair) other;
            return (this.first.equals(otherPair.getFirst()) &&
                    this.second.equals(otherPair.getSecond()));
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    /**
     *
     * @return
     */
    public A getFirst() {
        return first;
    }

    /**
     *
     * @param first
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     *
     * @return
     */
    public B getSecond() {
        return second;
    }

    /**
     *
     * @param second
     */
    public void setSecond(B second) {
        this.second = second;
    }

}
