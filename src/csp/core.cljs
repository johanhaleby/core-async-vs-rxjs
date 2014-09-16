(ns csp.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :as async
             :refer [>! <! put! chan alts!]]
            [goog.events :as events]
            [goog.dom.classes :as classes])
  (:import [goog.events EventType]))

(enable-console-print!)

;; =============================================================================
;; Utilities

(defn by-id
  "Short-hand for document.getElementById(id)"
  [id]
  (.getElementById js/document id))

(defn events->chan
  "Given a target DOM element and event type return a channel of
  observed events. Can supply the channel to receive events as third
  optional argument."
  ([el event-type] (events->chan el event-type (chan)))
  ([el event-type c]
   (events/listen el event-type
                  (fn [e] (put! c e)))
   c))

(defn set-html!
  "Given a CSS id, replace the matching DOM element's
  content with the supplied string."
  [id s]
  (set! (.-innerHTML (by-id id)) s))

;; =============================================================================
;; Clojure Channel Example


(defn ex1 []
  (let [a (events->chan (by-id "ex1-button-a") EventType.CLICK (chan 1 (map (constantly :a))))
        b (events->chan (by-id "ex1-button-b") EventType.CLICK (chan 1 (map (constantly :b))))
        combination-max-time 5000
        secret-combination [:a :b :b :a :b :a]
        set-html! (partial set-html! "ex1-card")]
    (go
      (loop [correct-clicks []
             timeout (async/timeout combination-max-time)]
        (let [[val channel] (alts! [a b timeout])
              clicks (conj correct-clicks val)]
          (cond
            (= channel timeout) (do (set-html! "You're not fast enough, try again!") (recur [] (async/timeout combination-max-time)))
            (= clicks secret-combination) (do (set-html! "Combination unlocked!") (recur [] (async/timeout combination-max-time)))
            (and (= val (first secret-combination)) (zero? (count correct-clicks))) (do (set-html! clicks) (recur clicks (async/timeout combination-max-time))) ;Reset timeout when first match!
            (= val (nth secret-combination (count correct-clicks))) (do (set-html! clicks) (recur clicks timeout))
            :else
            (do (set-html! clicks) (recur [] timeout))))))))

(ex1)