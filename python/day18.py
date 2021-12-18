from aoc import *
from functools import reduce


def add_to(x, n, to_left):
    if isinstance(x, int): return x+n
    if to_left:
        return (add_to(x[0], n, True), x[1])
    else:
        return (x[0], add_to(x[1], n, False))


def explode(x, depth=1):
    if isinstance(x, int):
        return False, 0, x, 0
    a, b = x
    if depth > 4:
        return True, a, 0, b

    exploded, l, xx, r = explode(a, depth+1)
    if exploded:
        return True, l, (xx, add_to(b, r, True)), 0

    exploded, l, xx, r = explode(b, depth+1)
    if exploded:
        return True, 0, (add_to(a, l, False), xx), r

    return False, 0, x, 0


def split(x):
    if isinstance(x, int):
        return (True, (x//2, x-x//2)) if x >= 10 else (False, x)
    a, b = x
    splitted, sp = split(a)
    if splitted:
        return True, (sp, b)
    splitted, sp = split(b)
    return splitted, (a, sp)


def add(a, b):
    x = (a, b)
    while True:
        exploded, _, x, _ = explode(x)
        if exploded:
            continue
        splitted, x = split(x)
        if not splitted:
            return x


def magnitude(x):
    return (x if isinstance(x, int) else
            3*magnitude(x[0]) + 2*magnitude(x[1]))


data = mapl(eval, read_input(18))

print(magnitude(reduce(add, data)))
print(max(magnitude(add(a, b))
          for a in data for b in data if a != b))
