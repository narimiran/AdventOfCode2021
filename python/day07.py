from aoc import *
from statistics import median, mean
from math import floor, ceil


def solve(data, mid, func):
    m = mid(data)
    return min(sum(func(abs(n-p)) for n in data)
               for p in (floor(m), ceil(m)))


data = read_input(7, int, ',')
fuel = lambda d: d*(d+1) // 2

print(solve(data, median, int))
print(solve(data, mean, fuel))