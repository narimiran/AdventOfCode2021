#lang racket

(require "aoc.rkt" threading)


(define (find-lowest-points heightmap)
  (for/list ([(pt val) (in-hash heightmap)]
             #:when (for/and ([nb (in-list (neighbours pt))])
                      (< val (hash-ref heightmap nb 9))))
    pt))


(define (part-1 heightmap)
  (for/sum ([pt (find-lowest-points heightmap)])
    (add1 (hash-ref heightmap pt))))

(define (part-2 heightmap)
  (define (calc-basin pt seen)
    (for ([nb (neighbours pt)]
          #:when (and (not (set-member? seen nb))
                      (> 9 (hash-ref heightmap nb 9))))
      (set-add! seen nb)
      (calc-basin nb seen))
    (set-count seen))
  (~> (for/list ([pt (find-lowest-points heightmap)])
        (calc-basin pt (mutable-set pt)))
      (sort >)
      (take 3)
      (apply * _)))


(define (parse filename)
  (~>> filename
       read-input
       (map string->digits)
       list->grid))


(define heightmap (parse 9))
(part-1 heightmap)
(part-2 heightmap)










(module+ test
  (require rackunit)
  (define heightmap (parse "09-test"))
  (check-equal? (part-1 heightmap) 15)
  (check-equal? (part-2 heightmap) 1134))
