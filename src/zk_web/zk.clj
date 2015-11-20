(ns zk-web.zk
  (:require [noir.session :as session])
  (:import [com.netflix.curator.retry RetryNTimes]
           [com.netflix.curator.framework CuratorFramework CuratorFrameworkFactory])
  (:refer-clojure :exclude [set get])
  (:use zk-web.util
        clojure.tools.logging))

(defn- log-message
  "logs/sends message to required output/hook"
  [cli message]
  (def msg (str message " ( Connection String: " (-> cli (.getZookeeperClient) (.getCurrentConnectionString) ) " )") )
  (info msg)
  (post-to-slack msg)
)

(defn- mk-zk-cli-inner
  "Create a zk client using addr as connecting string"
  [ addr ]
  (let [cli (-> (CuratorFrameworkFactory/builder)
                (.connectString addr)
                (.retryPolicy (RetryNTimes. (int 3) (int 1000)))
                (.build))
        _ (.start cli)]
    cli))

;; memorize this function to save net connection
(def mk-zk-cli (memoize mk-zk-cli-inner))

(defn create
  "Create a node in zk with a client"
  ([cli path data]
    (do
     (log-message cli (str (session/get :user) " is creating: " path " with data " (new String data)))
     (-> cli
      (.create)
      (.creatingParentsIfNeeded)
      (.forPath path data))) ) 
  ([cli path]
    (do
     (log-message cli (str (session/get :user) " is creating: " path))
     (-> cli
         (.create)
         (.creatingParentsIfNeeded)
         (.forPath path))) ) )

(defn rm
  "Delete a node in zk with a client"
  [cli path]
  (do
    (log-message cli (str (session/get :user) " is removing: " path))
    (-> cli (.delete) (.forPath path))) )

(defn ls
  "List children of a node"
  [cli path]
  (-> cli (.getChildren) (.forPath path)))

(defn stat
  "Get stat of a node, return nil if no such node"
  [cli path]
  (-> cli (.checkExists) (.forPath path) bean (dissoc :class)))

(defn set
  "Set data to a node"
  [cli path data]
  (do
    (log-message cli (str (session/get :user) " is setting data: " (new String data) " for path " path))
    (-> cli (.setData) (.forPath path data))) )

(defn get
  "Get data from a node"
  [cli path]
  (-> cli (.getData) (.forPath path)))

(defn rmr
  "Remove recursively"
  [cli path]
  (do 
    (log-message cli (str (session/get :user) " is removing Recursively: " path))
    (doseq [child (ls cli path)]
      (rmr cli (child-path path child)))
    (rm cli path)) )
