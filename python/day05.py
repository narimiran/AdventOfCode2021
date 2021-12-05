from aoc import *
from collections import defaultdict


def solve(data):
    aux = lambda a, b: (abs(b-a), sign(b-a))
    safe = lambda c: sum(v >= 2 for v in c.values())
    c1 = defaultdict(int)
    c2 = defaultdict(int)
    for x1, y1, x2, y2 in data:
        dx, sx = aux(x1, x2)
        dy, sy = aux(y1, y2)
        for i in range(max(dx, dy)+1):
            x = x1 + i*sx
            y = y1 + i*sy
            if dx == 0 or dy == 0:
                c1[(x, y)] += 1
            c2[(x, y)] += 1
    return safe(c1), safe(c2)


data = map(integers, read_input(5))
print(solve(data))