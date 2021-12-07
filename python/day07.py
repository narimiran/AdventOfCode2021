from aoc import *
from statistics import median, mean


def part_1(data):
    m = int(median(data))
    return sum(abs(n-m) for n in data)

def part_2(data):
    fuel = lambda d: d*(d+1) // 2
    m = int(mean(data))
    return min(sum(fuel(abs(n-p)) for n in data)
               for p in range(m-1, m+2))


data = read_input(7, int, ',')

print(part_1(data))
print(part_2(data))