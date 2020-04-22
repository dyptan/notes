def maximum(listing):
    '''
    >>> l=[3,6,21,8,34,785,34]
    >>> print (maximum(l))
    785
    '''
    maxdig=0
    for i in listing:
        if i > maxdig:
            maxdig = i
        else: continue

    return maxdig

l=[-5,-4,-1,9]
print (maximum(l))