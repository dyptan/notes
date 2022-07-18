package src.generics.lab2_14_2;

/**
 * Created by diptan on 18.06.18.
 */
public class MyTestMethod{

    public static <T extends Comparable<T>> int calcNum (T[] array, T maxElem){
        int i = 0;
        for (T element:array)
        {
            if (element.compareTo(maxElem)==1)
                i++;
            else continue;
        }
        return i;
    }

    public static <T extends Number> double  calcSum (T[] array){
        double sum = 0.0;
        for (T element:array) {
            sum+=element.doubleValue();
        }
        return sum;
    }
}
