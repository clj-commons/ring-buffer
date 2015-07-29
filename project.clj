(defproject amalloy/ring-buffer "1.1"
  :description "Persistent bounded-size queue implementation in Clojure"
  :url "http://github.com/amalloy/ring-buffer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :plugins [[org.skummet/lein-skummet "0.2.1"]]
  :aot :all
  :profiles {:skummet {:exclusions [[org.clojure/clojure]]
                       :dependencies ^:replace [[com.factual.skummet/clojure "1.7.0-RC1-r4"]]}}

  :aliases {"c" ["do" "clean," "with-profile" "skummet" "skummet" "compile"]})
