(ns reitit-bug.core
  (:require [reitit.ring :as ring]
            [ring.util.response :refer [response resource-response content-type]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(def handler
  (ring/ring-handler
   (ring/router
    ["/ping" {:get (constantly (response "pong"))}])
   (ring/routes
    (ring/create-resource-handler {:path "/"
                                   :not-found-handler (fn [x]
                                                        (println "not found handler: " x)
                                                        (-> "index.html"
                                                            (resource-response {:root "public"})
                                                            (content-type "text/html")))})
    (ring/create-default-handler))))

(defn -main [& [port]]
  (let [port (Integer. (or port (System/getenv "PORT") 5000))]
    (run-jetty handler {:port port :join? false})))
