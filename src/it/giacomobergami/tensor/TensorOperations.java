/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
