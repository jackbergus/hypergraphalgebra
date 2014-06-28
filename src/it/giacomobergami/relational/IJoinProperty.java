/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.relational;

import it.giacomobergami.functional.Tuple;

/**
 *
 * @author gyankos
 */
public interface IJoinProperty {
    
    public boolean property(Tuple left, Tuple right);
    
}
