(ns db-examples-2.core
  (:gen-class))

(require '[clojure.java.jdbc :as db])

;; alternative ways to define db spec
(def db-spec {:subprotocol "postgresql"
              :subname "//localhost/reporting"
              :user "postgres"
              :password "sa"})

;; (def db-spec {:dbtype "postgresql"
;;               :dbname "reporting"
;;               :user "postgres"
;;               :password "sa"})

(defn create-users-table! []
  (db/db-do-commands db-spec
    (db/create-table-ddl
      :users
      [[:id "varchar(32) PRIMARY KEY"]
       [:pass "varchar(100)"]])))

(defn get-user [id]
  (first (db/query db-spec ["SELECT * FROM users WHERE id = ?" id])))

(defn get-users []
  (db/query db-spec ["SELECT * FROM users"]))

(defn add-user! [user]
  (db/insert! db-spec :users user))

;; call to add-users! is like
;; (add-users! {:id "user1" :pass "pass"} {:id "user2" :pass "pass"})
(defn add-users! [& users]
  (db/insert-multi! db-spec :users users))

(defn remove-user [id]
  (db/delete! db-spec :users ["id=?" id]))

(defn set-pass! [id pass]
  (db/update! db-spec ;; connection
                :users ;; table
                {:pass pass} ;; the update(s)
                ["id=?" id])) ;; WHERE clause
