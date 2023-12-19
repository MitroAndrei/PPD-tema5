package parallel;


import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    private final Deque<Entry> queue = new LinkedList<>();

    private static final int MAX = 100;

    private volatile boolean isWorking = true;

    public MessageQueue() {
    }

    public void put(Entry entry) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == MAX) {
                notFull.await();
            }
            queue.add(entry);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Entry take() throws InterruptedException {
        lock.lock();
        try {
            while (shouldWait()) {
                notEmpty.await();
            }
            Entry entry = queue.poll();
            if(entry == null){
                return null;
            }
            notFull.signal();
            return entry;
        } finally {
            lock.unlock();
        }
    }

    public boolean shouldWait() {
        try {
            lock.lock();
            if (!isWorking) {
//                System.out.println(!queue.isEmpty());
                return false;
            } else {
                return queue.isEmpty();
            }
        }finally {
            lock.unlock();
        }
    }

    public void setIsWorking(boolean isWorking) {
        try {
            lock.lock();
            this.isWorking = isWorking;
            notEmpty.signalAll();
        }finally {
            lock.unlock();
        }
    }

}
