(ns hello.core
  (:require
   [hello.controllers.index :as index]
   [hello.controllers.re-frame :as re-frame]
   [framework.config.core :as config]
   [framework.db.core :as db]
   [framework.interceptor.core :as interceptors]
   [framework.rbac.core :as rbac]
   [framework.route.core :as routes]
   [framework.session.core :as session]
   [framework.webserver.core :as ws]
   [piotr-yuxuan.closeable-map :refer [closeable-map]]
   [honeysql.helpers :refer [select from]]
   [reitit.ring :as ring]
   [ring.util.response :as ring-response]
   [xiana.commons :refer [rename-key]]
   [xiana.core :as xiana]))

(defn add-todo-view
  [state]
  (let [resp (-> state :response-data :db-data)]
    (xiana/ok (assoc state :response {:status 200
                                      :body {:response resp}}))))

(defn get-all-todos
  [state]
  (xiana/ok
   (-> state
       (assoc :query (-> (select :*) (from :todo)))
       (assoc :view add-todo-view))))

(defn mark-todo-done
  [state]
  (xiana/ok
   (-> state
       (assoc :response (ring-response/response "mark-todo-done!")))))

(defn add-todo
  [state]
  (xiana/ok
   (-> state
       (assoc :response (ring-response/response "get-all-todos!")))))

(def routes
  [["/" {:action index/handle-index}]
   ["/re-frame" {:action re-frame/handle-index}]
   ["/assets/*" (ring/create-resource-handler {:path "/"})]

   ["/api" {}
    ["/todos" (ring-response/response "hello")
     ["/get-all"
      {:action #'get-all-todos}]
     ["/add"
      {:action #'add-todo}]
     ["/mark-done/:id"
      {:action #'mark-todo-done}]]]])

(defn ->system
  [app-cfg]
  (-> (config/config app-cfg)
      (rename-key :framework.app/auth :auth)
      routes/reset
      rbac/init
      session/init-in-memory
      db/connect
      db/migrate!
      ws/start
      closeable-map))

(def app-cfg
  {:routes routes
   :router-interceptors     []
   :controller-interceptors [(interceptors/muuntaja)
                             interceptors/params
                             session/guest-session-interceptor
                             interceptors/view
                             interceptors/side-effect
                             db/db-access
                             rbac/interceptor]})

(defn -main
  [& _args]
  (->system app-cfg))
