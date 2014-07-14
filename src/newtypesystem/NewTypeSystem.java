/*
 * NewTypeSystem.java
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



package newtypesystem;

import it.giacomobergami.database.Database;
import it.giacomobergami.dhimp.DHImp;
import it.giacomobergami.dhimp.HypergraphOperations;
import it.giacomobergami.types.PojoGenerator;
import it.giacomobergami.utils.Dovetailing;
import it.giacomobergami.relational.Table;
import it.giacomobergami.functional.Tuple;
import it.giacomobergami.relational.ICalc;
import it.giacomobergami.relational.AbstractJoinProperty;
import it.giacomobergami.relational.IMapFunction;
import it.giacomobergami.tensor.BinaryRelationsTensor;
import it.giacomobergami.tensor.GuavaBinaryTensorLayer;
import it.giacomobergami.tensor.HyperEdge;
import it.giacomobergami.tensor.IHedgeProp;
import it.giacomobergami.types.Type;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 *
 * @author gyankos
 */
public class NewTypeSystem {

    private static void elems() {
                /*BigInteger i = Dovetailing.index(2399);
        BigInteger j = Dovetailing.index(6680);
        BigInteger dt = Dovetailing.dt(i, j);
        System.out.println(dt);
        BigInteger retdt[] = Dovetailing.dtInv(dt);
        System.out.println(retdt[0]+" "+retdt[1]);
        
        BigInteger k = Dovetailing.index(8);
        BigInteger h = Dovetailing.index(12);
        LinkedList<BigInteger> libi = new LinkedList<>();
        libi.add(i);
        libi.add(j);
        libi.add(k);
        libi.add(h);
        BigInteger vdt = Dovetailing.dtVec(libi);
        List<BigInteger> rlbi = Dovetailing.dtVecInv(vdt);
        for (BigInteger x: rlbi) {
            System.out.println(x);
        }*/
        
        /********************
        Table one = new Table("t1",Dovetailing.index(1),String.class, Integer.class,Double.class);
        one.addRow(0.1, "A",2,4.0);
        one.addRow(0.2, "B",8,10.0);
        one.addRow(0.3, "C",15,20.0);
        one.addRow(0.4, "A",28,3.0);
        one.addRow(0.5, "C",1,3.0);
        
        Table two = new Table("t1",Dovetailing.index(6),String.class, Integer.class,Double.class);
        two.addRow(0.2, "A",7,11.0);
        two.addRow(0.1,"A",2,4.0);
        
        Database db1 = new Database();
        db1.add(one);
        Database db2 = new Database();
        db2.add(two);
        
        BinaryRelationsTensor tensor1 = new BinaryRelationsTensor();
        tensor1.set(1, 1, "A", 0.1);
        tensor1.set(1, 3, "A", 0.3);
        tensor1.set(2, 2, "A", 0.2);
        tensor1.set(2, 4, "A", 0.5);
        tensor1.set(2, 5, "A", 0.1);
        tensor1.set(3, 3, "A", 0.5);
        tensor1.set(3, 4, "A", 0.6);
        tensor1.set(4, 1, "A", 0.1);
        tensor1.set(4, 4, "A", 0.2);
        tensor1.set(5, 2, "A", 0.3);
        tensor1.set(5, 5, "A", 0.5);
        
        BinaryRelationsTensor tensor2 = new BinaryRelationsTensor();
        tensor2.set(6,7,"A",1);
        tensor2.set(7,6,"A",1);
        
        DHImp<GuavaBinaryTensorLayer> dh1 = new DHImp<>(db1,tensor1);
        DHImp<GuavaBinaryTensorLayer> dh2 = new DHImp<>(db2,tensor2);
        HypergraphOperations<GuavaBinaryTensorLayer> go = new HypergraphOperations<>();
        
        
        
        DHImp<GuavaBinaryTensorLayer> result;
        ********************/
        
        
        //result = go.HProject(dh1, String.class);
        
        /*result = go.HSelect(dh, new IProperty() {
            @Override
            public boolean prop(Tuple tup) {
                return tup.get(0).equals("A");
            }
        }, new IHedgeProp() {
            @Override
            public boolean prop(HyperEdge e) {
                return true;
            }
        });*/
        /* result = go.HJoin(dh1, new IJoinProperty() {
            @Override
            public boolean property(Tuple left, Tuple right) {
                return (left.get(0).equals(right.get(0)));
            }
        }, dh2);*/
       /* LinkedList<DHImp<GuavaBinaryTensorLayer>> l = new LinkedList<>();
        l.add(dh1);
        l.add(dh2);
        result =  go.HUnion(l);*/
        
        
        /********************
        result = go.HLeftTiedJoin(dh1, new IJoinProperty() {
            @Override
            public boolean property(Tuple left, Tuple right) {
                return (left.get(0).equals(right.get(0)));
            }
        }, dh2);
        
        for (com.google.common.collect.Table.Cell<BigInteger, BigInteger, Double> cell:result.getT().get("A").getValueRange()) {
            System.out.println(cell);
        }
        for (Tuple x:result.getDB().getTable("t1|><|t1")) {
            System.out.println(x);
        }
        ********************/
        
        /*Table proj = TableOperations.projectAndGroupBy(one, Integer.class, new IMapFunction<Integer>() {
            @Override
            public Integer collectOf(Collection<Integer> collection) {
                int sum = 0;
                for (Integer x : collection)
                    sum += x;
                return sum;
            }
        }, String.class);*/
        
        /*Table one = new Table("t1",Dovetailing.index(1),Integer.class, Integer.class);
        one.addRow(1, 1,2);
        one.addRow(1, 1,4);
        Table two = new Table("t2",Dovetailing.index(3),Integer.class, Integer.class);
        two.addRow(1, 2,3);
        two.addRow(0.8, 2,6);
        two.addRow(0.8, 2,1);*/
        /*LinkedList<Table> tomerge = new LinkedList<>();
        tomerge.add(one);
        tomerge.add(two);
        Table proj = TableOperations.union(tomerge);*/
        /*Table proj = TableOperations.join(one, new IJoinProperty() {
            @Override
            public boolean property(Tuple left, Tuple right) {
                return (left.get(1).equals(right.get(0)));
            }
        }, two);*/
        /*for (Tuple x : proj) {
            System.out.println(x);
        }*/
    }

    public static void generateTIKZ(DHImp<GuavaBinaryTensorLayer> db, String layer, String filename) {
        String head = "\\documentclass[tikz]{standalone}\n" +
            "\\usepackage{tikz}\n" +
            "\\usepackage{pgf}\n" +
            //"\\usetikzlibrary{matrix,node-families,positioning-plus,paths.ortho}\n" +
            "\\usetikzlibrary{calc,positioning,shapes.geometric}\n" +
            "\\usetikzlibrary{external}\n" +
            "\\usetikzlibrary{positioning}\n" +
            "\\usetikzlibrary{circuits}\n" +
            "\\usetikzlibrary{arrows,shapes,snakes,automata,backgrounds,petri}\n" +
            "\n" +
            "\\usetikzlibrary{arrows}\n" +
            "\\begin{document}\n" +
            "\\begin{tikzpicture}[->,>=stealth',shorten >=1pt,auto,node distance=3cm,\n" +
            "  thick,main node/.style={circle,draw=blue,opacity=20,draw,font=\\sffamily\\Large\\bfseries},\n]";
        GuavaBinaryTensorLayer g = db.getT().get(layer);
        head += "\\def \\n {"+ Integer.toString(g.nEntities()) + "}\n\\def \\radius {3cm}\n" +
                "\\def \\margin {8} % margin in angles, depends on the radius\n" +
                 "\n";
        int k = 1;
        HashMap<BigInteger,Integer> hm = new HashMap<>();
        for (BigInteger bi : g.Entities()) {
            head += "  \\node[draw,rectangle] at ({360/\\n * ("+k+" - 1)}:\\radius) ("+k+") {\\texttt{"+db.getDB().getTuple(bi).toString2()+"}};\n";
            hm.put(bi,k++);
        }
        
        for (com.google.common.collect.Table.Cell<BigInteger, BigInteger, Double> cell:g.getValueRange()) {
            BigInteger before = cell.getRowKey();
            BigInteger after = cell.getColumnKey();
            double val = cell.getValue();
            String perc = new DecimalFormat("#").format(val*100).replace(",", ".");
            String perc2 = new DecimalFormat("#.##").format(val).replace(",", ".");
            int i = hm.get(before);
            int j = hm.get(after);
            String pos ="";
            String bend = "left";
            if (i==j) {
                pos = "loop left";
            }
            
            
            head += "\\path[every node/.style={font=\\sffamily\\small}] ("+i+") edge  ["+pos+",draw=blue!"+perc+"!red,color=blue!"+perc+"!red] node["+bend+"] {"+perc2+"} ("+j+");\n";
            
        }
    
        head += "\\end{tikzpicture}\n\\end{document}";
    
        		File file = new File(filename);
 
		try (FileOutputStream fop = new FileOutputStream(file)) {
 
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = head.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * @param args the command line arguments
     */

    /**
     *
     * @param args the command line arguments
     * @throws javassist.NotFoundException
     * @throws javassist.CannotCompileException
     */
    public static void main(String[] args) throws NotFoundException, CannotCompileException {
        Type t_screenname = PojoGenerator.generateMonoPojo("ScreenName", String.class);
        Type t_userid = PojoGenerator.generateMonoPojo("UserID", String.class);
        Type t_postid = PojoGenerator.generateMonoPojo("PostID", String.class);
        final Type t_count = PojoGenerator.generateMonoPojo("Count", Integer.class);
        Type t_neighscreenname = PojoGenerator.generateMonoPojo("NeighScreenName", String.class);
        Type t_neighuserid = PojoGenerator.generateMonoPojo("NeighUserID", String.class);
        
        /*Class<?> userid = PojoGenerator.generateMonoPojo(t_userid);
        Object j = PojoGenerator.getNewPojoInstance(t_userid, (Object)"123");
        System.out.println(PojoGenerator.getPojoValue(j));*/
        
        DHImp<GuavaBinaryTensorLayer> osn = DHImp.newGuavaBinaryTensorDHImp();
        osn.createTable("users", BigInteger.ONE, t_userid, t_screenname);
        BigInteger gyankos = osn.addPojoRow("users", 1.0, "gyankos","Jack Bergus");
        BigInteger jsb =     osn.addPojoRow("users", 1.0, "jsbach", "Johann Sebastian Bach");
        BigInteger gfh =     osn.addPojoRow("users", 1.0, "handel", "G F Haendel");
        BigInteger wam =     osn.addPojoRow("users", 1.0, "mozart", "W A Mozart");
        BigInteger pdq = osn.addPojoRow("users", 1.0, "faux","P.D.Q. Bach");
        osn.setRelation(jsb, gfh, "FriendOf", 1.0);
        osn.setRelation(wam, gfh, "FriendOf", 1.0);
        osn.setRelation(jsb, pdq, "FriendOf", 1.0);
        osn.setRelation(pdq, jsb, "FriendOf", 1.0);
        osn.setRelation(gyankos, pdq, "FriendOf", 1.0);
        System.out.println(osn.getDB().getTable("users").size());
        
        HypergraphOperations<GuavaBinaryTensorLayer> op = new HypergraphOperations<>();
        int phase = 0;
        
        //renaming the tabes
        DHImp<GuavaBinaryTensorLayer> renamed = op.HRename(osn, new Type[]{t_screenname,t_userid}, new Type[]{t_neighscreenname,t_neighuserid});
        System.out.println("~~~~~~~~~~~~~~~~~osn~~~~~~~~~~~~~");
        System.out.println(osn);
        System.out.println(osn.getT().get("FriendOf"));
        generateTIKZ(osn, "FriendOf", phase+".tex"); phase++;
        
        //adding the user count
        renamed = op.HCalc(renamed, t_count, new ICalc() {
            @Override
            public Object calcResult(Tuple t) {
                return t_count.create(new Integer(1));
            }
        });
        System.out.println("~~~~~~~~~~~~~~~~~calced~~~~~~~~~~~~~");
        System.out.println(renamed);
        System.out.println(renamed.getT().get("FriendOf"));
        generateTIKZ(renamed, "FriendOf", phase+".tex"); phase++;
        
        DHImp<GuavaBinaryTensorLayer> joined = op.HJoin(osn, new AbstractJoinProperty(osn, renamed) {
            @Override
            public boolean property(Tuple left, Tuple right, DHImp dhleft, DHImp dhright) {
                if (!dhleft.getT().containsKey("FriendOf"))
                    return false;
                return ((dhleft.getT().get("FriendOf").get(left.getIndex(), right.getIndex())!=0)
                        ||(left.getIndex().equals(right.getIndex())));
            }
        }, renamed);
        System.out.println("~~~~~~~~~~~~~~~~~joined~~~~~~~~~~~~~");
        System.out.println(joined);
        System.out.println(joined.getT().get("FriendOf"));
        generateTIKZ(joined, "FriendOf", phase+".tex"); phase++;
        joined = op.HSelect(joined, null, new IHedgeProp() {
            @Override
            public boolean prop(HyperEdge e) {
                return (e.getWeight()>=0.5);
            }
        });
        System.out.println(joined.getT().get("FriendOf"));
        generateTIKZ(joined, "FriendOf", phase+".tex"); phase++;
        //System.out.println(joined);
        
        joined = op.HGroupBy(joined, t_count, new IMapFunction() {
            @Override
            public Object collectOf(Collection collection) {
                return t_count.create(collection.size()-1);
            }
        }, t_screenname, t_userid);
        System.out.println("~~~~~~~~~~~~~~~~~groupby~~~~~~~~~~~~~");
        System.out.println(joined);
        System.out.println(joined.getT().get("FriendOf"));
        generateTIKZ(joined, "FriendOf", phase+".tex"); phase++;
        joined = op.HSelect(joined, null, new IHedgeProp() {
            @Override
            public boolean prop(HyperEdge e) {
                return (e.getWeight()>=0.25);
            }
        });
        System.out.println(joined.getT().get("FriendOf"));
        generateTIKZ(joined, "FriendOf", phase+".tex"); phase++;
        
        
        //Table posts = new Table("posts", BigInteger.ONE, t_postid, t_userid);
        
	/*Class<?> screenname = PojoGenerator.generateMonoPojo(t_screenname);
        Class<?> userid = PojoGenerator.generateMonoPojo(t_userid);
        Class<?> postid = PojoGenerator.generateMonoPojo(t_postid);
        Class<?> count = PojoGenerator.generateMonoPojo(t_count);
	Class<?> neighscreenname = PojoGenerator.generateMonoPojo(t_neighscreenname);
        Class<?> neighuserid = PojoGenerator.generateMonoPojo(t_neighuserid);*/
        
        
        
    }
    
}
