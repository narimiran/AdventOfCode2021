#lang racket


(require "aoc.rkt" threading)


(define (step! octopi pt)
  (define val (hash-ref octopi pt 99))
  (when (< val 10)
    (hash-update! octopi pt add1)
    (when (= val 9)
      (for-each (curry step! octopi) (neighbours pt 8)))))

(define (count-flashes octopi)
  (for/sum ([(pt val) (in-hash octopi)]
             #:when (= val 10))
    (hash-set! octopi pt 0)
    1))


(define (part-1 octopi)
  (for/sum ([n (in-range 100)])
    (for-each (curry step! octopi) (hash-keys octopi))
    (count-flashes octopi)))

(define (part-2 octopi)
  (for/or ([n (in-naturals 101)])
    (for-each (curry step! octopi) (hash-keys octopi))
    (count-flashes octopi)
    (if (andmap zero? (hash-values octopi))
        n
        #f)))


(define (make-grid lines)
  (define octopi (make-hash))
  (for* ([(line y) (in-indexed lines)]
         [(val x) (in-indexed line)])
    (hash-set! octopi (make-rectangular x y) val))
  octopi)

(define (parse filename)
  (~>> filename
       read-input
       (map string->digits)
       make-grid))


(define octopi (parse 11))

(part-1 octopi)
(part-2 octopi)










(module+ test
  (require rackunit)
  (define octopi (parse "11-test"))
  (check-equal? (part-1 octopi) 1656)
  (check-equal? (part-2 octopi) 195))
