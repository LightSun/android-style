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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
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
	/**
	 * an expand ArrayList2 . use {@link #setMode(Mode)} and {@link #setComparator(Comparator)} will be useful to control how to add new E
	 * and will effect add(E) , add(index,E)
	 * @author heaven7
	 *
	 * @param <E>
	 * @see Mode
	 * @see ExpandArrayList2#add(Object)
	 * @see ExpandArrayList2#add(int, Object)
	 */
	public static class ExpandArrayList2<E> extends ArrayList2<E>{
		private static final long serialVersionUID = -8201458809772301953L;
		
		private Mode mMode = Mode.Normal;
		private Comparator<E> mComparator;
		
		/***
		 * the mode of operate indicate how to effect {@link ExpandArrayList2#add(Object)} or {@link ExpandArrayList2#add(int, Object)}
		 * if {@link ExpandArrayList2#indexOf(Object)} != -1.
		 * @author heaven7
		 * @see ExpandArrayList2#add(Object)
		 * @see ExpandArrayList2#add(int, Object)
		 * @see Mode#Normal
		 * @see Mode#ClearPrevious
		 * @see Mode#ReplacePrevious
		 * @see Mode#ReplacePreviousAndClearAfter
		 */
		public  enum Mode{
			/** have no effect. same to {@link ArrayList2} */
			Normal (1), 
			/** use {@link ExpandArrayList2#indexOf(Object)} to seach previous ( as this behaviour depend on the Comparator 
			 * which can through {@link ExpandArrayList2#setComparator(Comparator)} to change it). 
			 * clear the previos E and put new E at the end or the target index.
			 * @see {@link ExpandArrayList2#add(int, Object)}
			 * @see {@linkplain ExpandArrayList2#indexOf(Object)}
			 * */
			ClearPrevious(2),
			/** update the previos E to new E
			 * @see #ClearPrevious*/
			ReplacePrevious(3),
			/** update the previos E to new E,and the All E after it will be cleared.
			 * this is similar to Activity.launchmode = top 
			 * @see #ClearPrevious*/
			ReplacePreviousAndClearAfter(4);
			
			final int value ;
			
			private Mode(int value){
				this.value = value;
			}
		}
		
		public Mode getMode() {
			return mMode;
		}
		/** set the mode which will effect the 'add(E)' and 'add(index,E)' operation.
		 * default is {@link Mode#Normal}
		 * @param mode  can't be null */
		public void setMode(Mode mode) {
			if(mode == null)
				throw new NullPointerException();
			this.mMode = mode;
		}
		public Comparator<E> getComparator() {
			return mComparator;
		}
		/** set the comparator , which will indicate how to find E. so effect {@link #indexOf(Object)} directly. 
		 * if {@link #getMode()} == {@link Mode#Normal},this have no effect.
		 * */
		public void setComparator(Comparator<E> c) {
			this.mComparator = c;
		}
		
		@Override
		public boolean add(E e) {
			if(e == null)
				throw new NullPointerException();
			
			switch (mMode) {
				
			case ClearPrevious:
				 int index = indexOf(e);
				 if(index != -1){
					 remove(index);
				 }
				 return super.add(e);
				
			case ReplacePrevious:
				int index2 = indexOf(e);
				 if(index2 != -1){
					 set(index2, e);
					 return true;
				 }
				 return super.add(e);
				 
			case ReplacePreviousAndClearAfter:
				int index3 = indexOf(e);
				if(index3 != -1){
					 set(index3, e);
					 clearAfter(index3);
					 return true;
				 }
				return super.add(e);
				
			case Normal:
			default:
				return super.add(e);
			}
		}
		
		@Override //index to add
		public void add(int index, E e) {
			if(e == null)
				throw new NullPointerException();
			
			switch (mMode) {
			
			case ClearPrevious:
				 int oldIndex = indexOf(e);
				 super.add(index,e);
				 if(oldIndex == -1){
					 break;
				 }
				//" oldIndex >= index " indicate the delete index after(>=) the  added index
				 final boolean after = oldIndex >= index;
				 remove(after ? oldIndex + 1 : oldIndex);
				 break;
				
			case ReplacePrevious:
				int index2 = indexOf(e);
				 if(index2 != -1){
					 set(index2, e);
					 break;
				 }
				 super.add(index,e);
				 break;
				 
			case ReplacePreviousAndClearAfter:
				int index3 = indexOf(e);
				if(index3 != -1){
					 set(index3, e);
					 clearAfter(index3);
					 break;
				 }
				 super.add(index,e);
				 break;
				
			case Normal:
			default:
				super.add(index,e);
			}
		}
		
		/** clear all element whose index after index */
		private void clearAfter(int index) {
			index += 1;
			while( index < size() ){
				remove(index);
			}
		}
		@SuppressWarnings("unchecked")
		@Override
		public int indexOf(Object object) {
			if(object == null)
				return -1;
			E target = null;
			try{
				target = (E) object;
			}catch(ClassCastException e){
				return -1;
			}
			final Comparator<E> comparator = this.mComparator;
			
			final int size = size();
			int index = - 1;
			for(int i = 0 ; i<size ; i++){
				E e = get(i);
				if(comparator == null){
					if( e.equals(object)){
						index = i;
						break;
					}
				}else if(comparator.compare(e, target) == 0){
					index = i;
					break;
				}
			}
			return index;
		}
		
		private void writeObject(ObjectOutputStream stream) throws IOException {
		        stream.defaultWriteObject();
		        stream.writeInt(mMode.value);
		        
		        final int size = size();
		        stream.writeInt(size);
		        for (int i = 0; i < size; i++) {
		            stream.writeObject(get(i));
		        }
		    }

	    @SuppressWarnings("unchecked")
		private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
	        stream.defaultReadObject();
	        int modeVlue = stream.readInt();
	        this.mMode = convertToMode(modeVlue);
	        
	        final int size = size();
	        int cap = stream.readInt();
	        if (cap < size) {
	            throw new InvalidObjectException(
	                    "Capacity: " + cap + " < size: " + size);
	        }
	        clear();
	        trimToSize();
	       // array = (cap == 0 ? EmptyArray.OBJECT : new Object[cap]);
	        for (int i = 0; i < size; i++) {
	            //array[i] = stream.readObject();
	        	add((E)stream.readObject());
	        }
	    }
	    public static Mode convertToMode(int modeVlue) {
	    	switch (modeVlue) {
			case 1:
				return Mode.Normal;
				
			case 2:
				return Mode.ClearPrevious;
				
			case 3:
				return Mode.ReplacePrevious;
            
			case 4:
				return Mode.ReplacePreviousAndClearAfter;
					
			default:
				throw new IllegalStateException("wrong mode = "+ modeVlue);
			}
		}
	}

}
