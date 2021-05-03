#!/usr/bin/python3

import sys
import re

pattern = r"[^a-zA-Z]"
for line in sys.stdin:
    for word in line.strip().replace("-", " ").split():
        word = word.strip().lower()
        lowered = word.lower()
        w = re.sub(pattern, "", word)
        if len(w) > 0:
            print(w)
