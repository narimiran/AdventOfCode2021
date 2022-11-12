#lang racket

(require "aoc.rkt" threading)


(define (traverse conns can-twice [a "start"] [seen (set "start")])
  (define (is-lowercase? s)
    (andmap char-lower-case? (string->list s)))
  (match a
    ["end" 1]
    [else
     (for/sum ([b (in-list (hash-ref conns a))])
       (cond
         [(is-lowercase? b)
          (cond
            [(not (set-member? seen b))
             (traverse conns can-twice b (set-add seen b))]
            [(and can-twice (not (member b '("start" "end"))))
             (traverse conns #f b seen)]
            [else 0])]
         [else
          (traverse conns can-twice b seen)]))]))


(define (create-connections data)
  (define connections (make-hash))
  (for ([line (in-list data)])
    (match-define (list a b) line)
    (hash-update! connections a (curry cons b) '())
    (hash-update! connections b (curry cons a) '()))
  connections)

(define (parse filename)
  (~>> filename
       read-input
       (map (curryr string-split "-"))
       create-connections))


(define connections (parse 12))

(traverse connections #f)
(traverse connections #t)










(module+ test
  (require rackunit)
  (define small (parse "12-test"))
  (check-equal? (traverse small #f) 10)
  (check-equal? (traverse small #t) 36)

  (define larger (parse "12-test2"))
  (check-equal? (traverse larger #f) 19)
  (check-equal? (traverse larger #t) 103)

  (define even-larger (parse "12-test3"))
  (check-equal? (traverse even-larger #f) 226)
  (check-equal? (traverse even-larger #t) 3509))
