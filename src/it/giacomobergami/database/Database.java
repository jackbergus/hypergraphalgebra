/*
 * Database.java
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

package it.giacomobergami.database;

import it.giacomobergami.functional.Tuple;
import it.giacomobergami.relational.Table;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gyankos
 */
public class Database implements Map<String,Table> {

    private Map<String,Table> db;
    
    public Database() {
        db = new HashMap<>();
    }
    
    public Database(Map<String,Table> database) {
        db = database;
    }
    
    @Override
    public int size() {
        return db.size();
    }

    @Override
    public boolean isEmpty() {
        return db.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return db.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return db.containsValue(value);
    }

    @Override
    public Table get(Object key) {
        return db.get(key);
    }
    
    public Tuple getEntityByIndex(BigInteger bi) {
        for (String key : db.keySet()) {
            if (db.get(key).containsKey(bi)) {
                return db.get(key).get(bi);
            }
        }
        return null;
    }
    
    public Set<BigInteger> getAllKeys() {
        Set<BigInteger> toret = new HashSet<>();
        for (String key : db.keySet()) {
            toret.addAll(db.get(key).getAllKeys());
        }
        return toret;
    }

    @Override
    public Table put(String key, Table value) {
        if (value.getName().equals(key)) {
            if (db.containsKey(key)) {
                if (!Arrays.equals(db.get(key).getSchema(),value.getSchema()))
                    throw new RuntimeException("Inserting table has different schema from the previously declared table with the same type name");
            }
            return db.put(key, value);
        } else
            throw new RuntimeException("Error: value has not the schema name " + key + " (has "+value.getName()+" instead)");
    }
    
    public Table add(Table value) {
        return put(value.getName(),value);
    }

    @Override
    public Table remove(Object key) {
        return db.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Table> m) {
        db.putAll(m);
    }

    @Override
    public void clear() {
        db.clear();
    }

    @Override
    public Set<String> keySet() {
        return db.keySet();
    }

    @Override
    public Collection<Table> values() {
        return db.values();
    }

    @Override
    public Set<Entry<String, Table>> entrySet() {
        return db.entrySet();
    }
    
}
