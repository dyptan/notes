package src.serialization.User;

import java.io.*;

public class User implements Serializable {
    String firstName, lastName;
    int age;

    public User() {
    }

    public User(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString(){
        return "Name: "+getFirstName()+" "+getLastName()+", age: "+getAge();
    }
    public void writeObject(RandomAccessFile out) throws IOException {
        out.writeUTF(firstName);
        out.writeUTF(lastName);
        out.writeInt(age);
    }
     public User readObject(RandomAccessFile in) throws IOException {
         firstName=in.readUTF();
         lastName=in.readUTF();
         age=in.readInt();
         return this;
     }
}
