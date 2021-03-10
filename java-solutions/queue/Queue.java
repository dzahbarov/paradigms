package queue;

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

public interface Queue {

    /*
        Pred: obj != null
        Post: n = n' + 1 && a[n] = obj && forall i=1...n' a[i] == a'[i]
    */
    void enqueue(Object obj);

    /*
        Pred: n > 0
        Post: R = a'[1] && n = n'-1 && forall i = 1..n: a[i] = a'[i+1]
    */
    Object dequeue();

    /*
        Pred: True
        Post: n = 0
    */
    void clear();

    /*
        Pred: 0 <= i <= n && obj != null
        Post: a[i] = obj && n = n' && forall k = 1..n\{i} a[k] = a[k']
    */
    void set(int i, Object obj);

    /*
        Pred: True
        Post: R = n && Immutable
    */
    int size();

    /*
        Pred: True
        Post: R = (n == 0) && Immutable
    */
    boolean isEmpty();

    /*
        Pred: 0 <= i <= n
        Post: R = a[i] && Immutable
     */
    Object get(int i);

    /*
        Pred: n > 0
        Post: R = a'[1] && Immutable
    */
    Object element();

    /*
        Pred: True
        Post: R = {a[1], a[2], .., a[n]} && Immutable
    */
    Object[] toArray();
}
