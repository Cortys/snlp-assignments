(ns task1
  (:require [clojure.string :refer [lower-case split join]]))

(defn print-bigrams
  [word-set bigrams]
  (let [matrix (into [(into [""] word-set)
                      (vec (repeat (inc (count word-set)) "---"))]
                     (for [word1 word-set]
                       (into [(str "**" word1 "**")]
                             (for [word2 word-set]
                               (bigrams [word1 word2])))))]
    (doseq [row matrix]
      (println (join " | " row)))))

(def document "London is the capital and largest city of England. Million people live in London. The River Thames is in London. London is the largest city in Western Europe.")

(def words (-> document
               (lower-case)
               (split #"[^a-z]+")))

(def word-set (into (sorted-set) words))
(def word-count (frequencies words))

(def bigrams (into (zipmap (for [w1 word-set, w2 word-set] [w1 w2])
                           (repeat 0))
                   (->> words
                             (partition 2 1)
                             (map vec)
                             (frequencies))))

(def add-1-bigrams (into {} (map (fn [[[w1 w2 :as k] v]]
                                   [k (/ (inc v) (+ (word-count w1) (count word-set)))])
                                 bigrams)))

(println "Simple:")
(print-bigrams word-set bigrams)

(println "Add-1:")
(print-bigrams word-set add-1-bigrams)
