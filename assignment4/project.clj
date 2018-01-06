(defproject snlp-assignment4 "0.1.0-SNAPSHOT"
  :description "SNLP assignment 4."

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [lambda-ml "0.1.0"]]

  :profiles {:task2 {:main task2
                     :aot [task2]
                     :jar-name "task2-small.jar"
                     :uberjar-name "task2.jar"
                     :uberjar-exclusions [#"clojure/(test|pprint|inspector|reflect|repl|xml)/.*"]}}

  :aliases {"run-task2" ["with-profile" "task2" "run"]})
