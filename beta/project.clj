(defproject beta "0.1.0-SNAPSHOT"
  :dependencies
  [[org.clojure/clojure "1.7.0"]
   [org.clojure/clojurescript "1.7.170"]
   [quil "2.2.6"]
   [reagent "0.5.1"]
   [re-frame "0.5.0"]
   [re-com "0.6.2"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.4.1" :exclusions [cider/cider-nrepl]]  ]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"  ]

  :cljsbuild
  {:builds [{:id "dev"
             :source-paths ["src/cljs"]

             :figwheel {:on-jsload "beta.core/mount-root"}

             :compiler {:main beta.core
                        :output-to "resources/public/js/compiled/app.js"
                        :output-dir "resources/public/js/compiled/out"
                        :asset-path "js/compiled/out"
                        :source-map-timestamp true}}

            {:id "min"
             :source-paths ["src/cljs"]
             :compiler {:main beta.core
                        :output-to "resources/public/js/compiled/app.js"
                        :optimizations :advanced
                        :pretty-print false}}]})
