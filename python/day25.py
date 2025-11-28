from aoc import *


def turn(grid):
    new_grid = [['.' for _ in range(w)] for _ in range(h)]
    moved = False
    for y, line in enumerate(grid):
        for x, c in enumerate(line):
            if c == '>':
                if line[(x+1)%w] == '.':
                    moved = True
                    new_grid[y][(x+1)%w] = '>'
                else:
                    new_grid[y][x] = '>'
    for y, line in enumerate(grid):
        for x, c in enumerate(line):
            if c == 'v':
                if grid[(y+1)%h][x] != 'v' and new_grid[(y+1)%h][x] == '.':
                    moved = True
                    new_grid[(y+1)%h][x] = 'v'
                else:
                    new_grid[y][x] = 'v' 
    return new_grid, moved


def solve(grid):
    moved = True
    cnt = 0
    while moved:
        cnt += 1
        grid, moved = turn(grid)
    return cnt

data = read_input(25, list)
w = len(data[0])
h = len(data)

print(solve(data))
