/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.relational;

import java.util.Collection;

/**
 *
 * @author gyankos
 */
public interface IMapFunction<CollectAndReturn> {
    
    public CollectAndReturn collectOf(Collection<CollectAndReturn> collection);
    
}
