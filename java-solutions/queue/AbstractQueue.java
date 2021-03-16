package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract void enqueueImpl(Object obj);

    protected abstract Object dequeueImpl();

    protected abstract void clearImpl();

    public abstract Object element();

    @Override
    public void enqueue(Object obj) {
        Objects.requireNonNull(obj);
        enqueueImpl(obj);
        size++;
    }

    @Override
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    @Override
    public void clear() {
        size = 0;
        clearImpl();
    }

    @Override
    public void set(int i, Object obj) {
        assert i >= 0 && i <= size;
        Objects.requireNonNull(obj);
        for (int j = 0; j < size; j++) {
            Object old = dequeue();
            if (j == i) {
                enqueue(obj);
            } else {
                enqueue(old);
            }
        }
    }

    @Override
    public Object get(int i) {
        assert i >= 0 && i <= size;
        Object res = null;
        for (int j = 0; j < size; j++) {
            Object obj = dequeue();
            if (j == i) {
                res = obj;
            }
            enqueue(obj);
        }
        return res;
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        for (int j = 0; j < size; j++) {
            Object obj = dequeue();
            res[j] = obj;
            enqueue(obj);
        }
        return res;
    }
}
