package queue;

import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private Node tail = new Node(null, null);
    private Node head = new Node(tail, null);

    private static class Node {
        private Node next;
        private Object value;

        public Node(final Node next, final Object value) {
            this.next = next;
            this.value = value;
        }
    }

    protected void enqueueImpl(Object obj) {
        tail.value = obj;
        tail.next = new Node(null, null);
        tail = tail.next;
    }

    public Object element() {
        assert size > 0;
        return head.next.value;
    }

    protected Object dequeueImpl() {
        Node result = head.next;
        head.next = result.next;
        return result.value;
    }

    protected void clearImpl() {
        tail = new Node(null, null);
        head = new Node(tail, null);
    }

    @Override
    public Object get(int i) {
        assert i >= 0 && i <= size;
        Node node = head;
        for (int j = 0; j <= i; j++) {
            node = node.next;
        }
        return node.value;
    }

    @Override
    public void set(int index, Object obj) {
        assert index >= 0 && index <= size;
        Objects.requireNonNull(obj);
        Node node = head;
        for (int j = 0; j <= index; j++) {
            node = node.next;
        }
        node.value = obj;
    }

    @Override
    public Object[] toArray() {
        Object[] res = new Object[size];
        Node node = head;
        for (int i = 0; i < size; i++) {
            res[i] = node.next.value;
            node = node.next;
        }
        return res;
    }

}
