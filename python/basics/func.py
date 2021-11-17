def maximum(listing):
    """
    >>> mylist=[3,6,21,8,34,785,34]
    >>> print (maximum(mylist))
    785
    """
    maxdig = 0
    for i in listing:
        if i > maxdig:
            maxdig = i
        else:
            continue

    return maxdig


l = [-5, -4, -1, 9]
print(maximum(l))
