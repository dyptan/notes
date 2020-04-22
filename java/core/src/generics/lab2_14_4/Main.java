package src.generics.lab2_14_4;

import java.util.Arrays;

/**
 * Created by diptan on 15.06.18.
 */
public class Main {
    public static void main(String[] args) {
        String[] myarray = {"one","two","three","four","five","twelve"};
        System.out.println(Arrays.toString(myarray));
        new MyMixer<>().shuffle(myarray);
        System.out.println(Arrays.toString(myarray));
    }
}
