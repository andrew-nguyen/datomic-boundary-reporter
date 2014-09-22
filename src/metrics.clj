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

(defn assoc-metric
  [m [metric units statistic description]]
  (assoc m (keyword metric) {:metric metric, :units units, :statistic statistic, :description description}))

(defn metrics-list
  [& [url]]
  (let [metrics-md (slurp (or url default-url))
        lines (map split-columns (split-lines metrics-md))
        metrics-map (reduce assoc-metric {} lines)]
    metrics-map))
