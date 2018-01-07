(ns task2
  (:require [clojure.java.io :refer [reader]]
            [clojure.data.csv :refer [read-csv]]
            [lambda-ml.core :as lml]
            [lambda-ml.regression :as reg]))

(def α 0.1)
(def λ 0.1)
(def i 500)

(defn read-data
  [path]
  (let [raw-data (-> path reader read-csv)]
    (->> raw-data
         (drop 1)
         (map (fn [row]
                (let [[num brand female age] (map #(Integer/parseInt %) row)]
                  {:x [female age]
                   :y brand}))))))

(defn classes
  [data]
  (set (map :y data)))

(defn format-data
  [data class]
  (map (fn [{:keys [x y]}]
         (conj x (if (= class y) 1 0)))
       data))

(defn log-model
  [train-data]
  (reg/regression-fit (reg/make-logistic-regression α λ i)
                      train-data))

(defn log-models
  [data]
  (let [classes (classes data)]
    (->> classes
         (pmap (juxt identity (comp log-model
                                    (partial format-data data))))
         (into {}))))

(defn predict
  [data models]
  (->> models
       (map (fn [[class model]]
              (map (partial hash-map class)
                   (reg/regression-predict model data))))
       (apply interleave)
       (partition (count (keys models)))
       (map (comp #(apply max-key % (keys models))
                  (partial apply merge)))))
