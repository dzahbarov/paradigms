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


public class ArrayQueueADT {
    private Object[] elements = new Object[2];
    private int size = 0;
    private int head = 0;


    /*
    Pred: True
    Post: ArrayQueueADT && n == 0
     */
    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    /*
    Pred: obj != null
    Post: n = n' + 1 && a[n] = obj && forall i=1...n' a[i] == a'[i]
    */
    public static void enqueue(ArrayQueueADT queue, Object obj) {
        Objects.requireNonNull(obj);
        ensureCapacity(queue, queue.size + 1);
        queue.elements[(queue.head + queue.size++) % queue.elements.length] = obj;
    }

    /*
    Pred: capacity >= 0
    Post: elements.length > n && immutable
     */
    private static void ensureCapacity(ArrayQueueADT queue, final int capacity) {
        assert capacity >= 0;
        if (queue.elements.length < capacity) {
            Object[] tmp = new Object[capacity * 2];
            System.arraycopy(queue.elements, queue.head, tmp, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, tmp, queue.elements.length - queue.head, queue.head);
            queue.elements = tmp;
            queue.head = 0;
        }
    }

    /*
    Pred: n > 0
    Post: R = a'[1] && Immutable
    */
    public static Object element(ArrayQueueADT arrayQueue) {
        assert arrayQueue.size > 0;
        return arrayQueue.elements[arrayQueue.head % arrayQueue.elements.length];
    }

    /*
   Pred: n > 0
   Post: R = a'[1] && n = n'-1 && forall i = 1..n: a[i] = a'[i+1]
   */
    public static Object dequeue(ArrayQueueADT arrayQueue) {
        assert arrayQueue.size > 0;
        arrayQueue.size--;
        Object result = arrayQueue.elements[arrayQueue.head % arrayQueue.elements.length];

        arrayQueue.elements[arrayQueue.head % arrayQueue.elements.length] = null;
        arrayQueue.head = (arrayQueue.head + 1) % arrayQueue.elements.length;
        return result;
    }

    /*
    Pred: True
    Post: R = n && Immutable
     */
    public static int size(ArrayQueueADT arrayQueue) {
        return arrayQueue.size;
    }

    /*
    Pred: True
    Post: R = (n == 0) && Immutable
     */
    public static boolean isEmpty(ArrayQueueADT arrayQueue) {
        return arrayQueue.size == 0;
    }

    /*
    Pred: True
    Post: n = 0
     */
    public static void clear(ArrayQueueADT arrayQueue) {
        arrayQueue.elements = new Object[2];
        arrayQueue.size = 0;
        arrayQueue.head = 0;
    }


    /*
    Pred: i >= 0
    Post: R = a[i] && Immutable
     */
    public static Object get(ArrayQueueADT arrayQueue, int i) {
        assert i >= 0;
        return arrayQueue.elements[(arrayQueue.head + i) % arrayQueue.elements.length];
    }

    /*
    Pred: i >= 0 && obj != null
    Post: a[i] = obj && n = n' && forall k = 1..n\{i} a[k] = a[k']
     */
    public static void set(ArrayQueueADT arrayQueue, int i, Object obj) {
        assert i >= 0;
        Objects.requireNonNull(obj);
        arrayQueue.elements[(arrayQueue.head + i) % arrayQueue.elements.length] = obj;
    }


}
