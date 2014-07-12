/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.functional;

import it.giacomobergami.types.Type;
import java.util.Objects;

/**
 *
 * @author gyankos
 */
public class Value {
    
    private Object value;
    private Type type;
    
    public Value(Object value, Type type) {
         this.type = type;
        if (type.hasObject(value))
            this.value = value;
        else
            this.value = null;
    }
    
    public Object getValue() {
        return value;
    }
    public Type getType() {
        return type;
    }
    public String getTypeName() {
        return type.getName();
    }
    public Class getJavaClass() {
        return type.getClass();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Value))
            return false;
        Value v = (Value)obj;
        return (v.getValue().equals(value) && v.getType().equals(type));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.value);
        hash = 11 * hash + Objects.hashCode(this.type);
        return hash;
    }
    
}
