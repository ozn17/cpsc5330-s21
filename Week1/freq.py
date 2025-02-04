import sys

# Find if input is being given through stdin or from a file
lines = None
if len(sys.argv) == 1:
    lines = sys.stdin
else:
    lines = open(sys.argv[1])

D = {}
for line in lines:
    for word in line.split():
        word = ''.join(list(filter(
            lambda ch: ch not in "`~!@#$%^&*()_-+={}[]\\|:;\"'<>,.?/",
            word)))
        word = word.lower()
        if word in D:
            D[word] += 1
        else:
            D[word] = 1

for word in sorted(D, key=D.get, reverse=True):
    print(word + ' ' + str(D[word]))
