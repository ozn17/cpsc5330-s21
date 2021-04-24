#!/usr/bin/python3


import sys
import os
docid = os.path.splitext(os.path.basename(os.getenv('map_input_file')))[0]


for line in sys.stdin:
    line = line.strip()
    split_words = line.split()
    for word in split_words:
        word = ''.join(list(filter(
            lambda ch: ch not in "`~!@#$%^&*()_-+={}[]\\|:;\"'<>,.?/",
            word)))
        word = word.strip()
        if word == "":
            continue
        word = word.lower()
        word = docid.lower() + "+" + word
        # print("word is:", word)
        print("%s\t%d" % (word, 1))
