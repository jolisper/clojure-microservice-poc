(ns project-catalog.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.json]
            [metis.core :as metis :refer [defvalidator]]))

(defn get-db-uri []
  (or (System/getenv "MONGO_CONNECTION")
            "mongodb://project-catalog-db/project-catalog"))

(def db
  (:db (mg/connect-via-uri (get-db-uri))))

(def valid-project-keys
  [:project-name :name :framework :language])

(defvalidator project-validator
  [:project-name :presence])

(defn- dispose-invalid-project-keys [project]
  (select-keys project valid-project-keys))

(defn all-projects []
  (mc/find-maps db "projects"))

(defn get-project [project-name]
  (mc/find-maps db "projects" {:project-name project-name}))

(defn add-project [project]
  (let [errors (project-validator (dispose-invalid-project-keys project))]
    (if (empty? errors)
      [(mc/insert-and-return db "projects" project) nil]
      [nil errors])))
