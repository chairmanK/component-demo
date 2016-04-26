(ns component-demo.config
  (:require
    [clojure.tools.cli :as cli]
    [camel-snake-kebab.core :as csk]))

(defn environment-variables
  []
  (->> (System/getenv)
       (map (fn [e] [(csk/->kebab-case-keyword (key e)) (val e)]))
       (into {})))

(defn get-configuration
  "Given command-line argument strings, return a configuration map"
  [& args]
  {})
