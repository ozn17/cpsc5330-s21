#!/usr/bin/python3

# a to i is an "early" word
# j to r is a "middle" word
# s to z is a "late" word

# a is ordinal 97, z is ordinal 122

import sys
import string

for line in sys.stdin:
    for word in line.strip().split():
        lowered = word.lower()
        filtered = "".join(filter(lambda c: 97 <= ord(c) <= 122, lowered))
        if len(filtered) > 0:
            o = ord(filtered[0])
            if o <= 105:
                k = 'early'
            elif o <= 114:
                k = 'middle'
            elif o <= 122:
                k = 'late'
            else: 
                raise "Bad ordinal!"
            print('%s\t%s' % (k, len(filtered)))
