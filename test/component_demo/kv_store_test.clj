(ns component-demo.kv-store-test
  (:require
    [clojure.test :refer :all]
    [component-demo.kv-store :as kv-store]
    [component-demo.test-helpers :refer [with-components bytes=]])
  (:import
    [java.io File]))

(defn test-kv-store
  [component]
  (let [k "this is a key"
        v (.getBytes "this is a value" "UTF-8")]
    (is (nil? (kv-store/delete component k)))
    (is (nil? (kv-store/get component k)))

    (is (nil? (kv-store/put component k v)))
    (is (bytes= v (kv-store/get component k)))

    (is (nil? (kv-store/put component k v)))
    (is (bytes= v (kv-store/get component k)))

    (is (nil? (kv-store/delete component k)))
    (is (nil? (kv-store/get component k)))

    (is (nil? (kv-store/delete component k)))))

(deftest AtomKVStore-satisfies-KVStore-protocol
  (with-components [component (kv-store/atom-kv-store)]
    (test-kv-store component)))

(deftest H2KVStore-satisfies-KVStore-protocol
  (let [path (.getAbsolutePath (File/createTempFile "test-" ".db"))]
    (with-components [component (kv-store/h2-kv-store path)]
      (test-kv-store component))))
