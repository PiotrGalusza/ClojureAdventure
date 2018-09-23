(defproject adventure "0.1.0-SNAPSHOT"
  :description "Zork-inspired text based adventure"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]]
  :main ^:skip-aot adventure.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
