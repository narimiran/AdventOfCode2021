#!~/miniconda3/envs/py310/bin/python


from aoc import *


def part_1(data):
    return sum(len(word) in {2, 3, 4, 7}
               for _, b in data
               for word in b)

def part_2(data):
    res = 0
    for a, b in data:
        d = {n: set('abcdefg') for n in range(10)}
        for word in sorted(a, key=len):
            letters = set(word)
            match (len(letters), len(letters & d[7]), len(letters & d[4])):
                case (2, _, _): d[1] = letters
                case (3, _, _): d[7] = letters
                case (4, _, _): d[4] = letters
                case (7, _, _): d[8] = letters
                case (5, 3, _): d[3] = letters
                case (5, _, 3): d[5] = letters
                case (5, _, _): d[2] = letters
                case (6, 3, 4): d[9] = letters
                case (6, 3, _): d[0] = letters
                case (6, _, _): d[6] = letters
        digits = (str(k) for digit in b
                         for k, v in d.items()
                         if set(digit) == v)
        res += int(cat(digits))
    return res


data = [mapt(str.split, line.split(' | ')) for line in read_input(8)]

print(part_1(data))
print(part_2(data))