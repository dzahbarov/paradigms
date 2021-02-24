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
    public void enqueue(ArrayQueue this, Object obj) {
        Objects.requireNonNull(obj);
        this.ensureCapacity(size + 1);
        this.elements[(head + size++) % elements.length] = obj;
    }

    /*
    Pred: capacity >= 0
    Post: elements.length > n && immutable
     */
    private void ensureCapacity(ArrayQueue this, final int capacity) {
        assert capacity >= 0;
        if (this.elements.length < capacity) {
            Object[] tmp = new Object[capacity * 2];
            System.arraycopy(this.elements, this.head, tmp, 0, this.elements.length - this.head);
            System.arraycopy(this.elements, 0, tmp, this.elements.length - this.head, this.head);
            this.elements = tmp;
            this.head = 0;
        }
    }

    /*
    Pred: n > 0
    Post: R = a'[1] && Immutable
    */
    public Object element(ArrayQueue this) {
        assert this.size > 0;
        return this.elements[this.head % this.elements.length];
    }

    /*
   Pred: n > 0
   Post: R = a'[1] && n = n'-1 && forall i = 1..n: a[i] = a'[i+1]
   */
    public Object dequeue(ArrayQueue this) {
        assert this.size > 0;
        this.size--;
        Object result = this.elements[this.head % this.elements.length];

        this.elements[this.head % this.elements.length] = null;
        this.head = (this.head + 1) % this.elements.length;
        return result;
    }

    /*
    Pred: True
    Post: R = n && Immutable
     */
    public int size(ArrayQueue this) {
        return this.size;
    }

    /*
    Pred: True
    Post: R = (n == 0) && Immutable
     */
    public boolean isEmpty(ArrayQueue this) {
        return this.size == 0;
    }

    /*
    Pred: True
    Post: n = 0
     */
    public void clear(ArrayQueue this) {
        this.elements = new Object[2];
        this.size = 0;
        this.head = 0;
    }

    /*
    Pred: i >= 0
    Post: R = a[i] && Immutable
     */
    public Object get(ArrayQueue this, int i) {
        assert i >= 0;
        return this.elements[(this.head + i) % this.elements.length];
    }

    /*
    Pred: i >= 0 && obj != null
    Post: a[i] = obj && n = n' && forall k = 1..n\{i} a[k] = a[k']
     */
    public void set(ArrayQueue this, int i, Object obj) {
        assert i >= 0;
        Objects.requireNonNull(obj);
        this.elements[(this.head + i) % this.elements.length] = obj;
    }

}
