# ring-buffer
[![Clojars Project](https://img.shields.io/clojars/v/amalloy/ring-buffer.svg)](https://clojars.org/amalloy/ring-buffer)
[![cljdoc badge](https://cljdoc.org/badge/amalloy/ring-buffer)](https://cljdoc.org/d/amalloy/ring-buffer)
[![Build Status](https://travis-ci.org/clj-commons/ring-buffer.svg)](https://travis-ci.org/clj-commons/ring-buffer)

A persistent collection with semantics roughly equivalent to a ring
buffer: acts like a queue, but has a predetermined maximum capacity;
items added after that capacity is exceeded implicitly eject items
from the front of the queue to make room.

Implements all the relevant Clojure interfaces, but none of the java
interop interfaces; pull requests welcome.

Possible optimization: keep a reference to unused items until some new
item overwrites it. This is easier and faster, but if you have a queue
with very large objects (or a very large, mostly-empty queue), you may
see memory leaks. The performance gain is probably not worth it, but I
suppose that's a tradeoff some may not be willing to make.

## Usage

Add the dependency into your `project.clj`

``` clojure
[amalloy/ring-buffer "1.3.0"]
```
Latest version: [![Clojars Project](https://img.shields.io/clojars/v/amalloy/ring-buffer.svg)](https://clojars.org/amalloy/ring-buffer)

See the [ring-buffer Clojars page](https://clojars.org/amalloy/ring-buffer) for Leiningen and Maven
install snippets.

Then require the namespace:

```clojure
(ns your.ns
 (:require [amalloy.ring-buffer :refer :all]))
```

Finally add and remove items from the ring-buffer

```clojure
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
```

Note of performance characteristics:

  - `conj`, `pop`, `peek`, `nth` are **O(1)** operations
  - `seq`, `rseq`, and all the operation which rely on sequence are
    **O(n)** where *n* is the size of the buffer
  - `into` with a sequence runs in **O(n)** where *n* is the size of
    the input sequence.


## Development

### Running tests

The project uses [lein-doo](http://github.com/bensu/doo) to run cljs
tests. So for example, if `phantomjs` is in the path, the tests can be
run with:

    $ lein doo phantom test

## History

ring-buffer was originally created by [Alan Malloy](https://github.com/amalloy).
In December 2018 it was moved to CLJ Commons for continued maintenance.

It could previously be found at [amalloy/ring-buffer](https://github.com/amalloy/ring-buffer). [clj-commons/ring-buffer](https://github.com/clj-commons/ring-buffer) is the canonical repository now.

## License

Copyright © 2012 Alan Malloy

Distributed under the Eclipse Public License, the same as Clojure.
