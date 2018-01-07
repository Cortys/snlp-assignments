(defproject snlp-assignment4 "0.1.0-SNAPSHOT"
  :description "SNLP assignment 4."

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [lambda-ml "0.1.0"]]

  :profiles {:dev {:source-paths ["dev" "src"]
                   :dependencies [[proto-repl "0.3.1"]
                                  [proto-repl-charts "0.3.1"]
                                  [proto-repl-sayid "0.1.3"]]
                   :repl-options {:init-ns user}}

             :task2a {:main task2a
                      :aot :all
                      :jar-name "task2a-small.jar"
                      :uberjar-name "task2a.jar"
                      :uberjar-exclusions [#"clojure/(test|pprint|inspector|reflect|repl|xml)/.*"]}
             :task2b {:main task2b
                      :aot :all
                      :jar-name "task2b-small.jar"
                      :uberjar-name "task2b.jar"
                      :uberjar-exclusions [#"clojure/(test|pprint|inspector|reflect|repl|xml)/.*"]}}

  :aliases {"run-task2a" ["with-profile" "task2a" "run"]
            "run-task2b" ["with-profile" "task2b" "run"]})
