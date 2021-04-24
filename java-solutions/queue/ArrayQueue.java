package queue;

import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[2];
    private int head = 0;

    protected void enqueueImpl(Object obj) {
        ensureCapacity(size+1);
        elements[(head + size) % elements.length] = obj;
    }

    private void ensureCapacity(final int capacity) {
        assert capacity >= 0;
        if (elements.length < capacity) {
            Object[] tmp = toArray();
            elements = new Object[2 * capacity];
            System.arraycopy(tmp, 0, elements, 0, size);
            head = 0;
        }
    }

    public Object element() {
        assert size > 0;
        return elements[head];
    }

    protected Object dequeueImpl() {
        Object result = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        return result;
    }

    protected void clearImpl() {
        elements = new Object[2];
        head = 0;
    }

    @Override
    public void set(int i, Object obj) {
        assert i >= 0;
        Objects.requireNonNull(obj);
        elements[(head + i) % elements.length] = obj;
    }

    @Override
    public Object get(int i) {
        assert i >= 0;
        return elements[(head + i) % elements.length];
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        for (int i = 0; i < size; i++) {
            res[i] = get(i);
        }
        return res;
    }
}
