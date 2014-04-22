;;; Copyright 2014 Mitchell Kember. Subject to the MIT License.

(ns conceal.connect
  (:require [clojure.browser.repl :as repl]))

(repl/connect "http://localhost:9000/repl")
