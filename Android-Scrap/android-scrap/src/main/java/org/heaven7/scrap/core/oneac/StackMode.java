package org.heaven7.scrap.core.oneac;


import org.heaven7.scrap.util.ArrayList2;

import java.util.Comparator;

/***
 * the mode of operate indicate how to effect {@link ExpandArrayList#add(Object)} or {@link ExpandArrayList#add(int, Object)}
 * if {@link ExpandArrayList#indexOf(Object)} != -1.
 * @author heaven7
 * @see ExpandArrayList#add(Object)
 * @see ExpandArrayList#add(int, Object)
 * @see #Normal
 * @see #ClearPrevious
 * @see #ReplacePrevious
 * @see #ReplacePreviousAndClearAfter
 */
public enum StackMode {

    /**
     * have no effect. same to {@link ArrayList2}
     */
    Normal(1),
    /**
     * use {@link ExpandArrayList#indexOf(Object)} to search previous ( as this behaviour depend on the Comparator
     * which can through {@link ExpandArrayList#setComparator(Comparator)} to change it).
     * clear the previous E and put new E at the end or the target index.
     *
     * @see ExpandArrayList#add(int, Object)
     * @see ExpandArrayList#indexOf(Object)
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

    private StackMode(int value) {
        this.value = value;
    }
}
