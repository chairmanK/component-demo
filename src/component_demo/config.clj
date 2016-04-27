(ns component-demo.config
  (:require
    [clojure.java.io :as io]
    [clojure.tools.cli :as cli]
    [camel-snake-kebab.core :as csk]))

(defn command-line-options
  [args]
  (-> args
      (cli/parse-opts
        [["-p" "--port PORT" "Port number"
          :parse-fn #(Integer/parseInt %)
          :default 3000]
         ["-d" "--database DATABASE" "Path to H2 database file"
          :default (.getAbsolutePath
                     (io/file
                       (System/getProperty "java.io.tmpdir")
                       "example.db"))]])
      :options))

(defn environment-variables
  []
  (->> (System/getenv)
       (map (fn [e] [(csk/->kebab-case-keyword (key e)) (val e)]))
       (into {})))

(defn get-configuration
  "Given command-line argument strings, return a configuration map"
  [& args]
  (let [overrides (command-line-options args)]
    (merge
      (select-keys (environment-variables) (keys overrides))
      overrides)))
