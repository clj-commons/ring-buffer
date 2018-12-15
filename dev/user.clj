(ns user
  (:require [amalloy.ring-buffer :refer :all]))

;; create a ring-buffer of given capacity
(ring-buffer 3)
;; => #amalloy/ring-buffer [3 ()]

;; add one item to a empty ring-buffer
(conj (ring-buffer 3) 'a)
;; => #amalloy/ring-buffer [3 (a)]

;; add multiple items at once
(into (ring-buffer 3) '(a b))
;; => #amalloy/ring-buffer [3 (a b)]

;; if you go beyond the ring-buffer capacity
;; the oldest elements are evicted
(into (ring-buffer 3) '(a b c d e))
;; => #amalloy/ring-buffer [3 (c d e)]

;; remove the first element from the buffer
(pop (into (ring-buffer 3) '(a b c d e)))
;; => #amalloy/ring-buffer [3 (d e)]

;; retrieve the first element from the buffer
(peek (into (ring-buffer 3) '(a b c d e)))
;; => c

;; alternatively use `first` and `last`
(first (into (ring-buffer 3) '(a b c d e)))
;; => c
(last (into (ring-buffer 3) '(a b c d e)))
;; => e

;; Lookup a specific index with `nth`
(nth (into (ring-buffer 3) '(a b c)) 1)
;; => b

;; `nth` suports negative indices to lookup
;; from the end (`-1` -> last element)
;; and it also wraps around
(nth (into (ring-buffer 3) '(a b c)) -1)
;; => c
(nth (into (ring-buffer 3) '(a b c)) 7)
;; => b

;; `nth` suports default value which is used
;; when element isn't present or instead of
;; wrapping around
(nth (into (ring-buffer 3) '(a b c)) 7 'z)
;; => z
(nth (into (ring-buffer 3) '(a )) 2 'z)
;; => z

;; to obtain the content of the buffer
;; in insertion order (FIFO) use `seq`.
;; use `rseq` to obtain the content in
;; reverse order (LIFO).
(seq (into (ring-buffer 3) '(a b c d e)))
;; => (c d e)
(rseq (into (ring-buffer 3) '(a b c d e)))
;; => (e d c)
