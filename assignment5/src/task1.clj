(ns task1
  (:require [instaparse.core :as insta :refer [defparser]]))

(defparser grammar
  "S := (NP VP | VP | WH_NP VP) <END>
   NP := NOUN | NP GP | NP CC NP
   VP := VERB NP? PP? | BE (GP | AP)?
   GP := GERUND NP? PP?
   AP := ADJ PP?
   WH_NP := WP NP
   PP := PREP NP
   WP := 'what'
   CC := 'and'
   NOUN := <SP> ('flights' | 'boston' | 'pittsburgh' | 'los angeles' | 'minneapolis' | 'chicago' | 'indianapolis') <SP>
   VERB := <SP> 'leave' <SP>
   GERUND := <SP> 'arriving' <SP>
   BE := <SP> 'are' <SP>
   PREP := <SP> ('in' | 'to' | 'between') <SP>
   ADJ := <SP> 'available' <SP>
   END := '.' | '?'
   SP := ' '*")

(def parses {:s1 (insta/parses grammar "what flights leave boston to pittsburgh .")
             :s2 (insta/parses grammar "what flights leave los angeles arriving in minneapolis .")
             :s3 (insta/parses grammar "what flights are available between chicago and indianapolis .")})

(defn visualize-parses
  [parses prefix]
  (doseq [[i parse] (map-indexed vector parses)]
    (insta/visualize parse :output-file (str prefix "p" i ".png"))))

(visualize-parses (:s1 parses) "img/s1")
(visualize-parses (:s2 parses) "img/s2")
(visualize-parses (:s3 parses) "img/s3")
