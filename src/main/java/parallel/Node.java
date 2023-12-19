package parallel;


import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private Entry entry;
    private Node next;
    private Node previous;
    private final ReentrantLock lock;

    public Node(Entry entry) {
        this.entry = entry;
        this.next = null;
        this.previous = null;
        this.lock = new ReentrantLock();
    }

    public void lock() {
        lock.lock();
    }
    public void unlock() {
        lock.unlock();
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }


}
