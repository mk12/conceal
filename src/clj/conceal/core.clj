;;; Copyright 2014 Mitchell Kember. Subject to the MIT License.

(ns conceal.core
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "<p>Hello from Compojure</p>")
  (route/resources "/")
  (route/not-found "Page not found"))

(def handler
  (handler/site app-routes))
