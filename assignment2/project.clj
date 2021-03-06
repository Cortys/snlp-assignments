(defproject snlp-assignment2 "0.1.0-SNAPSHOT"
  :description "SNLP homework assignment 2."

  :dependencies [[org.clojure/clojure "1.8.0"]]

  :profiles {:task2 {:main task2
                     :aot [task2]
                     :jar-name "task2-small.jar"
                     :uberjar-name "task2.jar"
                     :uberjar-exclusions [#"clojure/(test|pprint|inspector|reflect|repl|xml)/.*"]}
             :task3 {:main task3
                     :aot [task3]
                     :jar-name "task3-small.jar"
                     :uberjar-name "task3.jar"
                     :uberjar-exclusions [#"clojure/(test|pprint|inspector|reflect|repl|xml)/.*"]}}
  :aliases {"run-task2" ["with-profile" "task2" "run"]
            "run-task3" ["with-profile" "task3" "run"]})
