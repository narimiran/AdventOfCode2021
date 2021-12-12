from aoc import read_input
from collections import defaultdict


def traverse(can_twice, a='start', seen={'start'}):
    if a == 'end': yield 1
    else:
        for b in connections[a]:
            if b.islower():
                if b not in seen:
                    yield from traverse(can_twice, b, seen | {b})
                elif can_twice and b not in {'start', 'end'}:
                    yield from traverse(False, b, seen)
            else:
                yield from traverse(can_twice, b, seen)


connections = defaultdict(list)
for line in read_input(12):
    a, b = line.split('-')
    connections[a].append(b)
    connections[b].append(a)

print(sum(traverse(can_twice=False)))
print(sum(traverse(can_twice=True)))
