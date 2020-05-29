package org.heaven7.scrap.core.oneac;

import org.heaven7.scrap.util.ArrayList2;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;

/**
 * an expand ArrayList2 . use {@link #setStackMode(StackMode)} and {@link #setComparator(Comparator)} will be useful to control how to add new E
 * and will effect add(E) , add(index,E)
 *
 * @param <E>
 * @author heaven7
 * @see StackMode
 * @see ExpandArrayList#add(Object)
 * @see ExpandArrayList#add(int, Object)
 */
/*public*/ class ExpandArrayList<E> extends ArrayList2<E> {
    private static final long serialVersionUID = -8201458809772301953L;

    private StackMode mMode = StackMode.Normal;
    private Comparator<E> mComparator;

    public StackMode getMode() {
        return mMode;
    }

    /**
     * set the mode which will effect the 'add(E)' and 'add(index,E)' operation.
     * default is {@link StackMode#Normal}
     *
     * @param mode can't be null
     */
    public void setStackMode(StackMode mode) {
        if (mode == null)
            throw new NullPointerException();
        this.mMode = mode;
    }

    public Comparator<E> getComparator() {
        return mComparator;
    }

    /**
     * set the comparator , which will indicate how to find E. so effect {@link #indexOf(Object)} directly.
     * if {@link #getMode()} == {@link StackMode#Normal},this have no effect.
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

    public static StackMode convertToMode(int modeVlue) {
        switch (modeVlue) {
            case 1:
                return StackMode.Normal;

            case 2:
                return StackMode.ClearPrevious;

            case 3:
                return StackMode.ReplacePrevious;

            case 4:
                return StackMode.ReplacePreviousAndClearAfter;

            default:
                throw new IllegalStateException("wrong mode = " + modeVlue);
        }
    }
}