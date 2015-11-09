(ns zk-web.server
  (:gen-class)
  (:require [noir.server :as server]
            [zk-web.conf :as conf]
            [zk-web.google-login]
            ))

(server/load-views-ns 'zk-web.pages 'zk-web.google-login)


(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (:server-port (conf/load-conf))]
    (server/start port {:mode mode
                        :ns 'zk-web})))
