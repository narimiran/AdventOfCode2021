from aoc import *
from statistics import median


left, right = '([{<', ')]}>'


def parse_line(line):
    stack = []
    chunks = dict(zip(left, right))
    scores = dict(zip(right, (3, 57, 1197, 25137)))
    for c in line:
        if c in left: stack.append(chunks[c])
        elif c == stack[-1]: stack.pop()
        else: return scores[c], stack
    return 0, stack


def solve(data):
    total = 0
    totals = []
    scores = dict(zip(right, range(1, 5)))
    for line in data:
        score, stack = parse_line(line)
        if score: total += score
        else:
            while stack:
                score = 5*score + scores[stack.pop()]
            totals.append(score)
    return total, median(totals)


data = read_input(10)
print(solve(data))
