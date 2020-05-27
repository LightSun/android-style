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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * reflect util
 * @author heaven7
 */
public final class Reflector{
	
	
	public static IReflector from(Class<?> clazz){
		return new SimpleReflector(clazz);
	}
	
	public interface IReflector {
		
		/**equal to Class.newInstance() */
		<T>T newInstance();

		IReflector constructor(Class<?>... paramTypes);

		/** create an  Instance of T, you must call the method {@linkplain #constructor} first*/
		<T> T create(Object... args);

		/** find the field */
		IReflector field(String name);

		/** find the public method */
		IReflector method(String name, Class<?>... paramTypes);

		/** find the public methods */
		IReflector methods(String name, boolean staticType);

		/** set  static field value */
		IReflector setStatic(Object value);

		/** set  static field value */
		IReflector set(Object target, Object value);

		/** get field value*/
		<T> T get(Object target);

		/** get static field value*/
		<T> T getStatic();

		/** invoke static method */
		<T> T invokeStatic(Object... args);

		/** invoke static method . see {@linkplain #methods(String, boolean)}*/
		<T> T invokeStaticUntil(Object... args);

		/** invoke method */
		<T> T invoke(Object target, Object... args);

		/** invoke method */
		<T> T invokeUntil(Object target, Object... args);
	}
	
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
			Constructor<?> c = clazz.getConstructor(paramTypes);
			c.setAccessible(true);
			return (Constructor<T>) c;
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	public static Method method(Class<?> clazz,String name,Class<?>...paramTypes){
		try {
			Method m = clazz.getMethod(name, paramTypes);
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
		private List<Method> mMethods;

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

		@Override
		public IReflector methods(String name, boolean staticType) {
			Method[] methods = clazz.getMethods();
			List<Method> list = new ArrayList<>();
			boolean tmpStatic;
			for (Method m : methods){
				tmpStatic = (m.getModifiers() & Modifier.STATIC)== Modifier.STATIC;
				if(staticType == tmpStatic){
					if(m.getName().equals(name)){
						list.add(m);
					}
				}
			}
			if(list.isEmpty()){
				throw new IllegalStateException("can't find method for name = " + name + " ,static = " + staticType);
			}
			mMethods = list;
			return this;
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
		public <T> T invokeStaticUntil(Object... args) {
			return invokeUntil(null, args);
		}

		@Override  @SuppressWarnings("unchecked")
		public <T> T invokeUntil(Object target, Object... args) {
			for (Method m : mMethods){
				try {
					return (T) m.invoke(target, args);
				} catch (Exception e) {
					//ignore
				}
			}
			throw new IllegalStateException("invoke static method error.");
		}

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
