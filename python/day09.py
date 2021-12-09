from aoc import *
from math import prod


def neighbors(x, y):
    adjacent = lambda x, y: ((x-1, y), (x, y-1), (x, y+1), (x+1, y))
    in_bounds = lambda x, y: 0 <= x < w and 0 <= y < h
    yield from (pt for pt in adjacent(x, y) if in_bounds(*pt))


def calc_basin(x, y, seen=None):
    seen = seen or {(x, y)}
    height = lambda x, y: data[y][x]
    for nb in neighbors(x, y):
        if nb not in seen and height(*nb) < 9:
            seen.add(nb)
            calc_basin(*nb, seen)
    return len(seen)


def solve(data):
    risks, basins = zip(*((height+1, calc_basin(x, y))
        for y, line in enumerate(data)
            for x, height in enumerate(line)
                if all(height < data[ny][nx]
                       for nx, ny in neighbors(x, y))))
    return sum(risks), prod(sorted(basins, reverse=True)[:3])


data = mapt(digits, read_input(9))
w, h = len(data[0]), len(data)

print(solve(data))