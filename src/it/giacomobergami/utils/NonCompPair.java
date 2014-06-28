/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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