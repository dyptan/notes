package binarysearch;

public class e2 {

    public static int solve(int[] nums) {
        int counter = 0;
        for (int i = 0; i<=nums.length-1; i++) {
            for (int j = 0;j<=nums.length-1; j++) {
                if ((nums[i] + 1)==(nums[j])) {
                    counter++;
                    break;
                }
            }
        }
        return counter;
    }


    public static void main(String[] args) {

        int[] array = {3, 1, 2, 2, 7,9,10};
        System.out.println(solve(array));
    }
}
