from aoc import *
from heapq import heappop, heappush


def traverse(grid):
    start, end = min(grid), max(grid)
    heuristic = lambda x, y: abs(x-y) <= end[0]//3 # stay near the diagonal
    costs = dict()
    q = [(0, start)]
    while q:
        cost, pt = heappop(q)
        if pt == end:
            return cost
        for nb in neighbours(*pt):
            if nc := grid.get(nb):
                c = cost+nc
                if c < costs.get(nb, inf) and heuristic(*nb):
                    costs[nb] = c
                    heappush(q, (c, nb))


def enlarge_grid(grid, size):
    g = dict()
    l = max(grid)[0]+1
    for dy in range(size):
        for dx in range(size):
            for (x, y), val in grid.items():
                v = val+dx+dy
                while v > 9: v -= 9
                g[(x+dx*l, y+dy*l)] = v
    return g


grid_1 = list2grid(map(digits, read_input(15)))
grid_2 = enlarge_grid(grid_1, 5)

print(traverse(grid_1))
print(traverse(grid_2))
