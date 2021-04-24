#!/usr/bin/python3


import sys
from time import sleep

current = None
current_docid = None
current_count = 0


for line in sys.stdin:
    line = line.strip()
    word_docid, c = line.split('\t',1)

    ar, docid = word_docid.split("+")

    if current == ar:
        if current_docid != docid:
            current_docid = docid
            current_count += 1
    else:
        if current:
            print("%s\t%d" % (current, current_count))
        current = ar
        current_docid = docid
        current_count = 1

if current == ar:
    print("%s\t%d" % (current, current_count))

