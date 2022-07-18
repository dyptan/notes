package src.generics.lab2_14_2;

/**
 * Created by diptan on 18.06.18.
 */
public class Main {
    public static void main(String[] args) {
        Double[] myarray = {2.4,5.4,7.0,1.0,0.0,3.0};
        System.out.println(MyTestMethod.calcNum(myarray,2.0));
        System.out.println(MyTestMethod.calcSum(myarray));
    }
}
