package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    protected abstract void enqueueImpl(Object obj);
    protected abstract Object dequeueImpl();
    protected abstract void clearImpl();
    protected abstract void setImpl(Object obj, int i);
    public abstract Object get(int i);
    public abstract Object element();
    protected abstract Object[] toArrayImpl(Object[] res);
    @Override
    public void enqueue(Object obj) {
        Objects.requireNonNull(obj);
        size++;
        enqueueImpl(obj);
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
        setImpl(obj, i);
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
        return toArrayImpl(res);
    }
}
