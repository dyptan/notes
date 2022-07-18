package src.serialization.User;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Serialize {
    public static void main(String[] args) {
        User ivan = new User("Ivan", "Dyptan", 30);
        try (RandomAccessFile raf = new RandomAccessFile("user.ser", "rw");
        ){
            ivan.writeObject(raf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
