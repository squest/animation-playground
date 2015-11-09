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

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  (q/background 210)
  ; setup function returns initial state. It contains
  ; circle color and position.
  {:color 0
   :vx 80
   :vy 0
   :sx 50
   :sy 400})

(defn update-state
  [state]
  ; Update sketch state by changing circle color and position.
  (let [{:keys [vx vy sx sy]} state
        g -20 t 0.1]
    {:color (mod (+ (:color state) 0.7) 255)
     :vx    vx
     :vy (+ vy (* g t))
     :sx (+ sx (* vx t))
     :sy (+ sy (* vy t))}))

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 210)
  ; Set circle color.
  (q/fill (:color state) 255 255)
  ; Calculate x and y coordinates of the circle.
  (let [{:keys [sx sy vx vy]} state]
    ; Move origin point to the center of the sketch.
    (q/text (str "Vx = " vx " Sy = " sy) 1000 50)
    (q/ellipse sx (- 450 sy) 30 30)))

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

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )


