(ns component-demo.system
  (:require
    [com.stuartsierra.component :as component]))

(defn build-system
  "Given a configuration map, return a system component that is ready to be
  `start`-ed"
  [config]
  (-> (component/system-map)
      (component/system-using {})))
