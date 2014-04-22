(defproject mk12/conceal "42"
  :description "A web app that generates word searches"
  :url "http://github.com/mk12/conceal"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :source-paths ["src/clj" "src/cljs"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [org.clojure/clojurescript "0.0-2067"]
                 [prismatic/dommy "0.1.2"]]
  :plugins [[lein-ring "0.8.10"]
            [lein-cljsbuild "1.0.0"]]
  :hooks [leiningen.cljsbuild]
  :ring {:handler conceal.core/handler}
  :cljsbuild
  {:builds [{:source-paths ["src/cljs"]
             :compiler {:output-to "resources/public/js/script.js"
                        :output-dir "resources/public/js"
                        :source-map "resources/public/js/script.js.map"
                        :optimizations :whitespace
                        :pretty-print false}}]})
