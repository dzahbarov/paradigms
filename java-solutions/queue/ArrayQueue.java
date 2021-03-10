package queue;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[2];
    private int head = 0;

    protected void enqueueImpl(Object obj) {
        ensureCapacity(size);
        elements[(head + size - 1) % elements.length] = obj;
    }

    private void ensureCapacity(final int capacity) {
        assert capacity >= 0;
        if (elements.length < capacity) {
            Object[] tmp = new Object[capacity * 2];
            System.arraycopy(elements, head, tmp, 0, elements.length - head);
            System.arraycopy(elements, 0, tmp, elements.length - head, head);
            elements = tmp;
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



}
