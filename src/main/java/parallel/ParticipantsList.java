package parallel;

import java.util.concurrent.ConcurrentHashMap;

public class ParticipantsList {
    private final Node head;
    private final Node tail;
//    private final List<Integer> blackList = new LinkedList<>();
    private final ConcurrentHashMap<Integer, Integer> blackList = new ConcurrentHashMap<>();

    public ParticipantsList() {
        Node first = new Node(new Entry(-1, Integer.MAX_VALUE,"C0"));
        Node last = new Node(new Entry(-2, Integer.MIN_VALUE,"C0"));
        first.setNext(last);
        last.setPrevious(first);
        head = first;
        tail = last;
    }

    public void insert(Entry entry) {
        if(blacklisted(entry)){
            return;
        }
        Node prev = head;
        prev.lock();
        Node current = head.getNext();
        try {
            while (current != null) {
                current.lock();
                //System.out.println("Thread " + Thread.currentThread().getId()+": " + entry.getId() + " "+entry.getScore()+" " + current.getEntry().getId());
                if (current.getEntry().getId() == entry.getId()) {
                    if(entry.getScore()<0) {
                        addBlackList(entry.getId());
                        Node next = current.getNext();
                        next.lock();
//                        System.out.println("Removing " + entry.getId()  );
                        remove(prev,current,next);
//                        System.out.println("Removed " + entry.getId());

                        next.unlock();
//                        System.out.println("unlocked everything");
//                        System.out.println(current.getPrevious() + " " + current.getNext());
                        break;
                    }else{
                        addScore(current, entry);
//                        System.out.println("Adding score" + entry.getId() + " " + entry.getScore());
                        break;
                    }
                } else if (current == tail){
                    if(!blacklisted(entry)){
                        if(entry.getScore()<0) {
                            addBlackList(entry.getId());
//                            System.out.println("Removing " + entry.getId()  );
                            break;
                        }else{
//                            System.out.println("Adding node" + entry.getId() + " " + entry.getScore());
                            addNewNode(prev,current,entry);

                            break;
                        }
                    }else{

                        break;
                    }
                }
                prev.unlock();
                prev = current;
                current = current.getNext();
            }
        } finally {
            prev.unlock();
            if (current != null) {
                current.unlock();
            }else{
                System.out.println("Current is null");
            }
        }
    }

    private void addNewNode(Node prev, Node current, Entry entry) {
        Node newParticipant = new Node(entry);
        prev.setNext(newParticipant);
        newParticipant.setPrevious(prev);
        newParticipant.setNext(current);
        current.setPrevious(newParticipant);
    }

    private void addScore(Node participant, Entry entry) {
        participant.getEntry().addScore(entry.getScore());
    }


    private boolean blacklisted(Entry entry) {
        return blackList.containsKey(entry.getId());
//        synchronized (blackList){
//            return blackList.contains(entry.getId());
//        }
    }

    private void addBlackList(int id) {
        blackList.put(id,id);
//        synchronized (blackList){
//            blackList.add(id);
//        }
    }

    private void remove(Node prev, Node current, Node next) {
        prev.setNext(next);
        next.setPrevious(prev);
        current.setNext(null);
        current.setPrevious(null);
    }


    public void printToFile(String filename){
        OrderedList orderedList = new OrderedList();
        Node node = head.getNext();
        while(node != tail){
            orderedList.add(node.getEntry());
            node = node.getNext();
        }
        orderedList.printToFile(filename);
    }

}
