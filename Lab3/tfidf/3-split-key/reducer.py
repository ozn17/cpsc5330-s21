#!/usr/bin/python3


import sys

current = None
current_count = 0


for line in sys.stdin:
    line = line.strip()
    ar, c, count = line.split('\t',1)
    
    try:
        c = int(c)
    except Exception:
        continue

    if current == ar:
        
        current_count += 1
    else:
        if current:
            print("%s\t%d" % (current, current_count))
        current = ar
        current_count = 1

if current == ar:
    print("%s\t%d" % (current, current_count))

