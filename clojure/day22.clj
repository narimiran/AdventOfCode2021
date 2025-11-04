(ns day22
  (:require aoc
            [clojure.string :as str]))


(defprotocol Cuboidal
  (intersect [this other])
  (is-init? [this])
  (volume [this]))

(defrecord Cuboid [on? x1 x2 y1 y2 z1 z2]
  Cuboidal
  (volume [_]
    (* (inc (- x2 x1))
       (inc (- y2 y1))
       (inc (- z2 z1))))
  (is-init? [_]
    (every? #(<= -50 % 50) [x1 x2 y1 y2 z1 z1]))
  (intersect [_ other]
    (let [nx1 (max x1 (:x1 other))
          nx2 (min x2 (:x2 other))
          ny1 (max y1 (:y1 other))
          ny2 (min y2 (:y2 other))
          nz1 (max z1 (:z1 other))
          nz2 (min z2 (:z2 other))]
      (when (and (<= nx1 nx2)
                 (<= ny1 ny2)
                 (<= nz1 nz2))
        (->Cuboid true nx1 nx2 ny1 ny2 nz1 nz2)))))


(defn- parse-line [line]
  (let [[onoff coords] (str/split line #" ")
        coords (aoc/integers coords)]
    (apply ->Cuboid (= "on" onoff) coords)))


(defn- calc [[head & tail :as cubes]]
  (cond
    (not (seq cubes)) 0
    (not (:on? head)) (recur tail)
    (nil? tail) (volume head)
    :else (let [intersections (->> tail
                                   (map #(intersect % head))
                                   (filter some?))]
            (- (+ (volume head) (calc tail))
               (calc intersections)))))


(defn solve [filename]
  (let [cubes (-> filename
                  aoc/read-input
                  (aoc/parse-lines parse-line))]
    [(calc (filterv is-init? cubes))
     (calc cubes)]))


(solve 22)
