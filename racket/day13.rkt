#lang racket

(require "aoc.rkt" threading)


(define (fold-it dots fold-along)
  (match-define (list axis fold-line) fold-along)
  (for/set ([dot (in-set dots)])
    (match-define (list x y) dot)
    (cond
      [(and (eq? axis 'x) (> x fold-line))
       (list (- (* 2 fold-line) x) y)]
      [(and (eq? axis 'y) (> y fold-line))
       (list x (- (* 2 fold-line) y))]
      [else
       (list x y)])))

(define (print-it dots)
  (define (find-upper d fn)
    (~>> d
         set->list
         (map fn)
         (apply max)
         add1))
  (for ([y (in-range (find-upper dots second))])
    (for ([x (in-range (find-upper dots first))])
      (display (if (set-member? dots (list x y)) "█" " ")))
    (displayln "")))


(define (part-1 dots folds)
  (set-count (fold-it dots (first folds))))

(define (part-2 dots folds)
  (for/fold ([acc dots]
             #:result (print-it acc))
            ([fold (in-list folds)])
    (fold-it acc fold)))


(define (parse filename)
  (define (parse-dots dots)
    (for/set ([line (in-list dots)])
      (~> line
          (string-split ",")
          (map int _))))
  (define (parse-folds folds)
    (for/list ([line (in-list (rest folds))])
      (match (string-split line "=")
        [(list "fold along x" n) (list 'x (int n))]
        [(list "fold along y" n) (list 'y (int n))])))
  (define-values (dots folds)
    (~> filename
        read-input
        (splitf-at non-empty-string?)))
  (values (parse-dots dots) (parse-folds folds)))


(define-values (dots folds) (parse 13))

(part-1 dots folds)
(part-2 dots folds)










(module+ test
  (require rackunit)
  (define-values (dots folds) (parse "13-test"))
  (check-equal? (part-1 dots folds) 17))
