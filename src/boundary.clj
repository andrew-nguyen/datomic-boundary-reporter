(ns boundary
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]
            [cognitect.transit :as transit]
            [metrics :as metrics]))

(defn url
  [& parts]
  (let [parts (interpose "/" (map name parts))
        url (apply str "https://premium-api.boundary.com/v1/" parts)]
    (println "parts:" parts)
    (println "url:" url)
    url))

(def config-reader (transit/reader (io/input-stream "config.json") :json))
(def config (transit/read config-reader))

(def email (get config "email"))
(def api-token (get config "api-token"))

(defn boundary-get
  "Generic GET for boundary"
  []
  (-> (http/get (url :metrics) {:basic-auth [email api-token], :as :json})
      (get-in [:body :result])))

(defn names
  "Takes list of maps (as returned by boundary-get) and returns a set of just the names as keywords"
  [boundary-metrics-list]
  (reduce #(conj %1 (keyword (:name %2))) #{} boundary-metrics-list))

(defn boundary-put
  "Generic PUT for boundary"
  [action params]
  (http/put (url action (:name params)) {:basic-auth [email api-token]
                                         :form-params params
                                         :content-type :json
                                         :throw-entire-message? true}))

(defn create-metric
  "Creates a single metric in boundary using a map as created by metrics.clj
  The expected map is the value of what's returned by metrics-map"
  [m]
  (boundary-put :metrics m))

(defn create-all-metrics
  "Calls metrics/metrics-map and creates all of the metrics in boundary"
  []
  (let [all-metrics (metrics/metrics-map)
        metric-maps (vals all-metrics)]
    (doseq [m metric-maps]
      (create-metric m))))
