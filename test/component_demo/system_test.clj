(ns component-demo.system-test
  (:require
    [clojure.test :refer :all]
    [component-demo.system :as system]
    [component-demo.test-helpers :refer [with-components]]))

(def test-config
  {})

(deftest build-system-returns-a-system-component
  (with-components [system (system/build-system test-config)]
    (is true "system can start"))
  (is true "system can stop"))
