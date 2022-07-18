package src.serialization.Employee;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serialize {
    public static void main(String[] args) {
        Employee vasya = new Employee("Vasyl Kyrylovych", "Kyiv", 98752, 630024466);
        try (FileOutputStream fos = new FileOutputStream("employee.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fos))
        {
            oos.writeObject(vasya);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
