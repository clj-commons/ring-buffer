(ns amalloy.ring-buffer-test
  (:use clojure.test
        amalloy.ring-buffer)
  (:import [java.io
            ByteArrayOutputStream
            ByteArrayInputStream
            ObjectOutputStream
            ObjectInputStream
            ]))

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
