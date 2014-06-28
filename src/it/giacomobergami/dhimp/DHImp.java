/*
 * DHImp.java
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
