from aoc import read_input_line, cat
from operator import add, mul, lt, gt, eq
from functools import reduce


class Stream:
    def __init__(self, hex):
        self.bin = cat(f"{int(x, 16):04b}" for x in hex)
        self.stream = iter(self.bin)
        self.consumed_bytes = 0
        self.version_sum = 0
        self.value = 0

    def read_next(self, n=1):
        self.consumed_bytes += n
        bits = (next(self.stream) for _ in range(n))
        return int(cat(bits), 2)

    def parse(self):
        v = self.read_next(3)
        t = self.read_next(3)
        self.version_sum += v

        if t == 4:
            self.value = 0
            while True:
                prefix = self.read_next()
                self.value *= 16
                self.value += self.read_next(4)
                if prefix == 0: break
            return self.value
        else:
            subs = []
            mode = self.read_next()
            if mode == 0:
                read_until = self.read_next(15) + self.consumed_bytes
                while self.consumed_bytes < read_until:
                    subs.append(self.parse())
            else:
                sub_num = self.read_next(11)
                for _ in range(sub_num):
                    subs.append(self.parse())

            funcs = [add, mul, min, max, int, gt, lt, eq]
            self.value = reduce(funcs[t], subs)
            return self.value


s = Stream(read_input_line(16))
s.parse()
print(s.version_sum)
print(s.value)
