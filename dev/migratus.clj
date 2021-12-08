(ns migratus-repl
  (:require
    [framework.config.core :as config]
    [migratus.core :as m]))

(def cfg
  (let [c (config/config)]
    (-> c
        :framework.db.storage/migration
        (assoc :db (:framework.db.storage/postgresql c)))))

(defn create-migration
  "Creates a pair (down&up) of new migration files."
  [name]
  (m/create cfg name))

(comment
  (m/migrate cfg) ; applies all new migrations

  (m/reset cfg) ; applies all "down" migrations, then applies all "up"s

  (m/completed-list cfg) ; list completed migrations

  (create-migration
   "todo-table"
    #_"^^^ new migration name here ^^^")
)
