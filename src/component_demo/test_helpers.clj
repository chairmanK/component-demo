(ns component-demo.test-helpers
  (:require
    [com.stuartsierra.component :as component])
  (:import
    [java.util Arrays]))

(defmacro with-components
  "Start components, evaluate the body with local bindings for the started
  components, and finally stop components in reverse order.

  This macro is adapted from
  https://github.com/stuartsierra/component/issues/6"
  [bindings & body]
  {:pre [(vector? bindings)
         (even? (count bindings))
         (every? symbol? (take-nth 2 bindings))]}
  (if (zero? (count bindings))
    `(do ~@body)
    `(let [~(bindings 0) (component/start ~(bindings 1))]
       (try
         (with-components ~(subvec bindings 2) ~@body)
         (finally (component/stop ~(bindings 0)))))))

(defn bytes=
  [^bytes x ^bytes y]
  (Arrays/equals x y))
