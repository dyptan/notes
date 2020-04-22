file = open('mbox-short.txt')
counts = dict()
word=list()
for line in file:
    word = line.split()
    if 'From:' not in word : continue
    print 'word', word
    if word[1] not in counts:
        counts[word[1]] = 1
    else:
        counts[word[1]] += 1

lst = list()
for key, val in counts.items():
    lst.append( (val, key) )
lst.sort(reverse=True)

for key, val in lst[:1] :
    print key, val