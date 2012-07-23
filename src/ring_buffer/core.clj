(ns ring-buffer.core
  (:import (clojure.lang Counted Sequential IPersistentCollection IPersistentStack Reversible IObj)
           (java.io Writer)))

;; If one of our numbers gets over 2 billion, the user's ring buffer is way too large!
;; and count is defined to return an int anyway, so we can't make it work regardless.
;; So we'll just skip that overflow check for a mild speed boost.
(def ^:private old-unchecked-math *unchecked-math*)
(set! *unchecked-math* true)

(deftype RingBuffer [^long start ^long len buf meta]
  Counted
  (count [this] len)

  Sequential ;; tagging interface

  IObj
  (withMeta [this m]
    (RingBuffer. start len buf m))
  (meta [this] meta)

  Object
  (toString [this]
    (pr-str (lazy-seq (seq this))))

  IPersistentStack
  (peek [this]
    (nth buf (rem start (count buf))))
  (pop [this]
    (if (zero? len)
      (throw (IllegalStateException. "Can't pop empty queue"))
      (RingBuffer. (rem (inc start) (count buf)) (dec len) (assoc buf start nil) meta)))
  (empty [this]
    (RingBuffer. 0 0 (vec (repeat (count buf) nil)) meta))
  (equiv [this other]
    (and (sequential? other)
         (or (not (counted? other))
             (= (count this) (count other)))
         (= (seq this) (seq other))))

  IPersistentCollection
  (cons [this x]
    (if (= len (count buf))
      (RingBuffer. (rem (inc start) len) len (assoc buf start x) meta)
      (RingBuffer. start (inc len) (assoc buf (rem (+ start len) (count buf)) x) meta)))
  (seq [this]
    (seq (for [i (range len)]
           (nth buf (rem (+ start i) (count buf)))))))

(defmethod print-method RingBuffer [b ^Writer w]
  (.write w "(")
  (loop [b (seq b)]
    (when-let [[x & xs] b]
      (print-method x w)
      (when xs
        (.write w " ")
        (recur xs))))
  (.write w ")"))

(defn ring-buffer
  "Create an empty ring buffer with the specified [capacity]."
  [capacity]
  (RingBuffer. 0 0 (vec (repeat capacity nil)) nil))

(set! *unchecked-math* old-unchecked-math)
