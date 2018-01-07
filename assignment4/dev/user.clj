(ns user
  (:require [clojure.java.io :refer [resource]]
            [proto-repl-charts.charts :as charts]
            [task2 :refer [read-data log-models predict]]
            [task2a :refer [-main] :rename {-main mainA}]
            [task2b :refer [-main] :rename {-main mainB}]))

(def r (resource "dataset.csv"))
(def data (read-data r))
(def parts (split-at 650 data))
; (def models (log-models data))

(defn show-data
  [data]
  (charts/custom-chart "data"
                       {:data {:type "scatter"
                               :xs {"1" "1x"
                                    "2" "2x"
                                    "3" "3x"}
                               :columns (->> (group-by :y data)
                                             (mapcat (fn [[class data]]
                                                       (let [x (map (comp second :x) data)
                                                             y (map (comp first :x) data)
                                                             y (map (comp (partial + class)
                                                                          (partial * 10))
                                                                    y)]
                                                         [(apply vector (str class "x") x)
                                                          (apply vector (str class) y)])))
                                             (vec))}}))
