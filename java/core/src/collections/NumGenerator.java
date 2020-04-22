package src.collections;

import java.util.*;

/**
 * Created by diptan on 29.06.18.
 */
public class NumGenerator {
    private int NumOfElem, MaxNum;

    public NumGenerator(int numOfElem, int maxNum) {
        NumOfElem = numOfElem;
        MaxNum = maxNum;
    }

    public List generate() {
        List<Integer> mylist = new ArrayList<>();
        Random randInstance = new Random();

        for (int i = 0; i < NumOfElem; i++) {
            Integer randNum=randInstance.nextInt(MaxNum);
            mylist.add(randNum);
        }

        return mylist;
    }

    public Set generateDistinct() {
        Set<Integer> mylist = new HashSet<>();
        Random randInstance = new Random();

        for (int i = 0; i < NumOfElem; i++) {
            Integer randNum=randInstance.nextInt(MaxNum);
            mylist.add(randNum);
        }

        return mylist;
    }
}
