#!/usr/bin/python3


import sys
import os

for line in sys.stdin:
    line = line.strip()
    print(line.replace("+", "\t"))
