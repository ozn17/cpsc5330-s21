#!/usr/bin/python3


import sys, re

all_count = 0
count = {
    "INFO": 0,
    "WARN": 0,
    "ERROR": 0,
    "FATAL": 0,
}

pattern = "\d+-\d+-\d+\s\d+\:(\d+)\S+\s(\w+)\s.*"

for line in sys.stdin:
    line = line.strip()
    match = re.search(pattern, line)
    if match:
        items = match.groups()
        if len(items) != 2:
            continue
        print("%s\t%s" % (items[0], items[1].upper()))

