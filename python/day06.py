from aoc import read_input
from collections import Counter
from functools import lru_cache


@lru_cache
def solve(day):
    return (1 if day <= 0 else
            solve(day-7) + solve(day-9))


data = Counter(read_input(6, int, sep=','))
for days in (80, 256):
    print(sum(amount * solve(days-day)
              for day, amount in data.items()))