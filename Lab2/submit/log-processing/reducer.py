#!/usr/bin/python3


import sys
from time import sleep

all_count = 0
min_dict = {}
count = {
    "all": 0,
    "INFO": 0,
    "WARN": 0,
    "ERROR": 0,
    "FATAL": 0,
}
min_count = {}

current = ""

for line in sys.stdin:
    line = line.strip()
    min, level = line.split('\t',1)
    level = level.upper()
    if level in count.keys():
        if min != current:
            if current != "":
                print("%s\t%d\t%d\t%d\t%d\t%d" % (current,min_count[current], count["INFO"], count["WARN"], count["ERROR"], count["FATAL"]))
            current = min
            count = {
                "all": 0,
                "INFO": 0,
                "WARN": 0,
                "ERROR": 0,
                "FATAL": 0,
            }
            min_count[min] = 0
        
        all_count += 1
        count[level] += 1
        min_count[min] += 1

print("%s\t%d\t%d\t%d\t%d\t%d" % (current,min_count[current], count["INFO"], count["WARN"], count["ERROR"], count["FATAL"]))
