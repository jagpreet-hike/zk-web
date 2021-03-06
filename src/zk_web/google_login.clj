(ns ^{:author "Jagpreet Singh Chawla (jagpreet@hike.in)"
	:doc "To handle Google OAuth2 login"}
	zk-web.google-login
	(:require [clj-http.client :as http-client]
			  [cheshire.core :as json-parser]
			  [noir.response :as resp]
			  [zk-web.pages :refer [on-login-success]]
			  [zk-web.conf :as conf] )
	(:use [noir.core])
	)

(def CLIENT_ID (:g-client-id (conf/load-conf)) )
(def REDIRECT_URI (:g-redirect-url (conf/load-conf)) )
(def CLIENT_SECRET (:g-client-secret (conf/load-conf)) )
(def DOMAIN (:g-hd (conf/load-conf)))
(def ALLOWED_EMAIL_IDS (:allowed-email-ids (conf/load-conf)) )
(def RESTRICT_TO_IDS (:restrict-to-ids (conf/load-conf)) )

(def redirect-url (str "https://accounts.google.com/o/oauth2/auth?"
              "scope=email%20profile&"
              "redirect_uri=" (ring.util.codec/url-encode REDIRECT_URI) "&"
              "response_type=code&"
              "client_id=" (ring.util.codec/url-encode CLIENT_ID) "&"
              "approval_prompt=force&"
              (if (nil? DOMAIN ) "" (str "hd=" DOMAIN) ) ))

(defn google [code]
 (if-not (nil? code)
	 (let [access-token-response (http-client/post "https://www.googleapis.com/oauth2/v3/token"
	                                          {:form-params {:code code
	                                           :client_id CLIENT_ID
	                                           :client_secret CLIENT_SECRET
	                                           :redirect_uri REDIRECT_URI
	                                           :grant_type "authorization_code"}})
	       user-details (json-parser/parse-string (:body (http-client/get (str "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
	 (get (json-parser/parse-string (:body access-token-response)) "access_token")))))]
	 (if (and (or (nil? DOMAIN ) (.endsWith (get user-details "email") (str "@" DOMAIN))) (or (not RESTRICT_TO_IDS) (contains? ALLOWED_EMAIL_IDS (get user-details "email"))))
	 	(do ( on-login-success (get user-details "name") (get user-details "email") )
	 		(resp/redirect "/"))
	 	(render [:get "/login"]
 			{:msg "Please Use only Hike Email Ids." :target "/"}) ))

 	 (render [:get "/login"]
 		{:msg "Something went wrong. Try again." :target "/"}) ))

(defpage "/google" {:keys [code]}
  (google code) )

(defpage "/gauth" []
    (resp/redirect redirect-url) )
