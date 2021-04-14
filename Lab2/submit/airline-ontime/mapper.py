#!/usr/bin/python3


import sys

for line in sys.stdin:
    line = line.strip()
    airline, delay = line.split(",")
    if delay == "null":
        continue
    print("%s\t%s" % (airline, delay))
