(ns amalloy.ring-buffer
  (:import (clojure.lang Counted
                         Sequential
                         IPersistentCollection
                         IPersistentStack
                         Reversible
                         IObj)
           (java.io Writer Serializable)
           (java.util Collection)
           (amalloy Dummy Dummy$Holder))
  (:gen-class))

;; If one of our numbers gets over 2 billion, the user's ring buffer is way too large!
;; and count is defined to return an int anyway, so we can't make it work regardless.
;; So we'll just skip that overflow check for a mild speed boost.
(def ^:private old-unchecked-math *unchecked-math*)
(set! *unchecked-math* true)

(deftype RingBuffer [^long start ^long len buf meta]
  Serializable

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
           (nth buf (rem (+ start i) (count buf))))))

  Collection
  (iterator [this]
    (.iterator ^Iterable (sequence this)))
  (contains [this e]
    (boolean (some #(= e %) (.seq this))))
  (containsAll [this elts]
    (every? #(.contains this %) elts))
  (size [this]
    (.count this))
  (isEmpty [this]
    (empty? this))
  (toArray [this dest]
    (reduce (fn [idx item]
              (aset dest idx item)
              (inc idx))
            0, this)
    dest)
  (toArray [this]
    (.toArray this (object-array (.count this)))))

(defn- ^:strict add [^long x ^long y]
  (unchecked-inc
   (unchecked-inc
    (unchecked-add x
                   (unchecked-subtract Long/MIN_VALUE y)))))

(defn- ^:strict do-print [b w]
  (let [^RingBuffer b b, ^Writer w w]
    (.containsAll b b)
    (.containsAll b (identity b))
    (.write w "#amalloy/ring-buffer ")
    (.write w "")
    (print-method [(.len b) (sequence b)] w)))

(defmethod print-method RingBuffer [b w]
  (do-print b w))

(defn- read-method [[len items]]
  (RingBuffer. 0 len (vec (take len (concat items (repeat nil)))) nil))

(defn ring-buffer
  "Create an empty ring buffer with the specified [capacity]."
  [capacity]
  (RingBuffer. 0 0 (vec (repeat capacity nil)) nil))

(defn ^:strict test-field-setting []
  (let [^Dummy d (Dummy. (Dummy$Holder. "old"))]
    (set! (.s (.h d)) "new")
    (prn (.s (.h d))))

  (let [^Dummy d (identity (Dummy. (Dummy$Holder. "old")))
        ^Dummy$Holder h (Dummy$Holder. "help")]
    (set! (.h ^Dummy d) h)
    nil))

(defn -main []
  (test-field-setting)
  (prn (ring-buffer 5)))


(set! *unchecked-math* old-unchecked-math)
