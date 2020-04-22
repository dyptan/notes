package src.generics.lab2_14_1;

/**
 * Created by diptan on 15.06.18.
 */
public class Main {
    public static void main(String[] args) {
        MyTuple<Integer, String, Float> triplet1 = new MyTuple<Integer,String,Float>(10,"ten", 10.0f);
        MyTuple<Integer, String, Float> triplet2 = new MyTuple<Integer,String,Float>(12,"twelve", 12.0f);
    }
}
