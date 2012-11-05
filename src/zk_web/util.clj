(ns zk-web.util
  (:require [noir.session :as session])
  (:import [java.nio.charset Charset]))

(defn bytes->str
  "Convert byte[] to String"
  [bytes]
  (String. bytes (Charset/forName "UTF-8")))

(defn normalize-path
  "fix the path to normalized form"
  [path]
  (let [path (if (empty? path) "/" path)
        path (if (and (.endsWith path "/") (> (count path) 1))
               (apply str (drop-last path))
               path)]
    path))

(defn space
  "Retern a number of html space"
  [n]
  (apply str (repeat n "&nbsp;")))

(defmacro when-admin [ & exprs ]
  `(when (session/get :user)
     ~@exprs))