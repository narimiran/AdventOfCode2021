from aoc import *
from collections import Counter


def common_bits(col):
    (x1, v1), (x2, v2) = Counter(col).most_common()
    if v1 == v2:
        x1, x2 = '1', '0'
    return x1, x2

multiply = lambda a, b: bin2int(cat(a)) * bin2int(cat(b))

def part_1(data):
    gamma, epsilon = zip(*(common_bits(col) for col in transpose(data)))
    return multiply(gamma, epsilon)

def filter_out(data, is_least_common):
    filtered = data.copy()
    for i in range(len(data[0])):
        col = transpose(filtered)[i]
        wanted = common_bits(col)[is_least_common]
        filtered = [line for line in filtered if line[i] == wanted]
        if len(filtered) == 1:
            return filtered

def part_2(data):
    oxy = filter_out(data, False)
    co2 = filter_out(data, True)
    return multiply(oxy, co2)


data = read_input(3)

print(part_1(data))
print(part_2(data))
