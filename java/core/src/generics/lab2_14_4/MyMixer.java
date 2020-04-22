package src.generics.lab2_14_4;

import java.util.Random;

/**
 * Created by diptan on 15.06.18.
 */
public class MyMixer<T> {
    T myarray;

    MyMixer(){}

    MyMixer(T input){
        this.myarray=input;
    }

    public void shuffle(T[] array){
        T temp;
        Random random = new Random();
        for (int i = 0; i<array.length; i++) {
            int randIndex = random.nextInt(i + 1);
            temp = array[randIndex];
            array[randIndex] = array[i];
            array[i]=temp;
        }
    }

}
