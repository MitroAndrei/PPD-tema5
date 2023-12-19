package main;

import parallel.*;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        while (true) {
            int totalThreads = 6;
            int readThreads = 2;
            FileNameGenerator fileNameGenerator = new FileNameGenerator(1);
            MessageQueue messageQueue = new MessageQueue();
            ParticipantsList participantsList = new ParticipantsList();
            Thread[] threads = new Thread[totalThreads - readThreads];
            ExecutorStarter executorStarter = new ExecutorStarter(readThreads, messageQueue, fileNameGenerator.getFilesForThread(0));
            Instant start = Instant.now();
            executorStarter.start();
            for (int i = 0; i < totalThreads - readThreads; i++) {
                threads[i] = new WorkerThread(i + readThreads, messageQueue, participantsList);
                threads[i].start();
            }
            executorStarter.stop();
            for (int i = 0; i < totalThreads-readThreads; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            participantsList.printToFile("Threads2.txt");
            Instant end = Instant.now();
            System.out.println((double) Duration.between(start,end).getNano() / 1000000);
            Test test = new Test();
            test.test("Threads2.txt", "Sequential.txt");
            break;
        }
    }

}
