(defproject snlp-assignment5 "0.1.0-SNAPSHOT"
  :description "SNLP assignment 5."

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [instaparse "1.4.8"]]

  :profiles {:dev {:source-paths ["dev" "src"]
                   :dependencies [[proto-repl "0.3.1"]
                                  [proto-repl-charts "0.3.1"]
                                  [proto-repl-sayid "0.1.3"]
                                  [rhizome "0.2.9"]]
                   :repl-options {:init-ns user}}})
