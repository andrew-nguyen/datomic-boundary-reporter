(ns datomic-boundary-reporter
  (:require [boundary :as boundary]
            [environ.core :as environ]))

(defn send-event
  [event]
  @(boundary/add-measurement event))

(defn now
  []
  (-> (java.util.Date.)
      .getTime))

(defn report-datomic-metrics
  [metrics]
  (let [now (now)]
    (doseq [[metric-name value] metrics]
      (let [metric-name (name metric-name)]
        (if (map? value)
          (doseq [[sub-metric-name sub-metric-value] value]
            (let [sub-metric-name (name sub-metric-name)]
              (send-event
                {:timestamp now
                 :metric metric-name
                 :sub-metric sub-metric-name
                 :value sub-metric-value})))
            (send-event
              {:timestamp now
               :metric metric-name
               :value value}))))))
