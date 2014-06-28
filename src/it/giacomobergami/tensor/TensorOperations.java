/*
 * TensorOperations.java
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

import it.giacomobergami.database.Database;
import it.giacomobergami.relational.Dovetailing;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gyankos
 * @param <T>
 */
public class TensorOperations {
    
    public static <T extends ITensorLayer> Tensor<T> Update(Database updatedOne, Tensor<T> tensor) {
        Tensor<T> toret = new Tensor<>(tensor.getLayersClass());
        Set<BigInteger> allKeys = updatedOne.getAllKeys();
        for (BigInteger x: allKeys) {
            System.out.println(x);
        }
        for (String layer : tensor.keySet()) {
            for (BigInteger dtx : allKeys) {
                List<BigInteger> vx = Dovetailing.dtVecInv(dtx);
                for (BigInteger dty : allKeys) {
                    List<BigInteger> vy = Dovetailing.dtVecInv(dty);
                    double size = vx.size() * vy.size();
                    if (size == 0)
                        continue;
                    double avg = 0;
                    for (BigInteger x : vx)
                        for (BigInteger y : vy)
                            avg = avg+tensor.get(layer).get(x, y);
                    avg = avg / size;
                    toret.set(dtx, dty, layer, avg);
                }
            }
        }
        return toret;
    }
    
}
