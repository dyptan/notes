package sorting;

public class BubbleSort {
    public static void sort(int[] array){
        for (int i = 0; i<array.length; i++){
            for (int j = 0; j<array.length-1-i; j++)
                if (array[j] > array[j+1]) {
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }

        }
    }

    public static void main(String[] args){
        int[] myarray = {1,8,5,3,10};
        sort(myarray);
        for (int element : myarray) {
            System.out.println(element);
        }
    }
}
