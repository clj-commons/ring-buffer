(defproject amalloy/ring-buffer "1.1"
  :description "Persistent bounded-size queue implementation in Clojure"
  :url "http://github.com/amalloy/ring-buffer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;:dependencies [[org.clojure/clojure "1.7.0"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :aot :all
  :main amalloy.ring-buffer
  :javac-options ["-target" "1.7" "-source" "1.7"]
  :exclusions [[org.clojure/clojure]]
                       :plugins [[org.skummet/lein-skummet "0.2.1"]]
                       :dependencies ^:replace [[com.factual.skummet/clojure "1.7.0-RC1-r4"]]
  :profiles {:default []}

  :aliases {"c" ["with-profile" "skummet" "do" "clean," "skummet" "compile"]
            "j" ["with-profile" "skummet" "do" "clean," "skummet" "jar"]})


;; remember. skummet compile works, but skummet jar somehow makes shitty classfile