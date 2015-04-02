(ns travel-guide.core
  (:require [clojure.browser.repl :as repl]))

(defonce conn
  (repl/connect "http://localhost:9000/repl"))

(defn get-template
  [scope]
  (str "<h2>" (:country-name scope) "</h2>"
       "<div>"
         "<img class='country-image' src=" (:image-src scope) ">"
       "</div>"))

(defn update-model
  []
  (let [country-name (.country (js/Chance.) (js-obj "full" true))
        encoded-country-name (js/encodeURIComponent country-name)]
    (-> (js/fetch (str "https://country-images.herokuapp.com/image?q="
                       encoded-country-name))
        (.then #(.json %))
        (.then (fn [data]
                 {:country-name country-name
                  :image-src (aget data "url")})))))

(defn update-component
  [component]
  (let [component-style (aget component "style")]
    (aset component-style "opacity" 0)
    (.then
      (update-model)
      (fn [model]
        (aset component "innerHTML" (get-template model))
        (aset component-style "opacity" 1)))))

(.addEventListener
  js/window
  "load"
  (fn []
    (let [component (.getElementById js/document "js-component-container")]
      (update-component component)
      (.addEventListener
        (.getElementById js/document "js-update-button")
        "click"
        (partial update-component component)))))
