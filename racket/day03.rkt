#lang racket

(require "aoc.rkt" threading)


(define (split-out data col)
  (partition
    (λ (row) (char=? #\1 (list-ref row col)))
    data))

(define (compute nrs)
  (for/sum ([(n ex) (in-indexed nrs)])
    (* n (expt 2 ex))))

(define (part-1 data)
  (define (opposite val)
    (- 1 val))
  (define (most-common-bit pos data)
    (define-values (ones zeros) (split-out data pos))
    (if (> (length zeros) (length ones)) 0 1))
  (for/fold ([gamma '()]
             #:result (* (compute gamma) (compute (map opposite gamma))))
            ([col (in-range (length (first data)))])
    (cons (most-common-bit col data) gamma)))


(define (filter-out most-common? data)
  (for/fold ([acc data]
              #:result (first acc))
            ([col (in-range (length (first data)))]
             #:break (= 1 (length acc)))
    (define-values (ones zeros) (split-out acc col))
    (define more-zeros? (> (length zeros) (length ones)))
    (if (xor most-common? more-zeros?) ones zeros)))

(define (part-2 data)
  (for/fold ([acc 1])
            ([most-common? '(#t #f)])
    (~>> data
         (filter-out most-common?)
         (map (λ (c) (if (char=? #\1 c) 1 0)))
         reverse
         compute
         (* acc))))


(define numbers (read-input 3 'list))
(part-1 numbers)
(part-2 numbers)











(module+ test
  (require rackunit)
  (define numbers (read-input "03-test" 'list))
  (check-equal? (part-1 numbers) 198)
  (check-equal? (part-2 numbers) 230))
