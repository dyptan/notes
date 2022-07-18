package src.serialization.User;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Deserialize {
    public static void main(String[] args) {
        User ivan = new User();
        try (RandomAccessFile fis = new RandomAccessFile ("user.ser", "rw")
        ){
            ivan = ivan.readObject(fis);
            System.out.println(ivan);

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
}
