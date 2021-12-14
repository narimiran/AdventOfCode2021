from aoc import read_input
from collections import Counter


def insertion(pairs, chars):
    p = Counter()
    for (a, b), times in pairs.items():
        mid = rules[a+b]
        p[a+mid] += times
        p[mid+b] += times
        chars[mid] += times
    return p


def solve(poly, steps):
    pairs = Counter(a+b for a, b in zip(poly, poly[1:]))
    chars = Counter(poly)
    for _ in range(steps):
        pairs = insertion(pairs, chars)
    return max(chars.values()) - min(chars.values())


poly, _, *rules = read_input(14)
rules = dict(r.split(' -> ') for r in rules)

print(solve(poly, 10))
print(solve(poly, 40))
