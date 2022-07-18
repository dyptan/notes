def my_sort(l):
    '''
    >>> import random
    >>> rand_list = [random.randint(0,1000) for _ in range(100)]
    >>> my_list = my_sort(rand_list)
    >>> python_list = sorted(rand_list)
    >>> my_list == python_list
    True
    '''
    for i in range(len(l)):
        for i in range(len(l)-1):
            if l[i] > l[i+1]:
                temp=l[i]
                l[i]=l[i+1]
                l[i+1]=temp
    return l

# l=[1,7,3,2,19,456,43,345,76,456,234,5]
# print (my_sort(l))
# pass
