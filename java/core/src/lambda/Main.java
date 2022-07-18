package src.lambda;

import java.util.*;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {

        //Sort Int list with Lambda
        List<Integer> mylist = new ArrayList<>();
        Random randInstance = new Random();

        for (int i = 0; i < 10; i++) {
            Integer randNum=randInstance.nextInt(100);
            mylist.add(randNum);
        }

        System.out.println("Random Int list "+mylist);
        Collections.sort(mylist, (a,b)->a-b);
        System.out.println("Sorted Int list "+mylist);

        List<String> myStringList = Arrays.asList("Ivan","Petro","Stepan","Vasyl","Afanasiy");
        System.out.println("Unsorted String list "+Arrays.asList(myStringList));

        //Sort String list with Lambda
        myStringList.sort((x,y)->x.compareToIgnoreCase(y));
        System.out.println("Sorted String list with Lambda "+Arrays.asList(myStringList));

        //Sum of even nubmers by Lambda
        System.out.println("Sum of even nubmers with Lambda "+sumEven(mylist, x->x%2==0));

        //Select string by condition
        int y = 6;
        System.out.printf("Selected Strings shorter then %s "+printJStr(myStringList, x->x.length()<y),y);

        //copy list and add Null entry
        List<String> myNewStringList = new ArrayList<>(myStringList);
        myNewStringList.add(null);

        //Null safe convertion of Strings to uppercase
        System.out.println("\nList updated with lambda "+updateList(myNewStringList, x->x.toUpperCase()));

    }

    public static List<String> updateList(List<String> list, MyConverter converter){
        List<String> updatedList = new ArrayList<>();
        for (String s:list
             ) {
            if (!MyConverter.isNull(s)) updatedList.add(converter.convertStr(s));
        }
        return updatedList;
    }

    public static Integer sumEven(List<Integer> x, Predicate<Integer> lambdaBehaviour){
        int sum = 0;
        for (Integer i:x) {
            if (lambdaBehaviour.test(i)) sum+=i;
        }
        return sum;
    }

    public static List<String> printJStr(List<String> x, Predicate<String> lambdaBehaviour){
        List<String> selected = new ArrayList<>();
        for (String i:x) {
            if (lambdaBehaviour.test(i)) selected.add(i);
        }
        return selected;
    }
}
