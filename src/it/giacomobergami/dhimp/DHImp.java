/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.dhimp;

import it.giacomobergami.database.Database;
import it.giacomobergami.tensor.ITensorLayer;
import it.giacomobergami.tensor.Tensor;

/**
 *
 * @author gyankos
 * @param <T>
 */
public class DHImp<T extends ITensorLayer> {
    
    private Database first;
    private final Tensor<T> second;
    
    public DHImp(Database db, Tensor<T> tensor) {
        first = db;
        second = tensor;
    }
    
    public Database getDB() {
        return first;
    }
    public Tensor<T> getT() {
        return second;
    }
    
    public Class<T> getTClass() {
        return second.getLayersClass();
    }
    
}
