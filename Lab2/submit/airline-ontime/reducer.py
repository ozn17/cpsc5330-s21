#!/usr/bin/python3


import sys
from time import sleep

current = None
current_sum = 0
current_count = 0
current_min = 0
current_max = 0


for line in sys.stdin:
    line = line.strip()
    ar, delay = line.split('\t',1)
    ar = ar.upper()

    try:
        delay = float(delay)
    except Exception:
        continue

    if current == ar:
        current_sum += delay
        current_count += 1
        current_min = min(current_min, delay)
        current_max = max(current_max, delay)
    else:
        if current:
            print("%s\t%f\t%f\t%f" % (current, current_min,current_max, (1.0*current_sum)/current_count))
        current = ar
        current_sum = delay
        current_count = 1
        current_min = delay
        current_max = delay

if current == ar:
    print("%s\t%f\t%f\t%f" % (current, current_min, current_max, (1.0*current_sum)/current_count))

