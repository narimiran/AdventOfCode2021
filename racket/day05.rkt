#lang racket

(require "aoc.rkt" threading)


(struct point (x y) #:transparent)

(define (points->lines pts)
  (define (aux a b)
    (values (abs (- b a)) (sgn (- b a))))
  (match-define (list x1 y1 x2 y2) pts)
  (define-values (dx sx) (aux x1 x2))
  (define-values (dy sy) (aux y1 y2))
  (list x1 sx y1 sy (max dx dy)))

(define (solve instrs part)
  (define c (make-hash))
  (for ([pts (in-list instrs)])
    (match-define (list x sx y sy dist) pts)
    (for ([i (in-inclusive-range 0 dist)])
      (define pt (point (+ x (* i sx))
                        (+ y (* i sy))))
      (cond [(match part
               [1 (or (= sx 0) (= sy 0))]
               [2 #t])
             (hash-update! c pt add1 0)])))
  (count (curryr >= 2) (hash-values c)))


(define (parse filename)
  (define (parse-line line)
    (~>> line
         (regexp-match #px"^(\\d+),(\\d+) -> (\\d+),(\\d+)$")
         rest
         (map int)
         (points->lines)))
  (map parse-line (read-input filename)))


(define data (parse 5))
(solve data 1)
(solve data 2)










(module+ test
  (require rackunit)
  (define data (parse "05-test"))
  (check-equal? (solve data 1) 5)
  (check-equal? (solve data 2) 12))
