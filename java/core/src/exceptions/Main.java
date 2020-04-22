package src.exceptions;

/**
 * Created by diptan on 08.06.18.
 */
public class Main {
    public static void main(String[] args) {
        Person person = new Person();
        try {
            person.setAge(100);
            System.out.println("It works! Congratulations!");
        } catch (InvalidAgeException e){
            System.out.println("you can't do this!");
            e.getMessage();
        } finally {
            System.out.println("Always make sure you have common sense.");
        }
    }
}
