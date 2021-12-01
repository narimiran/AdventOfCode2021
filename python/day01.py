from aoc import read_input


data = read_input(1, int)
for window in (1, 3):
    print(sum(a < b for a, b in zip(data, data[window:])))
