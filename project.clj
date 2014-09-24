(defproject datomic-boundary-reporter "0.1.0"
  :description "Lib for reporting datomic metrics to boundary"
  :url "http://github.com/andrew-nguyen/datomic-boundary-reporter"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-http "1.0.0"]
                 [com.cognitect/transit-clj "0.8.259"]
                 [com.palletops/thread-expr "1.3.0"]
                 [environ "0.4.0"]])
