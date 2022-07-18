# Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
# Input: nums = [2,7,11,15], target = 9
# Output: [0,1]
# Output: Because nums[0] + nums[1] == 9, we return [0, 1].

from typing import List


def twoSum(nums: List[int], target: int) -> List[int]:
    first = 0
    second = 1
    for i in range(first, len(nums)):
        while(first < len(nums)-1):
            while (second < len(nums)):
                if target - nums[first] == nums[second]:
                    return [first, second]
                else:
                    second += 1
            first += 1
            second = first+1

def recSum(nums: List[int], target: int) -> List[int]:

    def helper(numbers: List[int], first, second):
        if second>len(numbers)-1:
            return helper(nums, first+1, first+2)

        if target - numbers[first] == numbers[second]:
            return [first, second]
        else:
            return helper(numbers, first, second+1)

    output = helper(nums, 0, 1)

    return output

if __name__ == '__main__':
    mylist = [1,5,8,9]
    output = recSum(mylist, 10)
    print(output)
