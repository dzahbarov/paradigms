package queue;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[2];
    private int head = 0;

<<<<<<< HEAD
    protected void enqueueImpl(Object obj) {
        ensureCapacity(size);
        elements[(head + size - 1) % elements.length] = obj;
    }

    private void ensureCapacity(final int capacity) {
=======
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
>>>>>>> a429dd3339698008163854e1303b5b3ae3252839
        assert capacity >= 0;
        if (elements.length < capacity) {
            Object[] tmp = new Object[capacity * 2];
            System.arraycopy(elements, head, tmp, 0, elements.length - head);
            System.arraycopy(elements, 0, tmp, elements.length - head, head);
            elements = tmp;
            head = 0;
        }
    }

<<<<<<< HEAD
=======
    /*
    Pred: n > 0
    Post: R = a'[1] && Immutable
    */
>>>>>>> a429dd3339698008163854e1303b5b3ae3252839
    public Object element() {
        assert size > 0;
        return elements[head];
    }

<<<<<<< HEAD
    protected Object dequeueImpl() {
        Object result = elements[head];
=======
    /*
   Pred: n > 0
   Post: R = a'[1] && n = n'-1 && forall i = 1..n: a[i] = a'[i+1]
   */
    public Object dequeue() {
        assert size > 0;
        size--;
        Object result = elements[head];

>>>>>>> a429dd3339698008163854e1303b5b3ae3252839
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

<<<<<<< HEAD
    protected void clearImpl() {
        elements = new Object[2];
        head = 0;
    }

    public Object get(int i) {
        assert i >= 0 && i <= size;
        return elements[(head + i) % elements.length];
    }

    protected void setImpl(Object obj, int index) {
        elements[(head + index) % elements.length] = obj;
    }

    protected Object[] toArrayImpl(Object[] res) {
        for (int i = 0; i < size; i++) {
            res[i] = get(i);
        }
        return res;
    }


=======
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
>>>>>>> a429dd3339698008163854e1303b5b3ae3252839

}
