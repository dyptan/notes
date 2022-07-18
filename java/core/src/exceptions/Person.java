package src.exceptions;

/**
 * Created by diptan on 08.06.18.
 */
public class Person {
    String firstName, lastName;
    int age;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        if (age > 100 || age < 1) {
            throw new InvalidAgeException();
        }
        else this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }
}
