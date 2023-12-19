package parallel;

import java.io.FileWriter;
import java.io.IOException;

public class OrderedList {
    private Node head = null;

    public void add(Entry entry){
        if(head == null){
            head = new Node(entry);
            return;
        }
        Node current = head;
        Node prev = null;
        while(current != null){
            if(current.getEntry().getScore() < entry.getScore()){
                Node newNode = new Node(entry);
                if(prev == null){
                    newNode.setNext(head);
                    head = newNode;
                }else{
                    prev.setNext(newNode);
                    newNode.setNext(current);
                }
                return;
            }
            prev = current;
            current = current.getNext();
        }
        Node newNode = new Node(entry);
        prev.setNext(newNode);
    }

    public void printToFile(String filename){
        try {
            FileWriter myWriter = new FileWriter(filename);
            Node node = head;
            while(node != null){
                myWriter.write(node.getEntry().getId() + " " + node.getEntry().getScore() + " " + node.getEntry().getCountry() + '\n');
                node = node.getNext();
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
