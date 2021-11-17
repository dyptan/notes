# import urllib
# file=urllib.urlopen('http://www.py4inf.com/code/mbox-short.txt').read()
file = open('mbox-short.txt').read()
file = file.split()
# file=file.upper()
print(file)
