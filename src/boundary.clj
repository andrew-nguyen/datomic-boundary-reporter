(ns boundary
  (:require [clj-http.client :as http]
            [clojure.java.io :as io]
            [cognitect.transit :as transit]
            [metrics :as metrics]
            [pallet.thread-expr :refer [when-let-> when-not->]]))

(defn boundary-url
  [& parts]
  (let [parts (interpose "/" (map name parts))
        url (apply str "https://premium-api.boundary.com/v1/" parts)]
    url))

(def config-reader (transit/reader (io/input-stream "config/boundary-config.json") :json))
(def config (transit/read config-reader))

(def email (get config "email"))
(def api-token (get config "api-token"))

(def hostname (.. java.net.InetAddress getLocalHost getHostName))

(defn boundary-get
  "Generic GET for boundary"
  [action]
  (-> (http/get (boundary-url action) {:basic-auth [email api-token], :as :json})
      (get-in [:body :result])))

(defn get-metrics
  []
  (boundary-get :metrics))

(defn names
  "Takes list of maps (as returned by boundary-get) and returns a set of just the names as keywords"
  [boundary-metrics-list]
  (reduce #(conj %1 (:name %2)) #{} boundary-metrics-list))

(def registered-metrics (atom (names (get-metrics))))

(def method-map {:put http/put
                 :post http/post
                 :delete http/delete})

(defn boundary*
  [method f url params]
  (-> (try
        (f url {:basic-auth [email api-token]
                :form-params params
                :content-type :json
                :throw-entire-message? true
                :as :json})
        (catch Exception e))
      (get-in [:body :result :success])))

(defn boundary
  "Generic boundary REST call"
  [method url & [params]]
  (future
    (let [f (method-map method)]
      (loop [success? (boundary* method f url params)
             num-loops 1
             sleep-time 1000]
        (if (or success? (>= num-loops 10))
          success?
          (recur (do
                   (Thread/sleep sleep-time)
                   (boundary* method f url params))
                 (inc num-loops)
                 (* sleep-time 2)))))))

(defn ends-with?
  [s match]
  (boolean (re-find (re-pattern (str ".*" match "$")) s)))

(defn unit
  [m]
  (let [metric-name (:metric m)]
    (cond
      (ends-with? metric-name "MB") "bytecount"
      (ends-with? metric-name "Bytes") "bytecount"
      (ends-with? metric-name "Msec") "duration"
      :else "number")))

(defn upper-case-underscore
  [s]
  (-> s
      (clojure.string/replace #"([a-z])([A-Z])" "$1_$2")
      clojure.string/upper-case))

(defn boundary-name
  [m]
  (-> (str "DATOMIC_" (upper-case-underscore (:metric m)))
      (when-let-> [s (:sub-metric m)]
        (str "_" (upper-case-underscore s)))))

(defn display-name
  [m]
  (-> (str "Datomic " (:metric m))
      (when-let-> [s (:sub-metric m)]
        (str " " (clojure.string/capitalize s)))))

(defn description
  [m]
  (display-name m))

(defn display-name-short
  [m]
  (-> (str "d/" (:metric m))
      (when-let-> [s (:sub-metric m)]
        (str "/" s))))

(defn default-aggregate
  [m]
  (case (:sub-metric m)
    "lo" "min"
    "hi" "max"
    "sum" "sum"
    "count" "avg"
    "avg"))

(defn ->metric
  "Takes the generic map (for creation of a metric or measurement) and modifies it to be compliant with create-metric"
  [m]
  (-> {}
      (assoc :name (boundary-name m))
      (assoc :description (description m))
      (assoc :displayName (display-name m))
      (assoc :displayNameShort (display-name-short m))
      (assoc :unit (unit m))
      (assoc :defaultAggregate (default-aggregate m))))

(defn value
  [m]
  (if (ends-with? (:metric m) "MB")
    (reduce * (:value m) [1024 1024])
    (:value m)))

(defn timestamp
  [m]
  (:timestamp m))

(defn ->measurement
  [m]
  (-> {}
      (assoc :source hostname)
      (assoc :metric (boundary-name m))
      (assoc :measure (value m))
      (assoc :timestamp (timestamp m))))

(defn create-metric
  "Creates a single metric in boundary using a map as created by metrics.clj The
  expected map is the value of what's returned by metrics-map and what's
  required by boundary's api's
  :name - name of the metric, globally unique [0-9A-Z_]+
  :description
  :displayName
  :displayNameShort - < 15 characters preferred
  :unit - percent, number, bytecount, duration
  :defaultAggregate - sum, avg, max, min
  :isDisabled"
  [m]
  (boundary :put (boundary-url :metrics (:name m)) m))

(defn delete-metric
  "Creates a single metric in boundary using a map as created by metrics.clj
  The expected map is the value of what's returned by metrics-map"
  [m]
  (boundary :delete (boundary-url :metrics (:name m)) {:deleteAlarms true}))

(defn add-measurement
  "Expects a map with:
  :metric - name of the metric as setup in the account
  :measure - numeric measure to report
  :timestamp - unix timestamp of when the measurement was taken"
  [event]
  (future
    (when-not (contains? @registered-metrics (boundary-name event))
      (let [success? @(create-metric (->metric event))]
        (when success?
          (swap! registered-metrics conj (boundary-name event)))))
    (boundary :post (boundary-url :measurements) (->measurement event))))

(defn create-all-metrics
  "Calls metrics/metrics-map and creates all of the metrics in boundary"
  []
  (let [all-metrics (metrics/metrics-map)
        metric-maps (vals all-metrics)]
    (doseq [m metric-maps]
      (create-metric m))))

(defn delete-all-metrics
  "Calls metrics/metrics-map and deletes all of the metrics in boundary"
  []
  (let [all-metrics (metrics/metrics-map)
        metric-maps (vals all-metrics)]
    (doseq [m metric-maps]
      (delete-metric m))))
