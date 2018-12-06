(defproject amalloy/ring-buffer "1.2.2"
  :description "Persistent bounded-size queue implementation in Clojure"
  :url "https://github.com/clj-commons/ring-buffer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-doo "0.1.11"]]
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src" "test"]
                        :compiler     {:output-to     "target/testable.js"
                                       :main          amalloy.runner
                                       :target :nodejs
                                       :optimizations :none}}]})
