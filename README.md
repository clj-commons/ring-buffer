# ring-buffer

[![Build Status](https://travis-ci.org/clj-commons/ring-buffer.svg)](https://travis-ci.org/clj-commons/ring-buffer)

A persistent collection with semantics roughly equivalent to a ring buffer: acts like a queue, but
has a predetermined maximum capacity; items added after that capacity is exceeded implicitly eject
items from the front of the queue to make room.

Implements all the relevant Clojure interfaces, but none of the java interop interfaces; pull
requests welcome.

Possible optimization: keep a reference to unused items until some new item overwrites it. This is
easier and faster, but if you have a queue with very large objects (or a very large, mostly-empty
queue), you may see memory leaks. The performance gain is probably not worth it, but I suppose
that's a tradeoff some may not be willing to make.

## Usage

```clojure
amalloy.ring-buffer> (into (ring-buffer 3) '(a b))
(a b)
amalloy.ring-buffer> (into (ring-buffer 3) '(a b c d e))
(c d e)
amalloy.ring-buffer> (pop (into (ring-buffer 3) '(a b c d e)))
(d e)
amalloy.ring-buffer> (peek (into (ring-buffer 3) '(a b c d e)))
c
```

## Installation

See the [ring-buffer Clojars page](https://clojars.org/amalloy/ring-buffer) for Leiningen and Maven
install snippets.

## Development

### Running tests

The project uses [lein-doo](http://github.com/bensu/doo) to run cljs tests. So for example, if `phantomjs` is in the path, the tests can be run with:

    $ lein doo phantom test

## History

ring-buffer was originally created by [Alan Malloy](https://github.com/amalloy). In December 2018 it was moved to CLJ Commons for continued maintenance.

It could previously be found at [amalloy/ring-buffer](https://github.com/amalloy/ring-buffer). [clj-commons/ring-buffer](https://github.com/clj-commons/ring-buffer) is the canonical repository now.

## License

Copyright © 2012 Alan Malloy

Distributed under the Eclipse Public License, the same as Clojure.
