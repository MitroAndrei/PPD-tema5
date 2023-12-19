package parallel;

public class WorkerThread extends Thread{
    private final int id;
    private final MessageQueue queue;
    private final ParticipantsList participantsList;
    private int sc=0;

    public WorkerThread(int id, MessageQueue queue, ParticipantsList participantsList) {
        this.id = id;
        this.queue = queue;
        this.participantsList = participantsList;
    }

    @Override
    public void run() {
//        System.out.println("Worker " + Thread.currentThread().getId() + " started reading");
        while(true){
            try {
                Entry entry = queue.take();
                if(entry == null){
//                    System.out.println("Worker " + id + " score " + sc);
                    break;
                }
                sc++;
//                System.out.println("entry " + id + ":" + entry);
                participantsList.insert(entry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
