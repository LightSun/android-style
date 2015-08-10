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
import java.util.Collection;
//comes from android src
/**
 * Copy on write array. This array is not thread safe, and only one loop can
 * iterate over this array at any given time. This class avoids allocations
 * until a concurrent modification happens.
 *
 * Usage:
 *
 * CopyOnWriteArray.Access<MyData> access = array.start();
 * try {
 *     for (int i = 0; i < access.size(); i++) {
 *         MyData d = access.get(i);
 *     }
 * } finally {
 *     access.end();
 * }
 */
public class CopyOnWriteArray<T> {
        private ArrayList<T> mData = new ArrayList<T>();
        private ArrayList<T> mDataCopy;

        private final Access<T> mAccess = new Access<T>();

        private boolean mStart;

        public static class Access<T> {
            private ArrayList<T> mData;
            private int mSize;

            public T get(int index) {
                return mData.get(index);
            }
            public int size() {
                return mSize;
            }
        }

        public CopyOnWriteArray() {
        }

        private ArrayList<T> getArray() {
            if (mStart) {
                if (mDataCopy == null) mDataCopy = new ArrayList<T>(mData);
                return mDataCopy;
            }
            return mData;
        }

         public  Access<T> start() {
            if (mStart) throw new IllegalStateException("Iteration already started");
            mStart = true;
            mDataCopy = null;
            mAccess.mData = mData;
            mAccess.mSize = mData.size();
            return mAccess;
        }

         public void end() {
            if (!mStart) throw new IllegalStateException("Iteration not started");
            mStart = false;
            if (mDataCopy != null) {
                mData = mDataCopy;
                mAccess.mData.clear();
                mAccess.mSize = 0;
            }
            mDataCopy = null;
        }

       public int size() {
            return getArray().size();
        }

       public void add(T item) {
            getArray().add(item);
        }

       public void addAll(CopyOnWriteArray<T> array) {
            getArray().addAll(array.mData);
        }
       public void addAll(Collection<T> collection) {
            getArray().addAll(collection);
       }
       public void addAll(T...ts) {
            for(T t : ts){
                getArray().add(t);
            }
       }

       public void remove(T item) {
            getArray().remove(item);
        }

       public void clear() {
            getArray().clear();
        }

       public boolean contains(Object t){
           return getArray().contains(t);
       }
    }
