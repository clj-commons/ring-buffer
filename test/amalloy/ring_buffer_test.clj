(ns amalloy.ring-buffer-test
  (:use clojure.test
        amalloy.ring-buffer)
  (:import [java.io
            ByteArrayOutputStream
            ByteArrayInputStream
            ObjectOutputStream
            ObjectInputStream
            ]
           [amalloy.ring_buffer RingBuffer]))

(deftest features
  (let [b (ring-buffer 3)]
    (is (= '(a b) (into b '(a b))))
    (is (= '(c d e) (into b '(a b c d e))))
    (is (= '(d e) (pop (into b '(a b c d e)))))
    (is (= 'c (peek (into b '(a b c d e)))))))

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
      (is (= rb rb2)))))

(deftest test-collection
  ;; Use a ring buffer that has wrapped to ensure we are
  ;; using proper offsets
  (let [^RingBuffer rb (into (ring-buffer 4) [:gone1 :gone2 :one :two :three nil])
        expected [:one :two :three nil]]
    (is (= expected (iterator-seq (.iterator rb))))
    (is (.contains rb :two))
    (is (.contains rb nil))
    (is (not (.contains rb :gone1)))
    (is (.containsAll rb expected))
    (is (not (.containsAll rb [:gone1 :gone2])))
    (is (= 4 (.size rb)))
    (is (.isEmpty (ring-buffer 10)))
    (is (not (.isEmpty rb)))
    (is (= expected (seq (.toArray rb))))
    ))
