(ns component-demo.config-test
  (:require
    [clojure.test :refer :all]
    [component-demo.config :as config]))

(deftest get-configuration-returns-map
  (is (map? (config/get-configuration))))
