file = open('mbox-short.txt')
count = 0
for line in file:
    # print word
    line = line.split()
    if len(line) == 0 : continue
    if line[0] != 'From:' : continue
    count += 1
    print(line[1])
print('There were %d lines in the file with From as the first word' % count)
