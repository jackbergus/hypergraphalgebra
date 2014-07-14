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

import com.google.common.collect.Table;
import it.giacomobergami.database.Database;
import it.giacomobergami.utils.Dovetailing;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gyankos
 * @param <T>
 */
public class TensorOperations {
    
    public static <T extends ITensorLayer> Tensor<T> TUpdate(Database updatedOne, Tensor<T> tensor) {
        Tensor<T> toret = new Tensor<>(tensor.getLayersClass());
        Set<BigInteger> allKeys = updatedOne.getAllRowsKeys();
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
    
    /*public static <T extends ITensorLayer> Tensor<T> TEdgeSelect(Database ndb, Tensor<T> tensor, IHedgeProp prop) {
        Tensor<T> toret = tensor.createNewTensor();
        for (String layername : tensor.keySet()) {
            T layer = tensor.get(layername);
            for (Table.Cell<BigInteger, BigInteger, Double> cell:layer.getValueRange()) {
                if (prop.prop( new HyperEdge(ndb.getTuple(cell.getRowKey()), ndb.getTuple(cell.getColumnKey()), layername, cell.getValue()))) {
                    toret.set(cell.getRowKey(), cell.getColumnKey(), layername, cell.getValue());
                }
            }
        }
        return toret;
    }*/
    
    public static <T extends ITensorLayer> Tensor<T> TJoin(Database ndb, Collection<String> commonLayers, Tensor<T> tLeft, Tensor<T> tRight) {
         Tensor<T> nt = new Tensor<>(tLeft.getLayersClass());
        Set<BigInteger> allKeys = ndb.getAllRowsKeys();
            for (String x : commonLayers) {
            T xtLeft = tLeft.get(x);
            T xtRight = tRight.get(x);
            for (BigInteger idx : allKeys) {
                BigInteger dtx[] = Dovetailing.dtInv(idx);
                for (BigInteger idy : allKeys) {
                    BigInteger dty[] = Dovetailing.dtInv(idy);
                    double avg = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            avg = avg + xtLeft.get(dtx[i], dty[j]) + xtRight.get(dtx[i], dty[j]);
                        }
                    }
                    avg = avg / 8.0;
                    nt.set(idx, idy, x, avg);
                }
            }
        }
            return nt;
    }
    
}
