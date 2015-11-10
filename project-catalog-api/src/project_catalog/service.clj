(ns project-catalog.service
  (:require [io.pedestal.http :as bootstrap]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.interceptor.helpers :as interceptor]
            [ring.util.response :as ring-resp]
            [project-catalog.db :as db]))

(defn about-page
  [request]
  (ring-resp/response (format "Clojure %s - served from %s"
                              (clojure-version)
                              (route/url-for ::about-page))))

(defn home-page
  [request]
  (bootstrap/json-response (db/all-projects)))

(defn get-projects
  [request]
  (bootstrap/json-response (db/all-projects)))

(defn get-project
  [request]
  (let [project-name (get-in request [:path-params :project-name])]
    (bootstrap/json-response (db/get-project project-name))))

(defn add-project
  [request]
  (let [project (:json-params request)
        [persisted-project errors] (db/add-project project)]
    (if errors
      (assoc (bootstrap/json-response errors) :status 400)
      (bootstrap/json-response persisted-project))))

(def token-check
  (interceptor/handler
   ::token-check
   (fn [request]
     (let [token (get-in request [:headers "x-catalog-token"])]
       (if (not (= token "o brave new world"))
         (assoc (ring-resp/response {:body "access denied"}) :status 403))))))

(defroutes routes
  ;; Defines "/" and "/about" routes with their associated :get handlers.
  ;; The interceptors defined after the verb map (e.g., {:get home-page}
  ;; apply to / and its children (/about).
  [[["/" {:get home-page}
     ^:interceptors [(body-params/body-params)
                     bootstrap/html-body
                     token-check]        
     ["/projects" {:get get-projects
                   :post add-project}]
     ["/projects/:project-name" {:get get-project}]
     ["/about" {:get about-page}]]]])

;; Consumed by project-catalog.server/create-server
;; See bootstrap/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::bootstrap/interceptors []
              ::bootstrap/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::bootstrap/allowed-origins ["scheme://host:port"]

              ;; Root for resource interceptor that is available by default.
              ::bootstrap/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ::bootstrap/type :jetty
              ;;::bootstrap/host "localhost"
              ::bootstrap/port (Integer. (or (System/getenv "PORT") 5000))})

