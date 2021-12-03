from aoc import *
from collections import Counter

def most_common(col):
    (first, x), (second, y) = Counter(col).most_common(2)
    if x == y:
        first = '1'
        second = '0'
    return first, second

bin2int = lambda s: int(cat(s), 2)

def get_stuff(data, is_least_common):
    rows = data.copy()
    for i in range(len(rows[0])):
        t = transpose(rows)
        wanted = most_common(t[i])[is_least_common]
        rows = [line for line in rows if line[i] == wanted]
        if len(rows) == 1:
            return bin2int(rows)


def part_1(data):
    l = [most_common(col) for col in transpose(data)]
    l = transpose(l)
    return bin2int(l[0]) * bin2int(l[1])

def part_2(data):
    t = transpose(data)
    g = get_stuff(data, False)
    e = get_stuff(data, True)
    return g * e

data = read_input("03b")

print(part_1(data))
print(part_2(data))
