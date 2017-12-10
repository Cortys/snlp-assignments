(ns task2
  (:require [clojure.string :as str]
            [clojure.set :as set])
  (:gen-class))

(defn load-docs
  [path]
  (str/split (slurp path) #"\n"))

(defn doc->set
  [k doc]
  (disj (set (map (partial str/join "")
                  (partition k 1 doc)))
        ""))

(defn docs->sets
  [k docs]
  (map (partial doc->set k) docs))

(defn shingle-list
  [sets]
  (vec (apply set/union sets)))

(defn set->vec
  [shingle-list set]
  (mapv #(if (contains? set %) 1 0) shingle-list))

(defn sets->vecs
  [sets]
  (map (partial set->vec
                (shingle-list sets))
       sets))

(defn random-permutations
  [n dim]
  (repeatedly n #(shuffle (range dim))))

(defn vec->hash
  [permutations vec]
  (mapv (fn [permutation]
          (first (keep-indexed #(when (= 1 (get vec %2))
                                  %1)
                               permutation)))
        permutations))

(defn vecs->hashes
  [dim vecs]
  (map (partial vec->hash (random-permutations dim (count (first vecs))))
       vecs))

(defn jaccard-similarity
  [s1 s2]
  (if (= s1 s2)
    1
    (/ (count (set/intersection s1 s2))
       (count (set/union s1 s2)))))

(defn hash-similarity
  [h1 h2]
  (let [dim (count h1)]
    (/ (count (filter #(= (get h1 %) (get h2 %))
                      (range dim)))
       dim)))

(defn hash->bands-column
  [r hash]
  (map vec (partition r hash)))

(defn band->buckets
  [band]
  (->> (reduce-kv (fn [buckets i band-column]
                    (update buckets band-column
                            #(if % (conj % i) [i])))
                  {} (vec band))
       vals
       (filter #(< 1 (count %)))))

(defn hashes->buckets
  [r hashes]
  (let [bands-columns (map (partial hash->bands-column r) hashes)
        bands (loop [bands-columns bands-columns
                     bands '()]
                (let [band (map first bands-columns)]
                  (if (nil? (first band))
                    bands
                    (recur (map next bands-columns)
                           (conj bands band)))))
        buckets (mapcat band->buckets bands)]
    buckets))

(defn hashes->similarities
  [r hashes]
  (let [hashes (vec hashes)]
    (reduce (fn [similarities bucket]
              (let [comps (for [i (range (count bucket)), dj (subvec bucket (inc i))
                                :let [comp [(bucket i) dj]]
                                :when (not (contains? similarities comp))]
                            comp)
                    new-similarities (map (comp (partial apply hash-similarity)
                                                (partial map hashes))
                                          comps)]
                (merge similarities (zipmap comps new-similarities))))
            {} (hashes->buckets r hashes))))

(defn similar-hashes
  [r θ hashes]
  (->> (hashes->similarities r hashes)
       (filter (comp (partial <= θ) second))
       (map first)))

(defn similar-sets
  [b r θ sets]
  (->> sets
       (sets->vecs)
       (vecs->hashes (* b r))
       (similar-hashes r θ)))

(defn similar-docs
  [k b r θ docs]
  (->> docs
       (docs->sets k)
       (similar-sets b r θ)))

(def k 3)
(def b 20)
(def r 5)
(def θ 0.9)

(defn -main
  [path]
  (doseq [[d1 d2] (similar-docs k b r θ (load-docs path))]
    (println (str (inc d1) " = " (inc d2)))))
