(ns zk-web.server
  (:gen-class)
  (:require [noir.server :as server]
            [noir.response :as resp]
            [zk-web.conf :as conf]
            [zk-web.pages :refer [users-token-map]]
            [zk-web.google-login]
            ))

(server/load-views-ns 'zk-web.pages 'zk-web.google-login)


(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (:server-port (conf/load-conf))]
    (server/add-middleware 
	  	(fn
	  		;To check if user is Logged in before processing request. Only logged in users should be allowed access to any data or changes. 
	  		[handler]
	  		(fn [request]
	  			(let [user-token (get @users-token-map (get-in request [:session :user])) ]
					(if (or (some #(boolean (re-find % (get request :uri))) #{#"^/login" #"^/img/.*" #"^/js/.*" #"^/css/.*" #"^/gauth" #"^/google"})
						(and (not (nil? user-token)) (= user-token (get-in request [:cookies "token" :value])) ))
						(handler request) (resp/redirect "/login") ))
				))
	)
    (server/start port {:mode mode
                        :ns 'zk-web}))
)
