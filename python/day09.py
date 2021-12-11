from aoc import *
from math import prod


def calc_basin(pt, seen):
    for nb in neighbours(*pt):
        if nb not in seen and grid.get(nb, 9) < 9:
            seen.add(nb)
            calc_basin(nb, seen)
    return len(seen)


def solve(grid):
    risks, basins = zip(*((height+1, calc_basin(pt, {pt}))
        for pt, height in grid.items()
            if all(height < grid.get(nb, 9)
                   for nb in neighbours(*pt))))
    return sum(risks), prod(sorted(basins, reverse=True)[:3])


grid = list2grid(map(digits, read_input(9)))
print(solve(grid))
