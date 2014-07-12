/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author gyankos
 */
public class Type implements Comparable<Type>{
    
    private String name;
   // private static Map<Class,Type> types = new HashMap<>();
    private Class clazz;
    
    //public final static Type TOP = new Type("Object",Object.class);

    public Class getJavaClass() {
        return clazz;
    }
    
    public Type(String name, Class cl) {
        this.name = name;
        this.clazz = cl;
    }
    
    /*public static Type createType(String name, Class type) {
        if (!types.containsKey(type)) {
            Type t = new Type(name,type);
            types.put(type, t);
            return t;
        }
        return null;
    }
    
    public static Type creatorType(String name) {
        return createType(name, Object.class);
    }
    
    public static boolean hasClass(Class cl) {
        return types.containsKey(cl);
    }
    
    public static Type getType(Class cl) {
        return types.get(cl);
    }*/
    
    public boolean hasObject(Object o) {
        return o.getClass().equals(this.clazz);
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj==null)
            return false;
        if (!(obj instanceof Type))
            return false;
        Type t = (Type)obj;
        return (name.equals(t.getName()) && clazz.equals(t.getJavaClass()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.clazz);
        return hash;
    }

    @Override
    public int compareTo(Type o) {
        if (this.equals(o))
            return 0;
        else
            return name.compareTo(o.getName());
    }
    
    public Object create(Object value) {
        return PojoGenerator.getNewPojoInstance(this, value);
    }
    
    public Class getPojoClass() {
        return PojoGenerator.getPojoClass(this);
    }
    
    
}
