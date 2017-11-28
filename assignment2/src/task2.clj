(ns task2
  (:gen-class))

(defn damerau-levenshtein
  [s1 s2]
  (let [[c1 c2] (map (comp inc count) [s1 s2])
        d (reduce (fn [d [i1 i2]]
                    (assoc-in d [i1 i2]
                              (cond
                                (zero? i1) i2
                                (zero? i2) i1
                                :else
                                (min (+ (get-in d [(dec i1) (dec i2)])
                                        (if (= (get s1 (dec i1)) (get s2 (dec i2))) 0 1))
                                     (inc (get-in d [(dec i1) i2]))
                                     (inc (get-in d [i1 (dec i2)]))
                                     (if (and (> i1 1) (> i2 1)
                                              (= (get s1 (dec i1))
                                                 (get s2 (- i2 2)))
                                              (= (get s2 (dec i2))
                                                 (get s1 (- i1 2))))
                                       (inc (get-in d [(- i1 2) (- i2 2)]))
                                       Double/POSITIVE_INFINITY)))))
                  (into [(apply vector-of :long (range c2))]
                        (map (partial vector-of :long) (range 1 c1)))
                  (for [i1 (range 1 c1), i2 (range 1 c2)] [i1 i2]))]
    (get-in d [(dec c1) (dec c2)])))

(defn -main
  [s1 s2]
  (println (damerau-levenshtein s1 s2)))
