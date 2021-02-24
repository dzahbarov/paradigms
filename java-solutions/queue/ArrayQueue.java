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

public class ArrayQueue {
    private Object[] elements = new Object[2];
    private int size = 0;
    private int head = 0;

    /*
   Pred: obj != null
   Post: n = n' + 1 && a[n] = obj && forall i=1...n' a[i] == a'[i]
    */
    public void enqueue( Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(size + 1);
        elements[(head + size++) % elements.length] = obj;
    }

    /*
    Pred: capacity >= 0
    Post: elements.length >= capacity, если хотим добавить не больше чем в 2 раза && immutable
     */
    private void ensureCapacity( final int capacity) {
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
    public Object element() {
        assert size > 0;
        return elements[head];
    }

    /*
   Pred: n > 0
   Post: R = a'[1] && n = n'-1 && forall i = 1..n: a[i] = a'[i+1]
   */
    public Object dequeue() {
        assert size > 0;
        size--;
        Object result = elements[head];

        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    /*
    Pred: True
    Post: R = n && Immutable
     */
    public int size() {
        return size;
    }

    /*
    Pred: True
    Post: R = (n == 0) && Immutable
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
    Pred: True
    Post: n = 0
     */
    public void clear() {
        elements = new Object[2];
        size = 0;
        head = 0;
    }

    /*
    Pred: n > i >= 0
    Post: R = a[i + 1] && Immutable
     */
    public Object get(int i) {
        assert i >= 0;
        return elements[(head + i) % elements.length];
    }

    /*
    Pred: n > i >= 0 && obj != null
    Post: a[i + 1] = obj && n = n' && forall k = 1..n\{i} a[k] = a[k']
     */
    public void set(int i, Object obj) {
        assert i >= 0;
        Objects.requireNonNull(obj);
        elements[(head + i) % elements.length] = obj;
    }

}
