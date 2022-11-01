#lang racket


(provide read-input read-input-line int str cat
         string->digits string->vector list->grid
         transpose neighbours)

(require racket/format racket/runtime-path threading)

(define-runtime-path inputs "../inputs")

(define (read-input filename [datatype 'string])
  (~>> filename
       (~a _ #:align 'right #:min-width 2 #:pad-string "0")
       (format "~a.txt")
       (build-path inputs)
       file->lines
       ((match datatype
          ['int (curry map string->number)]
          ['list (curry map string->list)]
          ['string values]))))

(define (read-input-line filename [sep #f])
  (~>> filename
      (read-input _ 'string)
      first
      ((match sep
        [#f identity]
        [else (curryr string-split sep)]))))

(define int string->number)
(define str number->string)
(define cat list->string)

(define (string->digits s)
  (~>> s
       string->list
       (map string)
       (map int)))

(define (string->vector s)
  (~>> s
       string->list
       list->vector))


(define (transpose matrix)
  (match (first matrix)
    ['() '()]
    [_ (cons (map first matrix)
             (transpose (map rest matrix)))]))

(module+ test
  (require rackunit)
  (check-equal? (transpose '((1 2) (3 4)))
                '((1 3) (2 4)))
  (check-equal? (transpose '((1 2 3) (4 5 6)))
                '((1 4) (2 5) (3 6))))



(define (neighbours pt [amount 4])
  (for*/list ([dx '(-1 0 1)]
              [dy '(-1 0 1)]
              #:when (or (and (= amount 4) (not (= (abs dx) (abs dy))))
                         (and (= amount 8) (not (and (= dx 0) (= dy 0))))
                         (= amount 9)))
    (+ pt (make-rectangular dx dy))))

(module+ test
  (check-equal? (neighbours 2+3i) '(1+3i 2+2i 2+4i 3+3i))
  (check-equal? (neighbours 2+3i 8) '(1+2i 1+3i 1+4i 2+2i 2+4i 3+2i 3+3i 3+4i))
  (check-equal? (neighbours 2+3i 9) '(1+2i 1+3i 1+4i 2+2i 2+3i 2+4i 3+2i 3+3i 3+4i)))



(define (list->grid lines)
  (for*/hash ([(line y) (in-indexed lines)]
               [(val x) (in-indexed line)])
    (values (make-rectangular x y) val)))

(module+ test
  (check-equal? (list->grid '((5 6) (7 8)))
                '#hash((0    . 5) (1    . 6)
                       (0+1i . 7) (1+1i . 8))))
