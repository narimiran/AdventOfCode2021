#lang racket

(require "aoc.rkt" threading math/statistics)


(define left (string->vector "([{<"))
(define right (string->vector ")]}>"))

(define (parse-line line)
  (define b #f)
  (for/fold ([stack '()]
              #:result (cons b stack))
            ([c (in-list line)]
              #:break b)
    (define idx (vector-member c left))
    (cond
      [(number? idx) (cons (vector-ref right idx) stack)]
      [(char=? c (first stack)) (rest stack)]
      [else (begin
              (set! b #t)
              (cons c stack))])))


(define (part-1 data)
  (define/match (score c)
    [(#\)) 3]
    [(#\]) 57]
    [(#\}) 1197]
    [(#\>) 25137])
  (~>> data
       (filter first)
       (map second)
       (map score)
       (apply +)))

(define (part-2 data)
  (define (score stack)
    (for/fold ([acc 0])
      ([c (in-list stack)])
      (+ (* 5 acc)
         (case c
           [(#\)) 1]
           [(#\]) 2]
           [(#\}) 3]
           [(#\>) 4]))))
  (~>> data
       (filter-not first)
       (map rest)
       (map score)
       (median <)))


(define data (read-input 10 'list))
(define remainings (map parse-line data))
(part-1 remainings)
(part-2 remainings)










(module+ test
  (require rackunit)
  (define data (read-input "10-test" 'list))
  (define remainings (map parse-line data))
  (check-equal? (part-1 remainings) 26397)
  (check-equal? (part-2 remainings) 288957))
