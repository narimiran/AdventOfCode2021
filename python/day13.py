from aoc import *


def fold_it(dots, axis, fold):
    return {(2*fold-x, y) if axis == 'x' and x > fold else
            (x, 2*fold-y) if axis == 'y' and y > fold else
            (x, y)
            for x, y in dots}


def part_2(dots, folds):
    for axis, fold in folds:
        dots = fold_it(dots, axis, fold)
    print_2d(grid2list(dots))


dots, folds = map(str.splitlines, read_input(13, sep="\n\n"))
dots = mapl(integers, dots)
folds = [(line[11], int(line[13:])) for line in folds]

print(len(fold_it(dots, *folds[0])))
part_2(dots, folds)
