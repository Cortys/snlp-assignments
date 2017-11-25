(ns task1
  (:require [clojure.string :refer [lower-case split join]]
            [clojure.set :refer [difference]]))

(def s-start "<s>")
(def s-end "</s>")

(defn print-bigrams
  [word-set bigrams]
  (let [matrix (into [(into [""] (disj word-set s-start))
                      (vec (repeat (count word-set) "---"))]
                     (for [word1 (disj word-set s-end)]
                       (into [(str "**" word1 "**")]
                             (for [word2 (disj word-set s-start)]
                               (bigrams [word1 word2])))))]
    (doseq [row matrix]
      (println (join " | " row)))))

(def document "London is the capital and largest city of England. Million people live in London. The River Thames is in London. London is the largest city in Western Europe.")

(def sentences (map #(concat [s-start] (split % #"[^a-z]+") [s-end])
                    (-> document
                        (lower-case)
                        (split #"\s*\.\s*"))))

(def word-count (frequencies (flatten sentences)))
(def word-set (difference (into (sorted-set) (keys word-count))))

(def bigrams (into (zipmap (for [w1 word-set, w2 word-set] [w1 w2])
                           (repeat 0))
                   (->> sentences
                        (mapcat (partial partition 2 1))
                        (map vec)
                        (frequencies))))

(def add-1-bigrams (into {} (map (fn [[[w1 w2 :as k] v]]
                                   [k (/ (if (or (= w1 s-end) (= w2 s-start))
                                           v (inc v))
                                         (+ (word-count w1) (count word-set) -1))])
                                 bigrams)))

(println "Simple:")
(print-bigrams word-set bigrams)

(println "Add-1:")
(print-bigrams word-set add-1-bigrams)
