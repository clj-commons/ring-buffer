(ns amalloy.ring-buffer-test
  (:require #?(:clj  [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is]])
            [amalloy.ring-buffer #?@(:clj [:refer :all]
                                     :cljs [:refer [ring-buffer]])])
  #?(:clj
     (:import [java.io
               ByteArrayOutputStream
               ByteArrayInputStream
               ObjectOutputStream
               ObjectInputStream]
              [amalloy.ring_buffer RingBuffer])))

(deftest features
  (let [b (ring-buffer 3)]
    (is (= '(a b) (into b '(a b))))
    (is (= '(c d e) (into b '(a b c d e))))
    (is (= '(d e) (pop (into b '(a b c d e)))))
    (is (= 'c (peek (into b '(a b c d e)))))))

#?(:clj
(deftest test-serializable
  (let [^ByteArrayOutputStream baos (ByteArrayOutputStream.)
        ^ObjectOutputStream ois (ObjectOutputStream. baos)
        rb (-> (ring-buffer 5)
               (conj 1 2 3 4 5)
               (conj "one" "two" "three"))]
    (.writeObject ois rb)
    (let [^ObjectInputStream ois (-> baos
                                     .toByteArray
                                     ByteArrayInputStream.
                                     ObjectInputStream.)
          rb2 (.readObject ois)]
      (is (= rb rb2))))))

(deftest test-collection
  ;; Use a ring buffer that has wrapped to ensure we are
  ;; using proper offsets
  (let [rb (into (ring-buffer 4) [:gone1 :gone2 :one :two :three nil])
        expected [:one :two :three nil]]
    #?(:clj (is (= expected (iterator-seq (.iterator rb)))))
    (is (some #(= :two %) rb))
    (is (some #(= nil %) rb))
    (is (not (first (filter #(= :gone1 %) rb))))
    (is (= (set rb) (set expected)))
    (is (not (= (set rb) (set [:gone1 :gone2]))))
    (is (= 4 (count rb)))
    (is (empty? (ring-buffer 10)))
    (is (not (empty? rb)))
    (is (= (reverse expected) (rseq rb)))
    (is (= expected (seq (to-array rb))))))
