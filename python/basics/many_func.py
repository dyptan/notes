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
        for i in range(len(l) - 1):
            if l[i] > l[i + 1]:
                temp = l[i]
                l[i] = l[i + 1]
                l[i + 1] = temp
    return l


def reverse(x):
    '''
    >>> x='straight'
    >>> print(reverse(x))
    thgiarts
    '''
    x = x[::-1]
    return x


def is_palindrome(x):
    '''
    >>> x='radar'
    >>> print(is_palindrome(x))
    True
    >>> x='radam'
    >>> print(is_palindrome(x))
    False
    '''
    if x == reverse(x):
        return True
    else:
        return False


def is_member(x, y):
    '''
    >>> x=0
    >>> y=[5,2,6,3]
    >>> print(is_member(x,y))
    False
    >>> x=5
    >>> y=[5,2,6,3]
    >>> print(is_member(x,y))
    True
    '''
    for i in y:
        if i == x:
            return True
        else:
            continue
    return False


def overlapping(x, y):
    '''
    >>> x=[0,17,45,6]
    >>> y=[6,4,8,7,5]
    >>> print overlapping(x,y)
    True
    >>> x=[0,17,45]
    >>> y=[6,4,8,7,5]
    >>> print overlapping(x,y)
    False
    '''
    for s in x:
        for i in y:
            if i == s:
                return True
            else:
                continue

    return False


def lengh(x):
    '''
    >>> liste=['my','name', 'is', 'Ivan']
    >>> print lengh(liste)
    [2, 4, 2, 4]
    '''
    l = []
    for i in x:
        l.append(len(i))
    return l
