package src.exceptions;

/**
 * Created by diptan on 08.06.18.
 */
public class InvalidAgeException extends RuntimeException{
    InvalidAgeException(){
        throw new RuntimeException("Wrong age!");
    }
}
