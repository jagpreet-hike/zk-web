(defproject zk-web "0.1.0-SNAPSHOT"
            :description "FIXME: write this!"
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [noir "1.3.0-beta3"]
                           [org.apache.curator/curator-framework "3.0.0"]
                           [org.apache.curator/curator-test "3.0.0"]
                           [clj-http "2.0.0"]
                           [cheshire "5.5.0"]
                           [org.clojure/tools.logging "0.3.1"]
			   [org.slf4j/slf4j-log4j12 "1.7.13"]]
            :main zk-web.server)
