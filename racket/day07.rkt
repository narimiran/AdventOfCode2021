#lang racket

(require "aoc.rkt" math/statistics)


(define (part-1 nrs)
  (* (length nrs)
     (absdev nrs)))

(define (part-2 nrs)
  (define mid (mean nrs))
  (define (fuel d)
    (/ (* d (add1 d)) 2))
  (apply min
         (for/list ([pivot (in-inclusive-range (floor mid) (ceiling mid))])
           (for/sum ([n (in-list nrs)])
             (fuel (abs (- n pivot)))))))

(define (parse filename)
  (map int (read-input-line filename ",")))

(define numbers (parse 7))
(part-1 numbers)
(part-2 numbers)










(module+ test
  (require rackunit)
  (define numbers (parse "07-test"))
  (check-equal? (part-1 numbers) 37)
  (check-equal? (part-2 numbers) 168))
