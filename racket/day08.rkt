#lang racket

(require "aoc.rkt" threading algorithms)


(define (part-1 data)
  (for/sum ([line (in-list data)])
    (match-define (list _ output) line)
    (for/sum ([word (in-list output)]
               #:when (member (string-length word) '(2 3 4 7)))
      1)))


(define (decode line)
  (match-define (list patterns output) line)
  (define (compare-lengths a b)
    (< (string-length a) (string-length b)))
  (define (to-letters w)
    (~> w string->list list->set))
  (define d (~>> "abcdefg"
                 string->list
                 list->set
                 (repeat 10)
                 list->vector))
  (for ([word (in-list (sort patterns compare-lengths))])
    (define letters (to-letters word))
    (match (list (set-count letters)
                 (set-count (set-intersect letters (vector-ref d 7)))
                 (set-count (set-intersect letters (vector-ref d 4))))
      [(list 2 _ _) (vector-set! d 1 letters)]
      [(list 3 _ _) (vector-set! d 7 letters)]
      [(list 4 _ _) (vector-set! d 4 letters)]
      [(list 7 _ _) (vector-set! d 8 letters)]
      [(list 5 3 _) (vector-set! d 3 letters)]
      [(list 5 _ 3) (vector-set! d 5 letters)]
      [(list 5 _ _) (vector-set! d 2 letters)]
      [(list 6 3 4) (vector-set! d 9 letters)]
      [(list 6 3 _) (vector-set! d 0 letters)]
      [(list 6 _ _) (vector-set! d 6 letters)]))
  (for*/sum ([(digit n) (in-indexed (reverse output))]
             [(vals i) (in-indexed d)]
             #:when (set=? (to-letters digit) vals))
    (* i (expt 10 n))))

(define (part-2 data)
  (for/sum ([line (in-list data)])
    (decode line)))


(define (parse filename)
  (define (parse-line line)
    (~>> line
         (string-split _ " | ")
         (map string-split)))
  (~>> filename
       read-input
       (map parse-line)))

(define data (parse 8))
(part-1 data)
(part-2 data)










(module+ test
  (require rackunit)
  (define data (parse "08-test"))
  (check-equal? (part-1 data) 26)
  (check-equal? (part-2 data) 61229))
