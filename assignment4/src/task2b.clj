(ns task2b
  (:require [lambda-ml.core :refer [random-partition]]
            [task2 :refer [read-data log-models predict]])
  (:gen-class))

(defn folds
  [data]
  (let [partitions (vec (random-partition 10 data))]
    (for [i (range 10)]
      [(apply concat (keep-indexed #(if (not= i %1) %2) partitions))
       (nth partitions i)])))

(defn f-measure
  [counts]
  (try
    (/ (* 2 (counts [true true]))
       (+ (* 2 (counts [true true]))
          (counts [false true])
          (counts [true false])))
    (catch ArithmeticException e 1)))

(defn score-for-result
  [data models]
  (let [predictions (predict (map :x data)
                             models)
        answers (map :y data)
        counts (reduce (fn [counts [prediction answer]]
                         (into {} (for [model (keys models)
                                        :let [counts (counts model)]]
                                    [model (update counts
                                                   [(= model prediction)
                                                    (= model answer)]
                                                   inc)])))
                       (->> (keys models)
                            (map #(vector % {[false false] 0
                                             [false true] 0
                                             [true false] 0
                                             [true true] 0}))
                            (into {}))
                       (map vector predictions answers))
        micro-counts (apply merge-with + (vals counts))
        micro-score (f-measure micro-counts)
        macro-score (/ (reduce + (map f-measure (vals counts)))
                       (count models))]
    {:micro micro-score
     :macro macro-score}))

(defn score-for-fold
  [[train test]]
  (score-for-result test (log-models train)))

(defn -main
  [path]
  (let [data (read-data path)
        folds (folds data)
        {:keys [micro macro]} (apply merge-with + (pmap score-for-fold folds))
        micro (/ micro (count folds))
        macro (/ macro (count folds))]
    (println (str "Micro-averaged F-measure: " (double micro)))
    (println (str "Macro-averaged F-measure: " (double macro)))
    (shutdown-agents)))
