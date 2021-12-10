from aoc import *
from math import prod


def calc_basin(x, y, seen=None):
    seen = seen or {(x, y)}
    height = lambda x, y: data[y][x]
    for nb in neighbours(data, x, y):
        if nb not in seen and height(*nb) < 9:
            seen.add(nb)
            calc_basin(*nb, seen)
    return len(seen)


def solve(data):
    risks, basins = zip(*((height+1, calc_basin(x, y))
        for y, line in enumerate(data)
            for x, height in enumerate(line)
                if all(height < data[ny][nx]
                       for nx, ny in neighbours(data, x, y))))
    return sum(risks), prod(sorted(basins, reverse=True)[:3])


data = mapt(digits, read_input(9))
print(solve(data))
