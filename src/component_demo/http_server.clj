(ns component-demo.http-server
  (:require
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [com.stuartsierra.component :as component]
    [ring.adapter.jetty :as jetty]
    [component-demo.kv-store :as kv-store])
  (:import
    [org.apache.commons.io IOUtils]))

(defn kv-store-handler-factory
  [backing-store]
  (fn [{method :request-method, k :uri, input-stream :body}]
    (try
      (case method
        :get    (if-let [v (kv-store/get backing-store k)]
                  {:status 200, :body (io/input-stream v)}
                  {:status 404})
        :put    (let [v (IOUtils/toByteArray input-stream)]
                  (kv-store/put backing-store k v)
                  {:status 200})
        :delete (do (kv-store/delete backing-store k)
                    {:status 200})
        {:status 405})
      (catch Exception e
        {:status 500, :body (.getMessage e)}))))

(defrecord HttpServer [port kv-store handler jetty]
  component/Lifecycle
  (start [this]
    (log/infof "Starting HttpServer")
    (if jetty
      this
      (let [handler (kv-store-handler-factory kv-store)
            jetty (jetty/run-jetty handler {:join? false :port port})]
        (assoc this :handler handler :jetty jetty))))
  (stop [this]
    (log/infof "Stopping HttpServer")
    (when jetty
      (.stop jetty))
    (assoc this :handler nil :jetty nil)))

(defn http-server
  [port]
  (map->HttpServer {:port port}))
