(ns beta.core
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [beta.handlers]
    [beta.subs]
    [beta.views :as views]
    [quil.core :as q :include-macros true]
    [quil.middleware :as m]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))

;;; PENTING
(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  (q/background 244)
  ; setup function returns initial state. It contains
  ; circle color and position.
  (for [angle (range 2 90 1.5)]
    (let [v 120]
      {:color (* 10 angle)
       :vx    (* v (q/cos (q/radians angle)))
       :vy    (* v (q/sin (q/radians angle)))
       :t     0
       :sx 5
       :sy 0
       :sxo    5
       :syo   0})))

(defn update-state
  [state]
  ; Update sketch state by changing circle color and position.
  (let [g -20]
    (if (> (:t (first state)) 15)
      (q/exit)
      (for [{:keys [sxo syo vx vy sx sy t color]} state]
        (let [res {:color color
                   :t     (+ 0.05 t)
                   :vx    vx
                   :vy    vy
                   :sx    (+ sxo (* vx t))
                   :sy    (+ syo (* vy t) (* 0.5 g t t))
                   :sxo   sxo
                   :syo   syo}]
          (if (< syo 0)
            (merge res {:t 0 :sxo sx :syo sy :sx sx :sy sy})
            res))))))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  ; (q/background 210)
  ; Set circle color.
  (do (q/stroke 100)
      (q/line 20 450 1200 450))
  ; Calculate x and y coordinates of the circle.
  (doseq [{:keys [sx sy color]} state]
    (q/fill color 255 255)
    (q/ellipse sx (- 450 (q/abs sy)) 2 2)))

(q/defsketch
  beta
  :host "beta"
  :size [1160 1500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])

;; penting

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload


(defn on-js-reload []
  (js/alert "woi"))


