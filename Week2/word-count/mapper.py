#!/usr/bin/python3

import sys

# input comes from STDIN (standard input)
for line in sys.stdin:
    # remove leading and trailing whitespace
    line = line.strip()
    # split the line into words
    words = line.strip().split()
    for word in words:
        lowered = word.lower()
        filtered = "".join(filter(lambda c: 97 <= ord(c) <= 122, lowered))
        if len(filtered) > 0:
            # Output is a tab-delimited string
            print('%s\t%s' % (filtered, 1))
