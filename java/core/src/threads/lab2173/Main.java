package src.threads.lab2173;

/**
 * Created by diptan on 17.07.18.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {

        int[] myArray = new int[1000];
        MySumCount sumCount1 = new MySumCount();
        MySumCount sumCount2 = new MySumCount();

        int checkSum=0;
        for (int i = 0; i < myArray.length; i++){
            myArray[i] = (int) (Math.random() * 1000);
            checkSum+=myArray[i];
        }

        sumCount1.setStartIndex(0);
        sumCount1.setStopIndex(500);
        sumCount1.setIntArray(myArray);

        sumCount2.setStartIndex(500);
        sumCount2.setStopIndex(1000);
        sumCount2.setIntArray(myArray);

        sumCount1.start();
        sumCount1.join();

        sumCount2.start();
        sumCount2.join();


        System.out.println("checkSum = "+checkSum);
        System.out.println("Sum = "+MySumCount.resultSum);

    }
}

class MySumCount extends Thread {
    private int[] intArray;
    static long resultSum;
    public int startIndex,stopIndex;

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setStopIndex(int stopIndex) {
        this.stopIndex = stopIndex;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    @Override
    public void run(){
        while (startIndex<stopIndex)
        {
            resultSum+=intArray[startIndex];
            startIndex++;
            System.out.println(startIndex+" "+resultSum+" "+this.getName());
        }
    }
}