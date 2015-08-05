/*
 * Copyright (C) 2015 
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.heaven7.scrap.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * reflect util
 * @author heaven7
 */
public class Reflector{
	
	
	public static IReflector from(Class<?> clazz){
		return new SimpleReflector(clazz);
	}
	
	public interface IReflector {
		
		/**equal to Class.newInstance() */
		public abstract <T>T newInstance();

		public abstract  IReflector constructor(Class<?>... paramTypes);

		/** create an  Instance of T, you must call the method {@linkplain #constructor} first*/
		public abstract  <T> T create(Object... args);

		/** find the field */
		public abstract IReflector field(String name);

		/** find the method */
		public abstract IReflector method(String name, Class<?>... paramTypes);

		/** set  static field value */
		public abstract IReflector setStatic(Object value);

		/** set  static field value */
		public abstract IReflector set(Object target, Object value);

		/** get field value*/
		public abstract <T> T get(Object target);

		/** get static field value*/
		public abstract <T> T getStatic();

		/** invoke static method */
		public abstract <T> T invokeStatic(Object... args);

		/** invoke method */
		public abstract <T> T invoke(Object target, Object... args);
	}
	
    /*public static void getUniqueName(StringBuilder sb,Object...objs){
    	final Class<?> clazz = objs.getClass();
    	sb.setLength(0);
    	sb.append(clazz.getName());
    	
    	Class<?> clazz2  = clazz;
    	while(clazz2.isArray()){
    		clazz2 = clazz2.getComponentType();
    		sb.append(clazz2.getName());
    	}
    }*/

	public static Class<?> toArrayType(Class<?> clazz) {
		return Array.newInstance(clazz, 0).getClass();
	}
	@SuppressWarnings("unchecked")
	public static<T>T[] newArray(Class<T> clazz,int len) {
		return (T[]) Array.newInstance(clazz, len);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> constructor(Class<?> clazz,Class<?>...paramTypes){
		try {
			Constructor<?> c = clazz.getDeclaredConstructor(paramTypes);
			c.setAccessible(true);
			return (Constructor<T>) c;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	public static Method method(Class<?> clazz,String name,Class<?>...paramTypes){
		try {
			Method m = clazz.getDeclaredMethod(name, paramTypes);
			m.setAccessible(true);
			return m;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Field field(Class<?> clazz,String name){
		try {
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static class SimpleReflector implements IReflector{
		private final Class<?> clazz;
		private Method m;
		private Field f;
		private Constructor<?> c;
		
		public SimpleReflector(Class<?> clazz) {
			this.clazz = clazz;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <T>T create(Object...args){
			try {
				return (T) c.newInstance(args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public IReflector constructor(Class<?>...paramTypes){
			this.c = Reflector.constructor(clazz,paramTypes);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#field(java.lang.String)
		 */
		@Override
		public  IReflector field(String name){
			this.f = Reflector.field(clazz, name);
			return this;
		}
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#method(java.lang.String, java.lang.Class)
		 */
		@Override
		public IReflector method(String name,Class<?>...paramTypes){
			this.m = Reflector.method(clazz, name, paramTypes);
			return this;
		}
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#setStatic(java.lang.Object)
		 */
		@Override
		public IReflector setStatic(Object value){
			return set(null, value);
		}
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#set(java.lang.Object, java.lang.Object)
		 */
		@Override
		public IReflector set(Object target,Object value){
			try {
				f.set(target, value);
				return this;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#get(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <T> T get(Object target){
			try {
				return (T) f.get(target);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#getStatic()
		 */
		@Override
		public <T> T getStatic(){
			return get(null);
		}
		
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#invokeStatic(java.lang.Object)
		 */
		@Override
		public <T> T invokeStatic(Object...args){
			return invoke(null, args);
		}
		/* (non-Javadoc)
		 * @see com.heaven.classloadertest.IReflector#invoke(java.lang.Object, java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <T> T invoke(Object target,Object...args){
			try {
				return (T) m.invoke(target, args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T newInstance() {
			try {
				return (T) clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
