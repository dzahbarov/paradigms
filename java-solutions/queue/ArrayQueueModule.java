package queue;

import java.util.Objects;

/*
Model:
    [a1, a2, ..., an]
    n -- размер очереди

Inv:
    n >= 0
    forall i=1...n a[i] != null
Immutable:
    n = n' && forall i=1...n' a[i] == a'[i]
*/

public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int size = 0;
    private static int head = 0;

    /*
    Pred: obj != null
    Post: n = n' + 1 && a[n] = obj && forall i=1...n' a[i] == a'[i]
     */
    public static void enqueue(Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(size + 1);
        elements[(head + size++) % elements.length] = obj;
    }

    /*
    Pred: capacity >= 0
    Post: elements.length >= capacity, если хотим добавить не больше чем в 2 раза && immutable
     */
    private static void ensureCapacity(final int capacity) {
        assert capacity >= 0;
        if (elements.length < capacity) {
            Object[] tmp = new Object[capacity * 2];
            System.arraycopy(elements, head, tmp, 0, elements.length - head);
            System.arraycopy(elements, 0, tmp, elements.length - head, head);
            elements = tmp;
            head = 0;
        }
    }

    /*
   Pred: n > 0
   Post: R = a'[1] && Immutable
    */
    public static Object element() {
        assert size > 0;
        return elements[head % elements.length];
    }

    /*
   Pred: n > 0
   Post: R = a'[1] && n = n'-1 && forall i = 1..n: a[i] = a'[i+1]
    */
    public static Object dequeue() {
        assert size > 0;
        size--;
        Object result = elements[head % elements.length];

        elements[head % elements.length] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    /*
    Pred: True
    Post: R = n && Immutable
     */
    public static int size() {
        return size;
    }

    /*
    Pred: True
    Post: R = (n == 0) && Immutable
     */
    public static boolean isEmpty() {
        return size == 0;
    }


    /*
    Pred: True
    Post: n = 0
     */
    public static void clear() {
        elements = new Object[2];
        size = 0;
        head = 0;
    }

    /*
    Pred: i >= 0
    Post: R = a[i] && Immutable
    */
    public static Object get(int i) {
        assert i >= 0;
        return elements[(head + i) % elements.length];
    }

    /*
    Pred: i >= 0 && obj != null
    Post: a[i] = obj && n = n' && forall k = 1..n\{i} a[k] = a[k']
     */
    public static void set(int i, Object obj) {
        assert i >= 0;
        Objects.requireNonNull(obj);
        elements[(head + i) % elements.length] = obj;
    }
}
