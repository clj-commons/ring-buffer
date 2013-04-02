(ns amalloy.ring-buffer-test
  (:use clojure.test
        amalloy.ring-buffer))

(deftest features
  (let [b (ring-buffer 3)]
    (is (= '(a b) (into b '(a b))))
    (is (= '(c d e) (into b '(a b c d e))))
    (is (= '(d e) (pop (into b '(a b c d e)))))
    (is (= 'c (peek (into b '(a b c d e)))))))
