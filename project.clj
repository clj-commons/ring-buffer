(defproject amalloy/ring-buffer "1.2"
  :description "Persistent bounded-size queue implementation in Clojure"
  :url "http://github.com/amalloy/ring-buffer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]]
  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-doo "0.1.4"]]

  :cljsbuild {
    :builds {
      :test {:source-paths ["src" "test"]
             :compiler {:output-to "target/testable.js"
                        :main 'amalloy.runner
                        :optimizations :none}}}})
