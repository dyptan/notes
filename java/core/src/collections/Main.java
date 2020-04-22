package src.collections;

import java.util.*;

/**
 * Created by diptan on 29.06.18.
 */
public class Main {
    public static void testNumGenerator() {
        NumGenerator testGen = new NumGenerator(10, 20);
        System.out.println(testGen.generate());
        System.out.println(testGen.generateDistinct());
    }

    public static void testTranslator() {


        WordTranslator translateText = new WordTranslator();
        translateText.addNewWord("cat", "кіт");
        translateText.addNewWord("eat", "з'їв");
        translateText.addNewWord("mouse", "мишу");

        Scanner input = new Scanner(System.in);
        String[] words = input.nextLine().split(" ");
        for (String word : words
        ) {
            System.out.print(translateText.translate(word) + " ");
        }
    }

    public static void testQueue() {
        Deque<String> mydeq = new ArrayDeque<String>(2);
        mydeq.addFirst("one");
        mydeq.addFirst("two");
        mydeq.addFirst("three");
        System.out.println(mydeq);
        System.out.println(mydeq.removeLast());

    }

    public static void testDeQueue() {
        String strArray[] = {"A1", "B2", "C3"};
        System.out.println(Arrays.asList(strArray));
        ArrayDeque<String> arrDeque = new
                ArrayDeque<String>(Arrays.asList(strArray));
        System.out.println(arrDeque);

        //System.out.println(mydeq.poll());

        Map<Integer,String> mydeq = new HashMap<>(3);
        mydeq.put(1,"one");
        mydeq.put(2,"two");
        mydeq.put(null,"null");
        mydeq.put(null,"another null");
        System.out.println(mydeq);
        //mydeq.remove(2);
//        for (String element:mydeq
//             ) {
//            System.out.println(element);
//        }

    }

    public static void testTreeSet() {
        String[] myNames = {"Shreya", "Harry", "Paul", "Shreya", "Selvan"};
        TreeSet<String> treeSetNames = new
                TreeSet<String>(Arrays.asList(myNames));

        System.out.println(Arrays.asList(myNames));
        System.out.println(treeSetNames);
    }


    public static void testCollectionUtils(){

                Object[] myArray = new Object[3];
                myArray[1] = "Java";
                myArray[0] = "10";
                //Collections.sort(myArray);
                //myArray[2] = 'z';
                int position = Arrays.binarySearch(myArray, "10");
                System.out.println(position);

    }
}
