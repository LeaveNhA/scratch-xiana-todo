(ns hello.views
  (:require
   [re-frame.core :as re-frame]
   [hello.events :as events]
   [hello.subs :as subs]))

(defn main-panel []
  (re-frame/dispatch [::events/fetch-todos!])
  (let [todos (re-frame/subscribe [::subs/todos])]
    (println @todos)
    [:div
     (map #(identity [:ul (:todo/label %)])
          @todos)]))
