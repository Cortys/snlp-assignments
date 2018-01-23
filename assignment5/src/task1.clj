(ns task1
  (:require [instaparse.core :as insta :refer [defparser]]))

(defparser grammar
  "S := (NP VP | VP | WH_NP VP) END
   NP := NOUN | NP GP | NP CC NP
   VP := VERB NP? PP? | BE (GP | AP) | VP TO VP | VP CC VP
   GP := GERUND NP? PP?
   AP := ADJ PP?
   WH_NP := WP NP
   PP := PREP NP
   WP := 'what'
   CC := 'and'
   NOUN := <SP> ('flights' | 'boston' | 'pittsburgh' | 'los angeles' | 'minneapolis' | 'chicago' | 'indianapolis') <SP>
   VERB := <SP> ('leave' | 'pittsburgh' | 'indianapolis') <SP>
   GERUND := <SP> 'arriving' <SP>
   BE := <SP> 'are' <SP>
   TO := <SP> 'to' <SP>
   PREP := <SP> ('in' | 'to' | 'between') <SP>
   ADJ := <SP> 'available' <SP>
   END := <SP> ('.' | '?') <SP>
   SP := ' '*")

(defparser cnf-grammar
  "S := X1 END | VP END | VERB END
   X1 := NP VP | NOUN VP | WH_NP VP | NP VERB | NOUN VERB | WH_NP VERB
   NP := NP GP | NP X2 | NOUN GP | NOUN X2 | NP GERUND | NOUN GERUND
   X2 := CC NP | CC NOUN
   VP := VERB NP | VERB NOUN | VERB PP | VERB X3 | BE GP | BE GERUND | BE AP | BE ADJ | VP X4 | VERB X4
   X3 := NP PP | NOUN PP
   X4 := TO VP | TO VERB | CC VP | CC VERB
   GP := GERUND NP | GERUND PP | GERUND X3
   AP := ADJ PP
   WH_NP := WP NP | WP NOUN
   PP := PREP NP | PREP NOUN
   WP := 'what'
   CC := 'and'
   NOUN := <SP> ('flights' | 'boston' | 'pittsburgh' | 'los angeles' | 'minneapolis' | 'chicago' | 'indianapolis') <SP>
   VERB := <SP> ('leave' | 'pittsburgh' | 'indianapolis') <SP>
   GERUND := <SP> 'arriving' <SP>
   BE := <SP> 'are' <SP>
   TO := <SP> 'to' <SP>
   PREP := <SP> ('in' | 'to' | 'between') <SP>
   ADJ := <SP> 'available' <SP>
   END := <SP> ('.' | '?') <SP>
   SP := ' '*")

(def parses {:s1 (insta/parses grammar "what flights leave boston to pittsburgh .")
             :s2 (insta/parses grammar "what flights leave los angeles arriving in minneapolis .")
             :s3 (insta/parses grammar "what flights are available between chicago and indianapolis .")})

(def cnf-parses {:s1 (insta/parses cnf-grammar "what flights leave boston to pittsburgh .")
                 :s2 (insta/parses cnf-grammar "what flights leave los angeles arriving in minneapolis .")
                 :s3 (insta/parses cnf-grammar "what flights are available between chicago and indianapolis .")})

(defn visualize-parses
  [parses prefix]
  (doseq [[i parse] (map-indexed vector parses)]
    (insta/visualize parse :output-file (str prefix "p" i ".png") :output-format "png")))

(visualize-parses (:s1 parses) "img/normal/s1")
(visualize-parses (:s2 parses) "img/normal/s2")
(visualize-parses (:s3 parses) "img/normal/s3")

(visualize-parses (:s1 cnf-parses) "img/cnf/s1")
(visualize-parses (:s2 cnf-parses) "img/cnf/s2")
(visualize-parses (:s3 cnf-parses) "img/cnf/s3")
