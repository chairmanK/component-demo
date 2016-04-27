(ns component-demo.kv-store
  (:refer-clojure :exclude [get])
  (:require
    [clojure.java.jdbc :as jdbc]
    [clojure.tools.logging :as log]
    [com.stuartsierra.component :as component])
  (:import
    [com.mchange.v2.c3p0 ComboPooledDataSource DataSources]))

(defprotocol KVStore
  (get [this k]
    "Return the byte array that is stored with this string key, or nil if the
    key does not exist")
  (put [this k v]
    "Store a byte array with this string key, overwriting any already existing
    entry")
  (delete [this k]
    "Remove this string key along with the byte array that is stored with it,
    if the key exists"))

(defrecord AtomKVStore [a]
  component/Lifecycle
  (start [this]
    (log/info "Starting AtomKVStore")
    this)
  (stop [this]
    (log/info "Stopping AtomKVStore")
    this))

(extend-type AtomKVStore
  KVStore
  (get [{a :a} k]
    (@a k nil))
  (put [{a :a} k v]
    (swap! a assoc k v)
    nil)
  (delete [{a :a} k]
    (swap! a dissoc k)
    nil))

(defn atom-kv-store
  []
  (let [a (atom {})]
    (map->AtomKVStore {:a a})))

(defn- initialize-connection-pool
  [path]
  (doto (ComboPooledDataSource.)
    ; TODO additional configuration
    (.setDriverClass "org.h2.Driver")
    (.setJdbcUrl (format "jdbc:h2:%s" path))))

(defn- create-table-if-not-exists
  [db-spec]
  (jdbc/db-do-commands
    db-spec
    "CREATE TABLE IF NOT EXISTS store (k VARCHAR, v BINARY)"))

(defrecord H2KVStore [path datasource]
  component/Lifecycle
  (start [this]
    (log/info "Starting H2KVStore")
    (if datasource
      this
      (let [datasource* (initialize-connection-pool path)
            this* (assoc this :datasource datasource*)]
        (create-table-if-not-exists this*)
        this*)))
  (stop [this]
    (log/info "Stopping H2KVStore")
    (when datasource
      (DataSources/destroy datasource))
    (assoc this :datasource nil)))

(extend-type H2KVStore
  KVStore
  (get [this k]
    (jdbc/query
      this
      ["SELECT * FROM store WHERE k = ?" k]
      {:row-fn :v, :result-set-fn first}))
  (put [this k v]
    (jdbc/db-do-prepared
      this
      ["MERGE INTO store KEY(k) VALUES(?, ?)" k v])
    nil)
  (delete [this k]
    (jdbc/db-do-prepared
      this
      ["DELETE FROM store WHERE k = ?" k])
    nil))

(defn h2-kv-store
  [path]
  (map->H2KVStore {:path path}))
