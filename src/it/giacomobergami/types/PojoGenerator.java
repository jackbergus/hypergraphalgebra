/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.giacomobergami.types;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/** http://blog.javaforge.net/post/31913732423/howto-create-java-pojo-at-runtime-with-javassist */
public class PojoGenerator {
    
        private static Map<Type,Class> mc = new HashMap<>();

        public static Type generateMonoPojo(String name, Class<?> clazz) {
            Type t = new Type(name, clazz);
            if (mc.containsKey(t))
                 return t;
            Map<String, Class<?>> props = new HashMap<>();
	    props.put(name, clazz);
            try {
                mc.put(t,PojoGenerator.generate(name, props));
                return t;
            } catch (    NotFoundException | CannotCompileException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        
        /*public static Type generateMonoPojo(String name, Class cl) {
            Type t = new Type(name, cl);
            if (mc.containsKey(t))
                 return t;
             else {
                 Class cls = generateMonoPojo(t.getName(), t.getJavaClass());
                 mc.put(t,cls);
                 return t;
             }
        }*/
        
         /*public static Class generateMonoPojo(Type t) {
             if (mc.containsKey(t))
                 return mc.get(t);
             else {
                 Class cls = generateMonoPojo(t.getName(), t.getJavaClass());
                 mc.put(t,cls);
                 return cls;
             }
         }*/
         
         public static Object getNewPojoInstance(Class clazz, Object value) {
             if (!mc.containsValue(clazz))
                 return null;
            Object obj = PojoGenerator.getPojoInstance(clazz);
            PojoGenerator.setPojoValue(obj, value);
            return obj;
         }
         
         public static Object getNewPojoInstance(Type clazz, Object value) {
             if (!mc.containsKey(clazz))
                 return null;
            Object obj = PojoGenerator.getPojoInstance(mc.get(clazz));
            PojoGenerator.setPojoValue(obj, (Object)value);
            return obj;
         }
     
	private static Class generate(String className, Map<String, Class<?>>  properties) throws NotFoundException,
			CannotCompileException {

            
                className = "net.javaforge.blog.javassist.Pojo$"+className;
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass(className);

		// add this to define a super class to extend
		// cc.setSuperclass(resolveCtClass(MySuperClass.class));

		// add this to define an interface to implement
		//cc.addInterface(resolveCtClass(Serializable.class));

		for (Entry<String, Class<?>> entry : properties.entrySet()) {

			cc.addField(new CtField(resolveCtClass(entry.getValue()), entry.getKey(), cc));

			// add getter
			cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue()));
                        cc.addMethod(CtMethod.make("public Class get"+entry.getKey()+"Class() { return "+entry.getValue().getName()+".class; }", cc));

			// add setter
			cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue()));
		}

		return cc.toClass();
	}
        
        public static Class getPojoClass(Object pojo) {
            Class clazz = pojo.getClass();
                try {
                    return  (Class)clazz.getMethod("get"+clazz.getSimpleName().split("\\$")[1]+"Class").invoke(pojo);
                } catch (   NoSuchMethodException | SecurityException |     IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(PojoGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
        }
        
        public static Class getPojoClass(Type t) {
            return mc.get(t);
        }
        
        public static void setPojoValue(Object pojo, Object value) {
            try {
                Class clazz = pojo.getClass();
                Class toSet = getPojoClass(pojo);
                try {
                    clazz.getMethod("set"+clazz.getSimpleName().split("\\$")[1],toSet).invoke(pojo, value);
                } catch (        NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(PojoGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (    IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(PojoGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public static Object getPojoValue(Object pojo) {
            Class clazz = pojo.getClass();
            try {
                    return clazz.getMethod("get"+clazz.getSimpleName().split("\\$")[1]).invoke(pojo);
            } catch (   NoSuchMethodException | SecurityException |  IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(PojoGenerator.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        

	private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String getterName = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public ").append(fieldClass.getName()).append(" ")
				.append(getterName).append("(){").append("return this.")
				.append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public void ").append(setterName).append("(")
				.append(fieldClass.getName()).append(" ").append(fieldName)
				.append(")").append("{").append("this.").append(fieldName)
				.append("=").append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		return pool.get(clazz.getName());
	}

    public static Object getPojoInstance(Class<?> screenname) {
            try {
                return screenname.newInstance();
            } catch (    InstantiationException | IllegalAccessException ex) {
                return null;
            }
    
    }
}
