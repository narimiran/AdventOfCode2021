from aoc import *
from math import prod


neighbors = lambda x, y: ((x-1, y), (x, y-1), (x, y+1), (x+1, y))
is_inside = lambda x, y: 0 <= x < w and 0 <= y < h


def calc_basin(x, y, seen=None):
    seen = seen or {(x, y)}
    for nx, ny in neighbors(x, y):
        if (nx, ny) not in seen and is_inside(nx, ny) and data[ny][nx] < 9:
            seen.add((nx, ny))
            calc_basin(nx, ny, seen)
    return len(seen)


def solve(data):
    risks, basins = zip(*((height+1, calc_basin(x, y))
        for y, line in enumerate(data)
            for x, height in enumerate(line)
                if all(height < data[ny][nx]
                       for nx, ny in neighbors(x, y)
                       if is_inside(nx, ny))))
    return sum(risks), prod(sorted(basins, reverse=True)[:3])


data = mapt(digits, read_input(9))
w, h = len(data[0]), len(data)

print(solve(data))