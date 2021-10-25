package binarysearch;

import java.util.Arrays;

public class e1 {
    public static int solve(int n) {
        int[] tasks = {1,2,3,4,5};
        Arrays.stream(tasks).sorted();
        int oddNums = n*2;
        int acc = 0;
        for (int i=0; i<=oddNums; i++) {
            if (i%2==1) {
                acc+=i;
            }
        }
        return acc;
    }

    public static int solve(int[] tasks, int[] people) {
        int tasksCanBeSolved = 0;
        int[] sortedTasks =  tasks;
        Arrays.sort(sortedTasks);

        for (int person : people) {
            for (int i = sortedTasks.length-1; i>=0; i--){
                if (sortedTasks[i]<=person && sortedTasks[i]!=0) {
                    tasksCanBeSolved++;
                    sortedTasks[i] = 0;
                    break;
                }
            }
        }

        return tasksCanBeSolved;
    }

    public static void main(String[] args) {
        int[] tasks = {1,3,5,6,9,9};
        int[] people = {2,2,7,4,11,10};
        System.out.println(solve(tasks,people ));
    }
}


