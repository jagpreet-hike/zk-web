(ns ^{:author "Jagpreet Singh Chawla (jagpreet@hike.in)"
	:doc "To handle Google OAuth2 login"}
	zk-web.google-login
	(:require [clj-http.client :as http-client]
			  [cheshire.core :as json-parser]
			  [noir.response :as resp]
			  [zk-web.pages :refer [on-login-success]] ) 
	(:use [noir.core])
	)

(def CLIENT_ID "839489213646-h4grs7j97p39rncvh7uahrv5djscp45a.apps.googleusercontent.com")
(def REDIRECT_URI "http://localhost:8080/google")
(def CLIENT_SECRET "PqBrqFSFfgivzYDNS4S6fQVq")
(def google-user (atom {:google-id "" :google-name "" :google-email ""}))
 
(def redirect-url (str "https://accounts.google.com/o/oauth2/auth?"
              "scope=email%20profile&"
              "redirect_uri=" (ring.util.codec/url-encode REDIRECT_URI) "&"
              "response_type=code&"
              "client_id=" (ring.util.codec/url-encode CLIENT_ID) "&"
              "approval_prompt=force&"
              "hd=hike.in"))
 
(defn google [code]
 (if-not (nil? code)
	 (let [access-token-response (http-client/post "https://accounts.google.com/o/oauth2/token"
	                                          {:form-params {:code code
	                                           :client_id CLIENT_ID
	                                           :client_secret CLIENT_SECRET
	                                           :redirect_uri REDIRECT_URI
	                                           :grant_type "authorization_code"}})
	       user-details (json-parser/parse-string (:body (http-client/get (str "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
	 (get (json-parser/parse-string (:body access-token-response)) "access_token")))))]
	 ( on-login-success (get user-details "name") (get user-details "email") )
	 (resp/redirect "/") ) 

 	 (render [:get "/login"]
 		{:msg "Something went wrong. Try again." :target "/"}) ))
 
(defpage "/google" {:keys [code]}
  (google code) )
 
(defpage "/gauth" []
    (resp/redirect redirect-url) )

