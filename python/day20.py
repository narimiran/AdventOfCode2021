from aoc import *


def expand(grid, n):
    sentinel = '.' if n%2 else '#'
    new_grid = dict()
    for x in range(0-n, 100+n):
        for y in range(0-n, 100+n):
            tot = sum(2**(8-exp)
                      for exp, nb in enumerate(neighbours(x, y, 9))
                      if grid.get(nb, sentinel) == '#')
            new_grid[(x, y)] = algo[tot]
    return new_grid


def solve(grid, iters):
    for n in range(iters):
        grid = expand(grid, n+1)
    return sum(x == '#' for x in grid.values())


algo, image = read_input(20, sep="\n\n")
grid = list2grid(image.splitlines())

print(solve(grid, 2))
print(solve(grid, 50))
