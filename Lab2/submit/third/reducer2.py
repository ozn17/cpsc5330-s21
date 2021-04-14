#!/usr/bin/python3


import sys

for line in sys.stdin:
    line = line.strip()
    word, c = line.split('\t',1)

    print("%s\t%s" % (c,word,))
