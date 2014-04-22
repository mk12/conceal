;;; Copyright 2014 Mitchell Kember. Subject to the MIT License.

(ns conceal.core
  (:require [dommy.core :as dom]
            [conceal.word :as word]
            [clojure.string :refer [split]])
  (:use-macros [dommy.macros :only [node sel1]]))

(defn parse-number
  "Parses an input element's value as an integer. If successful, removes the
  error class from the element and returns the integer; otherwise, adds the
  error class to the element and returns false."
  [input]
  (let [n (js/parseInt (dom/value input))]
    (if (integer? n)
      (do (dom/remove-class! input :error)
          n)
      (do (dom/add-class! input :error)
          false))))

(defn ^:export submit
  "Validates the form, marking invalid fields with the error class. If all is
  valid, makes a random word search and adds it to the DOM."
  []
  (let [rows (parse-number (sel1 :#rows))
        cols (parse-number (sel1 :#cols))]
    (if (and rows cols)
      (let [string (dom/value (sel1 :#words))
            words (split string " ")
            grid (word/make-wordsearch rows cols 100 words)
            n (node (word/grid->dom grid))]
        (dom/replace! (sel1 :table) n))
      (dom/replace! (sel1 :table) (node [:table])))))
