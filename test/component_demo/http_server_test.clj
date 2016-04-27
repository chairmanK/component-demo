(ns component-demo.http-server-test
  (:require
    [clojure.test :refer :all]
    [component-demo.http-server :as http-server]
    [component-demo.kv-store :as kv-store]
    [component-demo.test-helpers :refer [with-components]]))

(deftest http-server-can-start-and-stop
  (with-components [store (kv-store/atom-kv-store)
                    server (assoc (http-server/http-server 0)
                                  :kv-store store)]
    (is true "HttpServer can start"))
  (is true "HttpServer can stop"))
