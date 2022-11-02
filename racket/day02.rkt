#lang racket

(require "aoc.rkt")


(define (solve data)
  (for/fold ([x 0]
             [y 0]
             [aim 0])
            ([instr (in-list data)])
    (match-define (list command (app int amount)) (string-split instr))
    (match command
      ["forward" (values (+ x amount) (+ y (* aim amount)) aim)]
      ["down" (values x y (+ aim amount))]
      ["up"  (values x y (- aim amount))])))


(define-values (x y aim) (solve (read-input 2)))
(* x aim)
(* x y)










(module+ test
  (require rackunit)
  (define-values (x y aim) (solve (read-input "02-test")))
  (check-equal? (* x aim) 150)
  (check-equal? (* x y) 900))
