package parallel;


import java.util.Objects;
import java.util.Scanner;

public class ReaderThread implements Runnable{
    private final MessageQueue queue;
    private final String file;

    public ReaderThread(MessageQueue queue, String file) {
        this.queue = queue;
        this.file = file;
    }

    @Override
    public void run() {
//        System.out.println("Reader " + Thread.currentThread().getId() + " started reading");
        readFromFile(file);
//        System.out.println("Reader " + id + " finished reading");
    }

    private void readFromFile(String file){
        Scanner scanner = new Scanner(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(file)));
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            int id = Integer.parseInt(tokens[0]);
            int score = Integer.parseInt(tokens[1]);
            String country;
            country = file.substring(9, 11);
            Entry entry = new Entry(id, score, country);
            try {
//                System.out.println("Reader " + this.id + " read " + entry + " from " + file);
                queue.put(entry);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
