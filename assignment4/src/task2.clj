(ns task2
  (:require [clojure.java.io :refer [reader resource]]
            [clojure.data.csv :refer [read-csv]]
            [lambda-ml.core :as lml]
            [lambda-ml.regression :as reg])
  (:gen-class))

(def α 0.01)
(def λ 0.1)
(def i 200)

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
  (set (map last data)))

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
  [train-data]
  (let [classes (classes data)]
    (->> classes
         (map (juxt identity (comp log-model
                                   (partial format-data data))))
         (into #{}))))

(defn predict
  [test-data models])

(def data (read-data (resource "dataset.csv")))

(defn -main
  [path])
