from aoc import *
from itertools import cycle, product
from collections import Counter
from functools import lru_cache


def part_1(p1, p2, sc1=0, sc2=0, cnt=0):
    if sc2 >= 1000:
        return cnt * sc1
    roll_sum = sum(next(dice) for _ in range(3))
    p1 = (p1 + roll_sum) % 10 or 10
    sc1 += p1
    return part_1(p2, p1, sc2, sc1, cnt+3)


@lru_cache(maxsize=None)
def part_2(p1, p2, sc1=0, sc2=0):
    if sc2 >= 21:
        return 0, 1
    w1, w2 = 0, 0
    for roll_sum, freq in dice_freq.items():
        new_p1 = (p1 + roll_sum) % 10 or 10
        new_sc1 = sc1 + new_p1
        p2w, p1w = part_2(p2, new_p1, sc2, new_sc1)
        w1 += freq * p1w
        w2 += freq * p2w
    return w1, w2


p1 = 8
p2 = 3
dice = cycle(range(1, 101))
dice_freq = Counter(sum(p) for p in product((1, 2, 3), repeat=3))

print(part_1(p1, p2))
print(max(part_2(p1, p2)))
