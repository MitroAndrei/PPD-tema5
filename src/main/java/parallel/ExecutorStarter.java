package parallel;


import java.util.List;
import java.util.concurrent.*;

public class ExecutorStarter {
    private final ExecutorService executor;
    private final MessageQueue queue;
    private final List<String> filesList;

    public ExecutorStarter(Integer nrThreads, MessageQueue queue, List<String> filesList) {
        this.executor = Executors.newFixedThreadPool(nrThreads);
        this.queue = queue;
        this.filesList = filesList;
    }
    public void start() {
        for(String file:filesList){
            executor.execute(new ReaderThread(queue, file));
        }
    }
    public void stop() {
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        queue.setIsWorking(false);
    }
}
