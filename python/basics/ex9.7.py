file = open('mbox-short.txt')
counts = dict()
words=list()
for line in file:
    words = line.split()
    if 'From' not in words : continue
    if words[2] not in counts:
        counts[words[2]] = 1
    else:
        counts[words[2]] += 1

lst = list()
for key, val in counts.items():
    lst.append( (val, key) )
lst.sort(reverse=True)

for key, val in lst[:7] :
    print key, val