from aoc import *


class Board:
    def __init__(self, board):
        board = mapl(integers, board.splitlines())
        self.rows = mapt(set, board)
        self.cols = mapt(set, zip(*board))
        self.won = False

    def is_winner(self, drawn):
        return any(any(line <= drawn for line in direction)
                   for direction in (self.rows, self.cols))

    def unmarked_sum(self, drawn):
        return sum(sum(col - drawn) for col in self.cols)


def parse_input(task):
    numbers, *boards = read_input(task, sep="\n\n")
    yield integers(numbers)
    yield [Board(board) for board in boards]


def solve(numbers, boards):
    drawn = set()
    scores = []
    for n in numbers:
        drawn.add(n)
        for board in boards:
            if not board.won and board.is_winner(drawn):
                scores.append(board.unmarked_sum(drawn) * n)
                board.won = True
        if len(scores) == len(boards):
            return scores


winners = solve(*parse_input(4))
print(winners[0], winners[-1])
