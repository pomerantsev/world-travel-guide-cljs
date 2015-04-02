(require 'cljs.closure)

(cljs.closure/watch
  "src"
  {:main 'travel-guide.core
   :output-to "out/main.js"})
