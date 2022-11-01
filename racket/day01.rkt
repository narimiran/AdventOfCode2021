#lang racket

(require "aoc.rkt" algorithms)


(define numbers (read-input 1 'int))

(for ([window '(1 3)])
  (displayln (count values
                    (zip-with <
                              (drop-right numbers window)
                              (drop numbers window)))))
