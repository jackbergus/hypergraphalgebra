/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.functional;

/**
 *
 * @author gyankos
 */
public interface IFunction<T> {
    
    public T fun(Tuple input);
    
}