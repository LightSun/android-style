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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
/**
 *  a util  similar to {@link ArrayList}  help to use other feature. like {@link LinkedList}  
 *  and have another helpful methods.such as: {@link #addAfter(Object, Object)} and {@link #addBefore(Object, Object)}
 * @author heaven7
 * @param <E>
 */
public class ArrayList2<E> extends ArrayList<E> {

	private static final long serialVersionUID = 894057356164202818L;
	
	@Override
	public boolean add(E e) {
		if(e  == null)
			throw new NullPointerException("in ArrayList2 or child of it Null element is not permit.");
		return super.add(e);
	}
    
	/** add e to the first .same as: {@link LinkedList#addFirst(Object)} */
	public void addFirst(E e){
		add(0, e);
	}
	/** add e to the last .same as: {@link LinkedList#addLast(Object)} */
	public void addLast(E e){
		add(size(), e);
	}
	/**
	 * add the element E after the element of index.
	 * @param index  must >=0
	 */
	public void addAfter(int index, E e) {
		if(index < 0)
			throw new IllegalArgumentException("index is invalid(must >=0). index = " +index);
		add(index + 1, e);
	}
	/**
	 * add an element after another element.
	 * @param referenced  the e to compare or refer.
	 * @param e          the e to add
	 * @throws NoSuchElementException if the referenced e can't find.
	 */
	public void addAfter(E referenced, E e) throws NoSuchElementException{
		int index = indexOf(referenced); //0 ~  size-1
		if(index == -1){
			throw new NoSuchElementException();
		}
		add(index + 1, e);
	}
	/**
	 * add the element E before the element of index.
	 * @param index must <= size() + 1
	 */
	public void addBefore(int index, E e){
		if(index > size() + 1)//max = size+1 
			throw new IllegalArgumentException("");
		add(index <=0 ? 0 : index-1 , e);
	}
	/**
	 * add an element before another element.
	 * @param referenced  the e to compare or refer.
	 * @param e          the e to add
	 * @throws NoSuchElementException if the referenced e can't find.
	 */
	public void addBefore(E referenced, E e){
		int index = indexOf(referenced);
		if(index == -1){
			throw new NoSuchElementException();
		}
		addBefore(index, e);
	}

	/** get and remove last element may be null if list is empty. */
	public E pollLast() {
		int size = size();
		return size !=0 ? remove(size -1) : null;
	}

	/** get and remove first element may be  null if list is empty. */
	public E pollFirst() {
		int size = size();
		return size !=0 ? remove(0) :null;
	}
	
	/** get first element but not remove from list, may be null if list is empty. */
	public E getFirst(){
		int size = size();
		return size !=0 ? get(0) :null;
	}
	/** get last element but not remove from list, may be null if list is empty. */
	public E getLast(){
		int size = size();
		return size !=0 ? get(size-1) :null;
	}
}
