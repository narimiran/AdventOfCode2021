# Advent of Code 2021

All my Advent of Code repos:

* [AoC 2015 in Nim, Python](https://github.com/narimiran/advent_of_code_2015)
* [AoC 2016 in Python](https://github.com/narimiran/advent_of_code_2016)
* [AoC 2017 in Nim, OCaml, Python](https://github.com/narimiran/AdventOfCode2017)
* [AoC 2018 in Nim, Python, Racket](https://github.com/narimiran/AdventOfCode2018)
* [AoC 2019 in OCaml, Python](https://github.com/narimiran/AdventOfCode2019)
* [AoC 2020 in Nim, one liner-y Python, Racket](https://github.com/narimiran/AdventOfCode2020)
* [AoC 2021 in Python, Racket](https://github.com/narimiran/AdventOfCode2021) (this repo)
* [AoC 2022 in Python, Clojure](https://github.com/narimiran/AdventOfCode2022)


&nbsp;

This year it is back to roots - Python!

I'll probably add solutions in other languages on some later date.

EDIT, November 2022: And I did - I've solved initial days in Racket too.



## Solutions


Task                                                                      | Python Solution              | Racket Solution               | Comment for Python solution
---                                                                       | ---                          | ---                           | ---
Day 00: Helper file                                                       | [aoc.py](python/aoc.py)      | [aoc.rkt](racket/aoc.rkt)     | Utilities to use for solving the tasks.
[Day 01: Sonar Sweep](http://adventofcode.com/2021/day/1)                 | [day01.py](python/day01.py)  | [day01.rkt](racket/day01.rkt) | `(a + b + c) < (b + c + d)` --> `a < d`
[Day 02: Dive!](http://adventofcode.com/2021/day/2)                       | [day02.py](python/day02.py)  | [day02.rkt](racket/day02.rkt) | "Up, up, down, down, left, right, left, right..."
[Day 03: Binary Diagnostic](http://adventofcode.com/2021/day/3)           | [day03.py](python/day03.py)  | [day03.rkt](racket/day03.rkt) | ` from collections import Counter`
[Day 04: Giant Squid](http://adventofcode.com/2021/day/4)                 | [day04.py](python/day04.py)  | [day04.rkt](racket/day04.rkt) | Set operations (`<=`, `-`) to the rescue.
[Day 05: Hydrothermal Venture](http://adventofcode.com/2021/day/5)        | [day05.py](python/day05.py)  | [day05.rkt](racket/day05.rkt) | TIL `defaultdict` is faster than `Counter`.
[Day 06: Lanternfish](http://adventofcode.com/2021/day/6)                 | [day06.py](python/day06.py)  | [day06.rkt](racket/day06.rkt) | Recursion with memoization.
[Day 07: The Treachery of Whales](http://adventofcode.com/2021/day/7)     | [day07.py](python/day07.py)  | [day07.rkt](racket/day07.rkt) | Mean `mean`!
[Day 08: Seven Segment Search](http://adventofcode.com/2021/day/8)        | [day08.py](python/day08.py)  | [day08.rkt](racket/day08.rkt) | Using Python 3.10 and pattern matching.
[Day 09: Smoke Basin](http://adventofcode.com/2021/day/9)                 | [day09.py](python/day09.py)  | [day09.rkt](racket/day09.rkt) | Recursive DFS.
[Day 10: Syntax Scoring](http://adventofcode.com/2021/day/10)             | [day10.py](python/day10.py)  | [day10.rkt](racket/day10.rkt) | `dict(zip(...))` for easier typing
[Day 11: Dumbo Octopus](http://adventofcode.com/2021/day/11)              | [day11.py](python/day11.py)  | [day11.rkt](racket/day11.rkt) | Iterative DFS.
[Day 12: Passage Pathing](http://adventofcode.com/2021/day/12)            | [day12.py](python/day12.py)  | [day12.rkt](racket/day12.rkt) | Recursive DFS generator with `yield from`.
[Day 13: Transparent Origami](http://adventofcode.com/2021/day/13)        | [day13.py](python/day13.py)  | [day13.rkt](racket/day13.rkt) | Fold. Not `fold`.
[Day 14: Extended Polymerization](http://adventofcode.com/2021/day/14)    | [day14.py](python/day14.py)  |                               | `Counter` to the rescue.
[Day 15: Chiton](http://adventofcode.com/2021/day/15)                     | [day15.py](python/day15.py)  |                               | Dijkstra.
[Day 16: Packet Decoder](http://adventofcode.com/2021/day/16)             | [day16.py](python/day16.py)  |                               | `iter` for some elegant stream implementation.
[Day 17: Trick Shot](http://adventofcode.com/2021/day/17)                 | [day17.py](python/day17.py)  |                               | Analytical + brute force.
[Day 18: Snailfish](http://adventofcode.com/2021/day/18)                  | [day18.py](python/day18.py)  |                               | My first ever `eval` in 7 instances of AoC.
[Day 19: Beacon Scanner](http://adventofcode.com/2021/day/19)             | [day19.py](python/day19.py)  |                               |
[Day 20: Trench Map](http://adventofcode.com/2021/day/20)                 | [day20.py](python/day20.py)  |                               | I had 9-neighbours helper ready.
[Day 21: Dirac Dice](http://adventofcode.com/2021/day/21)                 | [day21.py](python/day21.py)  |                               | Lanternfish says hi!
[Day 22: Reactor Reboot](http://adventofcode.com/2021/day/22)             | [day22.py](python/day22.py)  |                               |
[Day 23: Amphipod](http://adventofcode.com/2021/day/23)                   | [day23.py](python/day23.py)  |                               |
[Day 24: Arithmetic Logic Unit](http://adventofcode.com/2021/day/24)      | [day24.py](python/day24.py)  |                               |
[Day 25: Sea Cucumber](http://adventofcode.com/2021/day/25)               | [day25.py](python/day25.py)  |                               |
