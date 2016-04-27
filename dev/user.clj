(ns user
  (:require
    [clojure.tools.namespace.repl :refer [refresh]]
    [com.stuartsierra.component :as component]
    [component-demo
     [config :as config]
     [system :as system]]))

(def config
  (config/get-configuration))

(def system
  (system/build-system config))

(defn init
  "(Re)initialize the current development system"
  []
  (alter-var-root #'config (fn [_] (config/get-configuration)))
  (alter-var-root #'system (fn [_] (system/build-system config))))

(defn start
  "Start the current development system"
  []
  (alter-var-root #'system component/start))

(defn stop
  "Stop the current development system"
  []
  (alter-var-root #'system component/stop))

(defn- go*
  []
  (init)
  (start))

(defn go
  "(Re)initialize the current development system and start it"
  []
  (stop)
  (refresh :after 'user/go*))
