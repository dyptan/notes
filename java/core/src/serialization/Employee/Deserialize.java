package src.serialization.Employee;

import src.serialization.Employee.Employee;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Deserialize {
    public static void main(String[] args) {
        Employee vasya = null;
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream("employee.ser")))
        {
            vasya = (Employee)oos.readObject();
            System.out.println(vasya.toString());
        } catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }
}
