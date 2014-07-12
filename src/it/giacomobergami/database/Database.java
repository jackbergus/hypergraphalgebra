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
import it.giacomobergami.types.Type;
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
public class Database  {

    private Map<String,Table> db;
    
    public Database() {
        db = new HashMap<>();
    }
    
    public Set<String> getTableNames() {
        return db.keySet();
    }
    
    public Database(Map<String,Table> database) {
        db = database;
    }
    
    public int size() {
        return db.size();
    }

    public boolean isEmpty() {
        return db.isEmpty();
    }

    public boolean containsTableWithName(String key) {
        return db.containsKey(key);
    }

    public boolean containsTable(Table value) {
        return db.containsValue(value);
    }

    public Table getTable(String key) {
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
    
    public Set<BigInteger> getAllRowsKeys() {
        Set<BigInteger> toret = new HashSet<>();
        for (String key : db.keySet()) {
            toret.addAll(db.get(key).getAllKeys());
        }
        return toret;
    }

    public Table update(String key, Table value) {
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
        return update(value.getName(),value);
    }
    
    public Table createTable(String name, BigInteger startIndex, Type ... clazzes) {
        add(new Table(name, startIndex, clazzes));
        return getTable(name);
    }
    
    public Table createTable(String name,  Type ... clazzes) {
        add(new Table(name, clazzes));
        return getTable(name);
    }

    public Table remove(String key) {
        return db.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Table> m) {
        db.putAll(m);
    }


    public void clear() {
        db.clear();
    }


    
}
