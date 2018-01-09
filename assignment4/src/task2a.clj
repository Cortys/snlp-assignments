(ns task2a
  (:require [task2 :refer [read-data log-models predict]])
  (:gen-class))

(defn -main
  [path]
  (let [data (read-data path)
        [train-data test-data] (split-at 650 data)
        models (log-models train-data)]
    (doseq [prediction (predict (map :x test-data) models)]
      (println prediction))
    (shutdown-agents)))
