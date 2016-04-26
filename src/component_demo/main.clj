(ns component-demo.main
  (:require
    [clojure.tools.logging :as log]
    [com.stuartsierra.component :as component]
    [component-demo
     [config :as config]
     [system :as system]])
  (:import
    [java.lang Thread]))

(defn- shutdown-promise
  []
  (let [p (promise)
        deliver! #(deliver p true)]
    (try
      (.addShutdownHook (Runtime/getRuntime) (Thread. deliver!))
      (catch IllegalStateException e
        ; shutdown sequence has already begun
        (deliver!)))
    p))

(defn- stop-on-shutdown
  [system p]
  @p ; block until promise is delivered
  (log/info "Stopping system")
  (component/stop system))

(defn -main
  [& args]
  (let [p (shutdown-promise)]
    (log/info "Starting system")
    (-> (apply config/get-configuration args)
        (system/build-system)
        (component/start)
        (stop-on-shutdown p))))
