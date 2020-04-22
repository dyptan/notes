file=open('romeo.txt').read()

file=file.split()
list=[]
for word in file:
    if word not in list:
        list.append(word)
    if word==[]: break
list.sort()
print list