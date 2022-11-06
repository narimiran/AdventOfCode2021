#lang racket

(require "aoc.rkt" threading sugar)


(define (count-fish data)
  (for/hash ([i (in-range 1 6)])
    (values i (count (curry = i) data))))

(define/caching (offsprings day)
  (cond
    [(<= day 0) 1]
    [else (+ (offsprings (- day 7))
             (offsprings (- day 9)))]))

(define (solve data days)
  (for/sum ([(day amount) (in-hash data)])
    (* amount (offsprings (- days day)))))


(define (parse filename)
  (~> (read-input-line filename)
      (string-split ",")
      (map int _)
      count-fish))

(define fish (parse 6))
(solve fish 80)
(solve fish 256)










(module+ test
  (require rackunit)
  (define fish (parse "06-test"))
  (check-equal? (solve fish 18) 26)
  (check-equal? (solve fish 80) 5934)
  (check-equal? (solve fish 256) 26984457539))
