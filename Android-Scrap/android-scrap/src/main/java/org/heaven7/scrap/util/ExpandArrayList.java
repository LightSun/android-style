package org.heaven7.scrap.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;

/**
 * an expand ArrayList2 . use {@link #setMode(Mode)} and {@link #setComparator(Comparator)} will be useful to control how to add new E
 * and will effect add(E) , add(index,E)
 *
 * @param <E>
 * @author heaven7
 * @see Mode
 * @see ExpandArrayList#add(Object)
 * @see ExpandArrayList#add(int, Object)
 */
public class ExpandArrayList<E> extends ArrayList2<E> {
    private static final long serialVersionUID = -8201458809772301953L;

    private Mode mMode = Mode.Normal;
    private Comparator<E> mComparator;

    /***
     * the mode of operate indicate how to effect {@link ExpandArrayList#add(Object)} or {@link ExpandArrayList#add(int, Object)}
     * if {@link ExpandArrayList#indexOf(Object)} != -1.
     * @author heaven7
     * @see ExpandArrayList#add(Object)
     * @see ExpandArrayList#add(int, Object)
     * @see Mode#Normal
     * @see Mode#ClearPrevious
     * @see Mode#ReplacePrevious
     * @see Mode#ReplacePreviousAndClearAfter
     */
    public enum Mode {
        /**
         * have no effect. same to {@link ArrayList2}
         */
        Normal(1),
        /**
         * use {@link ExpandArrayList#indexOf(Object)} to search previous ( as this behaviour depend on the Comparator
         * which can through {@link ExpandArrayList#setComparator(Comparator)} to change it).
         * clear the previous E and put new E at the end or the target index.
         *
         * @see {@link ExpandArrayList#add(int, Object)}
         * @see {@linkplain ExpandArrayList#indexOf(Object)}
         */
        ClearPrevious(2),
        /**
         * update the previous E to new E
         *
         * @see #ClearPrevious
         */
        ReplacePrevious(3),
        /**
         * update the previous E to new E,and the All E after it will be cleared.
         * this is similar to Activity.launch-mode = top
         *
         * @see #ClearPrevious
         */
        ReplacePreviousAndClearAfter(4);

        final int value;

        private Mode(int value) {
            this.value = value;
        }
    }

    public Mode getMode() {
        return mMode;
    }

    /**
     * set the mode which will effect the 'add(E)' and 'add(index,E)' operation.
     * default is {@link Mode#Normal}
     *
     * @param mode can't be null
     */
    public void setMode(Mode mode) {
        if (mode == null)
            throw new NullPointerException();
        this.mMode = mode;
    }

    public Comparator<E> getComparator() {
        return mComparator;
    }

    /**
     * set the comparator , which will indicate how to find E. so effect {@link #indexOf(Object)} directly.
     * if {@link #getMode()} == {@link Mode#Normal},this have no effect.
     */
    public void setComparator(Comparator<E> c) {
        this.mComparator = c;
    }

    @Override
    public boolean add(E e) {
        if (e == null)
            throw new NullPointerException();

        switch (mMode) {
            case ClearPrevious:
                int index = indexOf(e);
                if (index != -1) {
                    remove(index);
                }
                return super.add(e);

            case ReplacePrevious:
                int index2 = indexOf(e);
                if (index2 != -1) {
                    set(index2, e);
                    return true;
                }
                return super.add(e);

            case ReplacePreviousAndClearAfter:
                int index3 = indexOf(e);
                if (index3 != -1) {
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
        if (e == null)
            throw new NullPointerException();

        switch (mMode) {

            case ClearPrevious:
                int oldIndex = indexOf(e);
                super.add(index, e);
                if (oldIndex == -1) {
                    break;
                }
                //" oldIndex >= index " indicate the delete index after(>=) the  added index
                final boolean after = oldIndex >= index;
                remove(after ? oldIndex + 1 : oldIndex);
                break;

            case ReplacePrevious:
                int index2 = indexOf(e);
                if (index2 != -1) {
                    set(index2, e);
                    break;
                }
                super.add(index, e);
                break;

            case ReplacePreviousAndClearAfter:
                int index3 = indexOf(e);
                if (index3 != -1) {
                    set(index3, e);
                    clearAfter(index3);
                    break;
                }
                super.add(index, e);
                break;

            case Normal:
            default:
                super.add(index, e);
        }
    }

    /**
     * clear all element whose index after index
     */
    private void clearAfter(int index) {
        removeRange(index + 1, size()- 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int indexOf(Object object) {
        if (object == null)
            return -1;
        E target = null;
        try {
            target = (E) object;
        } catch (ClassCastException e) {
            return -1;
        }
        final Comparator<E> comparator = this.mComparator;

        final int size = size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (comparator == null) {
                if (e.equals(object)) {
                    index = i;
                    break;
                }
            } else if (comparator.compare(e, target) == 0) {
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
            add((E) stream.readObject());
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
                throw new IllegalStateException("wrong mode = " + modeVlue);
        }
    }
}