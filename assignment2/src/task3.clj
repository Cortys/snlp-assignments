(ns task3
  (:require [clojure.string :refer [lower-case capitalize split split-lines join trim-newline]]
            [clojure.set :refer [union]])
  (:gen-class))

(defn words
  [sentence]
  (split sentence #"[^A-Za-z-']+"))

(defn sentences
  [document]
  (map #(concat [:start] (words %) [:end])
       (split (lower-case document) #"\s*[:;.!?]+\s*")))

(defn n-grams
  [n sentences]
  (->> sentences
    (mapcat (partial partition n 1))
    (map vec)
    (frequencies)))

(defn all-grams
  [n sentences]
  (apply merge
         {[] (count (flatten sentences))}
         (map #(n-grams % sentences)
              (range 1 (inc n)))))

(defn mutations
  ([n string]
   (mutations n string 0))
  ([n string c]
   (case n
     0 {string 0}
     1 (assoc (apply union (for [i (range 0 (inc (count string)))]
                             (let [[a [b1 b2 & bn :as b]] (split-at i string)]
                               (zipmap (concat ; insert & replace:
                                               (mapcat (fn [c] (map #(str (join "" a)
                                                                          (char c)
                                                                          (join "" %))
                                                                    [b (rest b)]))
                                                       (range 97 (+ 97 26)))
                                               ; delete:
                                               [(str (join "" a) (join "" (rest b)))]
                                               ; swap:
                                               (if (and b1 b2)
                                                 [(str (join "" a) b2 b1 (join "" bn))]
                                                 []))
                                       (repeat (inc c))))))
              string c)
     (apply merge-with min (map (partial apply mutations (dec n))
                                (mutations 1 string c))))))

(def ^:private alpha 0.4)

(defn probs
  [n sentences]
  (let [n-grams (all-grams n sentences)]
    (memoize (fn [words]
               (let [words (vec words)
                     word (last words)
                     back (min n (count words))
                     start (- (count words) back)]
                 (or (->> (range back)
                          (keep #(let [i (+ start %)
                                       pre (subvec words i (dec (count words)))]
                                   (when-let [count (n-grams (conj pre word))]
                                     (* (Math/pow alpha %) (/ count (n-grams pre))))))
                          first)
                     0))))))

(defn correction
  [probs test]
  (let [words (vec (words test))
        pre (mapv lower-case (subvec words 0 (dec (count words))))
        suf (last words)
        uc (re-matches #"^[A-Z].*" suf)
        suf (lower-case suf)
        mutations (mutations 2 suf)
        mut-changes-count (reduce #(+ %1 (if (zero? %2) 0 (/ 1 %2))) (vals mutations))
        typo-prob 0.1
        [correction ch] (apply max-key (fn [[correction changes]]
                                         (* (if (zero? changes)
                                              (- 1 typo-prob)
                                              (* typo-prob (/ 1 (* changes mut-changes-count))))
                                            (probs (conj pre correction))))
                               mutations)
        correction (if (zero? (probs (conj pre correction)))
                     suf correction)]
    (if uc
      (capitalize correction)
      correction)))

(defn read-files
  [corpus input]
  (let [[corpus input] (map slurp [corpus input])
        probs (probs 3 (sentences corpus))
        tests (-> input
                  trim-newline
                  split-lines)]
    [probs tests]))

(defn -main
  [corpus input]
  (let [[probs tests] (read-files corpus input)
        corrections (zipmap tests
                            (map (partial correction probs) tests))]
    (doseq [[s c] corrections]
      (println (str s ", " c)))))
