package queue;

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
}
