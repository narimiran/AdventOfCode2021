from aoc import *


def turn(grid):
    flashes = 0
    for k in grid:
        grid[k] += 1
    q = set(grid.keys())
    while q:
        pt = q.pop()
        if grid[pt] > 9:
            flashes += 1
            grid[pt] = 0
            for nb in neighbours(*pt, 8):
                if grid.get(nb):
                    grid[nb] += 1
                    q.add(nb)
    return flashes


octopi = list2grid(map(digits, read_input(11)))
flashed = 0
for step in count_from(1):
    flashed += turn(octopi)
    if step == 100:
        print(flashed)
    if not any(octopi.values()):
        print(step)
        break
