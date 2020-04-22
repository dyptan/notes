package src.threads.lab21711.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class CalcSum extends RecursiveTask<Integer> {
    int BLOCK_SIZE=10;
    int blockStart, blockEnd;
    int[] values;

    public CalcSum(int[] values) {
        this(0, values.length-1, values);
    }

    public CalcSum(int blockStart, int blockEnd, int[] values) {
        this.blockStart = blockStart;
        this.blockEnd = blockEnd;
        this.values=values;
    }

    public Integer calculate(){
        int localSum = 0;
        for (int i = blockStart; i <= blockEnd; i++) {
            //System.out.print(values[i]);
            localSum+=values[i];
        }
        //System.out.printf("Local sum: %d, Start: %d Stop: %d \n",localSum, blockStart, blockEnd);
        return localSum;
    }

    @Override
    public Integer compute() {
        int currentBlockSize = blockEnd-blockStart;
        if (currentBlockSize <= BLOCK_SIZE) {
            return calculate();
        } else {
            int mid = currentBlockSize/2;

            int firstHalfEnd = blockStart+mid;
            CalcSum firstHalf = new CalcSum(blockStart, firstHalfEnd, values);
            firstHalf.fork();

            int secondHalfStart = blockStart+mid+1;
            CalcSum secondHalf = new CalcSum(secondHalfStart, blockEnd, values);

            return (secondHalf.compute()+firstHalf.join());
        }
    }

}

public class Main {
    public static void main(String[] args) {
        int[] intArray = new int[100];
        java.util.Random randomValues = new java.util.Random();
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = randomValues.nextInt(10);
        }
        //System.out.println(Arrays.toString(intArray));

        ForkJoinPool pool = new ForkJoinPool();
        CalcSum mysum = new CalcSum(intArray);
        long startTime = System.nanoTime();
        System.out.println("ForkJoin Sum: "+ pool.invoke(mysum));
        long stopTime = System.nanoTime();
        System.out.println("took nanosec: "+(long)(stopTime - startTime));

        long startTime1 = System.nanoTime();
        int localSum = 0;
        for (int i = 0; i < intArray.length; i++) {
            localSum+=intArray[i];
        }
        long stopTime1 = System.nanoTime();
        System.out.printf("Traditional sum: %d, it took %d",localSum, (long)(stopTime1 - startTime1) );

    }
}
