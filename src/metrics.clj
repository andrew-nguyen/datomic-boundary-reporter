(ns metrics)

(def default-url "https://raw.githubusercontent.com/andrew-nguyen/datomic-boundary-reporter/master/doc/metrics.md")

(defn split-lines
  [s]
  (nthrest (clojure.string/split s #"\n") 2))

(defn split-columns
  [line]
  ; rest since we ignore the first, blank entry (lines lead with a |)
  (->> (clojure.string/split line #"\|")
       rest
       (map clojure.string/trim)))

(defn aggregate
  [statistic]
  (case (name statistic)
    "samples" "avg"
    (name statistic)))

(defn unit
  [unit]
  (case (name unit)
    "count" "number"
    "msec" "duration"
    "MB" "bytecount"
    "bytes" "bytecount"
    ""))

(defn upper-case-underscore
  [s]
  (-> s
      (clojure.string/replace #"([a-z])([A-Z])" "$1_$2")
      clojure.string/upper-case))

(defn assoc-metric
  [m [metric datomic-unit statistic description]]
  (assoc m (keyword metric) {:name (upper-case-underscore (str "datomic_" metric "_" statistic))
                             :displayNameShort (str "d/" metric)
                             :displayName (str "Datomic " metric) 
                             :unit (unit datomic-unit)
                             :description (str "[" statistic "]" description)
                             :defaultAggregate (aggregate statistic)
                             :isDisabled false}))

(defn metrics-map
  [& [url]]
  (let [metrics-md (slurp (or url default-url))
        lines (map split-columns (split-lines metrics-md))
        metrics-map (reduce assoc-metric {} lines)]
    metrics-map))
