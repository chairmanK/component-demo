(ns component-demo.system
  (:require
    [com.stuartsierra.component :as component]
    [component-demo
     [http-server :as http-server]
     [kv-store :as kv-store]]))

(defn build-system
  "Given a configuration map, return a system component that is ready to be
  `start`-ed"
  [{:keys [port database] :as config}]
  (-> (component/system-map
        :kv-store (kv-store/h2-kv-store database)
        :http-server (http-server/http-server port))
      (component/system-using
        {:kv-store []
         :http-server [:kv-store]})))
