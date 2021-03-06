(defproject component-demo "0.1.0-SNAPSHOT"
  :description "Example application that demonstrates usage of component"
  :url "http://github.com/chairmank/component-demo"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/java.jdbc "0.5.8"]
                 [org.clojure/tools.cli "0.3.3"]
                 [org.clojure/tools.logging "0.3.1"]
                 [com.stuartsierra/component "0.3.1"]
                 [camel-snake-kebab "0.4.0"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [com.h2database/h2 "1.4.191"]
                 [com.mchange/c3p0 "0.9.5.2"]
                 [commons-io/commons-io "2.5"]
                 [ring/ring-jetty-adapter "1.4.0"]]
  :main component-demo.main
  :profiles {:uberjar {:aot :all}
             :repl {:source-paths ["dev"]
                    :dependencies [[org.clojure/tools.namespace "0.2.11"]]
                    :main user}})
