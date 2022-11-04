#lang racket

(require "aoc.rkt" threading)


(struct board (rows
               cols
               [has-won? #:mutable]))

(define (init-board b)
  (board b (transpose b) #f))

(define (make-boards data)
  (cond
    [(empty? data) '()]
    [else (cons (~> data
                    (drop 1)
                    (take 5)
                    (map string-split _)
                    (map (curry map int) _)
                    init-board)
                (make-boards (drop data 6)))]))


(define (is-winner? board drawn)
  (or
    (for/or ([row (in-list (board-rows board))])
      (andmap (curryr member drawn) row))
    (for/or ([col (in-list (board-cols board))])
      (andmap (curryr member drawn) col))))

(define (unmarked-sum board drawn)
  (for*/sum ([row (in-list (board-rows board))]
             [n (in-list row)])
    (if (member n drawn) 0 n)))

(define (solve input)
  (match-define (list numbers boards) input)
  (for*/list ([n (in-range 1 (length numbers))]
              [b (in-list boards)]
               #:when (and (not (board-has-won? b))
                           (is-winner? b (take numbers n))))
    (define drawn (take numbers n))
    (set-board-has-won?! b #t)
    (* (last drawn) (unmarked-sum b drawn))))


(define (parse filename)
  (define data (read-input filename))
  (list
    (~>> (first data)
         (string-split _ ",")
         (map int))
    (make-boards (rest data))))

(define winning-scores (solve (parse 4)))
(first winning-scores)
(last winning-scores)










(module+ test
  (require rackunit)
  (define winning-scores (solve (parse "04-test")))
  (check-equal? (first winning-scores) 4512)
  (check-equal? (last winning-scores) 1924))
