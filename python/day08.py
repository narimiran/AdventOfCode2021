from aoc import *


def part_1(data):
    return sum(len(word) in {2, 3, 4, 7}
               for _, b in data
               for word in b)

def part_2(data):
    res = 0
    for a, b in data:
        d = dict()
        for word in sorted(a, key=len):
            word = set(word)
            l = len(word)
            if   l == 2: d[1] = word
            elif l == 3: d[7] = word
            elif l == 4: d[4] = word
            elif l == 7: d[8] = word
            elif l == 5:
                if   len(word & d[7]) == 3: d[3] = word
                elif len(word & d[4]) == 3: d[5] = word
                else:                       d[2] = word
            elif l == 6:
                if len(word & d[7]) == 3:
                    if len(word & d[4]) == 4: d[9] = word
                    else:                     d[0] = word
                else:                         d[6] = word
        digits = (str(k) for digit in b
                         for k, v in d.items()
                         if set(digit) == v)
        res += int(cat(digits))
    return res


data = [mapt(str.split, line.split(' | ')) for line in read_input(8)]

print(part_1(data))
print(part_2(data))