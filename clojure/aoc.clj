;; # Helper functions for AoC

^{:nextjournal.clerk/visibility {:code :hide :result :hide}}
(ns aoc
  {:nextjournal.clerk/visibility {:result :hide}
   :nextjournal.clerk/auto-expand-results? true
   :nextjournal.clerk/toc :collapsed}
  (:require [clojure.string :as str]
            [clojure.math :as math]
            [clojure.data.int-map :as i]
            [clojure.data.priority-map :refer [priority-map]]
            [nextjournal.clerk :as clerk]))




;; ## Reading input files
;;
;; The inputs are always in the `../inputs/` folder,
;; named `dd` and have a `.txt` extension.
;; Let's simplify reading them:
;;
(defn read-input [file]
  (let [name (if (int? file)
               (format "%02d" file)
               file)]
    (slurp (str "../inputs/" name ".txt"))))







;; ## Input parsing
;;
(defn integers
  [s & {:keys [negative?]
        :or {negative? true}}]
  (mapv parse-long
        (re-seq (if negative? #"-?\d+" #"\d+") s)))

(defn string->digits [s]
  (->> (str/split s #"")
       (map parse-long)
       (filterv some?)))

(defn parse-line [line & [parse-fn word-sep]]
  (let [f (case parse-fn
            :int    parse-long
            :ints   integers
            :digits string->digits
            :chars  vec
            :words  #(str/split % (or word-sep #" "))
            nil     identity
            parse-fn)]
    (f line)))

;; The `parse-line` is doing some heavy lifting here.\
;; The `parse-fn` parameter there is the key to the versatility:
;; it makes possible to parse all AoC inputs, either by the typical
;; functions (parsing integers, extracting integers from a line, creating a
;; vector of integers, vector of characters, splitting lines into words, or
;; keeping everything as it is) or by passing it a custom function, specially
;; crafted for the task at hand.

^{:nextjournal.clerk/visibility {:code :fold
                                 :result :show}}
(let [lines ["123" "abc12def34" "abc12def34" "ab1c" "abc def"]
      fns [:int :ints :digits :chars :words]
      results (map (fn [line parse-fn] (parse-line line parse-fn))
                   lines
                   fns)]
  (clerk/html
   [:div.flex.justify-center
    (clerk/table {"Line" lines
                  "Parse function" fns
                  "Result" results})]))


;; Now, parsing a whole input file is just a matter of applying that function
;; to each line of the input.\
;; On some rare occasions, the input is split into paragraphs by having an
;; empty line between different parts of input.
;; We have a function for that too:
;;
(defn parse-lines
  [s & [parse-fn {:keys [word-sep nl-sep]}]]
  (mapv #(parse-line % parse-fn word-sep)
        (str/split s (or nl-sep #"\n"))))

(defn parse-paragraphs
  [input & [parse-fn word-sep]]
  (mapv #(parse-lines % parse-fn {:word-sep word-sep})
        (parse-lines input nil {:nl-sep #"\n\n"})))







;; ## Grids
;;
;; AoC wouldn't be AoC if there aren't many tasks where you're given a 2D
;; grid (sometimes even 3D).
;;
;; It is important to have a usable representation of a grid.
;; Sometimes we need to know a character at each coordinate (`point-map`),
;; the other times only the coordinates are important (`point-set`).
;; We can only keep the coordinates that satisfy the `pred` function.

;; The hashed variants are used when trying to optimize for speed.
;;
(defn grid->point-map
  ([v] (grid->point-map v identity nil))
  ([v pred] (grid->point-map v pred nil))
  ([v pred mult]
   (into (if mult (i/int-map) {})
         (for [[^long y line] (map-indexed vector v)
               [^long x char] (map-indexed vector line)
               :when (pred char)]
           (if mult
             [(+ (* y ^long mult) x) char]
             [[x y] char])))))

(defn grid->hashed-point-map
  ([v] (grid->point-map v identity 1000))
  ([v pred] (grid->point-map v pred 1000))
  ([v pred mult] (grid->point-map v pred mult)))


(defn grid->point-set
  ([v] (grid->point-set v identity nil))
  ([v pred] (grid->point-set v pred nil))
  ([v pred mult]
   (into (if mult (i/dense-int-set) #{})
         (for [[^long y line] (map-indexed vector v)
               [^long x char] (map-indexed vector line)
               :when (pred char)]
           (if mult
             (+ (* y ^long mult) x)
             [x y])))))

(defn grid->hashed-point-set
  ([v] (grid->point-set v identity 1000))
  ([v pred] (grid->point-set v pred 1000))
  ([v pred mult] (grid->point-set v pred mult)))


;; Sometimes we need to inspect a grid.
;; With `points->lines`, we create a list of points in each line.
;;
(defn points->lines [points]
  (if (map? points) (points->lines (set (keys points)))
      (let [x-lim (inc (reduce max (map first points)))
            y-lim (inc (reduce max (map second points)))]
        (for [y (range y-lim)]
          (str/join (for [x (range x-lim)]
                      (if (points [x y])
                        \█ \space)))))))



;; ### 2D grids
;;
;; We also need some functions to navigate through the grids or do
;; stuff with the points in them:
;;
(defn manhattan ^long
  ([pt] (manhattan pt [0 0]))
  ([[^long x1 ^long y1] [^long x2 ^long y2]]
   (+ (abs (- x1 x2))
      (abs (- y1 y2)))))

(defn pt+ ^longs [[^long x1 ^long y1] [^long x2 ^long y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn pt- ^longs [[^long x1 ^long y1] [^long x2 ^long y2]]
  [(- x1 x2) (- y1 y2)])

(defn pt* ^longs [^long magnitude [^long x ^long y]]
  [(* magnitude x) (* magnitude y)])

(defn inside?
  ([size [x y]] (inside? size size x y))
  ([size x y]   (inside? size size x y))
  ([size-x size-y x y]
   (and (< -1 x size-x)
        (< -1 y size-y))))


(defn neighbours ^longs
  ([^long amount pt] (neighbours amount pt identity))
  ([^long amount [^long x ^long y] cnd]
   (for [^long dy [-1 0 1]
         ^long dx [-1 0 1]
         :when (case amount
                 4 (odd? (- dx dy))
                 5 (<= (+ (abs dx) (abs dy)) 1)
                 8 (not= dx dy 0)
                 9 true)
         :let [nb [(+ x dx) (+ y dy)]]
         :when (cnd nb)]
     nb)))


^{:nextjournal.clerk/visibility {:code :fold
                                 :result :show}}
(let [point [0 0]
      nbs [4 5 8 9]
      results (map #(neighbours % point) nbs)
      axis-template {:ticks ""
                     :showticklabels false
                     :showgrid false
                     :zeroline false
                     :range [-2 2]}]
  (clerk/row
   (for [res results]
     (clerk/plotly {:config {:displayModeBar false
                             :displayLogo false}
                    :data [{:x (map first res)
                            :y (map second res)
                            :type :scatter
                            :mode :markers
                            :marker {:size 12}
                            :showscale false}]
                    :layout {:xaxis axis-template
                             :yaxis axis-template
                             :width 100
                             :height 100
                             :margin {:l 0 :r 0 :t 0 :b 0}}}))))



;; ### 3D grids
;;
;; Sometimes we also get 3D-grids, so here are some simplified
;; 3D-variants of the functions above.
;;
(defn manhattan-3d ^long
  ([p] (manhattan-3d p [0 0 0]))
  ([[^long x1 ^long y1 ^long z1] [^long x2 ^long y2 ^long z2]]
   (+ (abs (- x1 x2))
      (abs (- y1 y2))
      (abs (- z1 z2)))))

(defn pt-3d+ ^longs [[^long x1 ^long y1 ^long z1]
                     [^long x2 ^long y2 ^long z2]]
  [(+ x1 x2) (+ y1 y2) (+ z1 z2)])

(defn pt-3d- ^longs [[^long x1 ^long y1 ^long z1]
                     [^long x2 ^long y2 ^long z2]]
  [(- x1 x2) (- y1 y2) (- z1 z2)])

(defn neighbours-3d [[^long x ^long y ^long z]]
  [[(dec x) y z] [(inc x) y z]
   [x (dec y) z] [x (inc y) z]
   [x y (dec z)] [x y (inc z)]])

(defn inside-3d?
  ([size [x y z]] (inside? size x y z))
  ([size x y z]
   (and (< -1 x size)
        (< -1 y size)
        (< -1 z size))))







;; ## Graph traversal
;;
;; Graph traversal problems are relatively frequent in AoC,
;; but this is the first time I'm writing a pre-defined helper function
;; for them.
;; It remains to be seen how useful it'll be for specific tasks with
;; their specific needs: I feel like in the recent years the graph traversal
;; tasks always had some gotcha which made it harder to use a general algorithm,
;; rather than a specific one written for the task at hand.
;;
;; If this gets used, the implementation details will probably change,
;; depending on the specific tasks.
;; (As if the `traverse` function is not already way too long and complicated.)
;;
;; All four algorithms (`DFS`, `BFS`, `Dijkstra`, `A*`) share the same logic,
;; the difference is in a `queue` type.
;;
(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn- build-path [current seen]
  (loop [curr current
         path nil]
    (if (or (nil? curr) (= :start curr))
      path
      (recur (first (seen curr)) (conj path curr)))))


(defn- traverse [algo {:keys [start end walls size size-x size-y
                              nb-func nb-num nb-cond end-cond
                              cost-fn heuristic steps-limit
                              allow-revisits? side-effect]
                       :or {nb-num 4
                            walls  #{}
                            nb-cond (constantly true)
                            cost-fn (constantly 1)
                            side-effect (constantly nil)
                            steps-limit ##Inf
                            end-cond  #(= end %)
                            heuristic (if end
                                        (fn [pt] (manhattan pt end))
                                        (constantly 0))}}]
  (let [inbounds? (cond
                    size #(inside? size %)
                    (and size-x size-y) (fn [[x y]] (inside? size-x size-y x y))
                    :else (constantly true))
        nb-filter (every-pred inbounds? (complement walls) nb-cond)]
    (loop [queue (case algo
                   :dfs    (list [start 0])
                   :bfs    (conj empty-queue [start 0])
                   :dijk   (priority-map start 0)
                   :a-star (priority-map [start 0] (heuristic start))
                   (throw (Exception. "unknown graph algorithm")))
           seen {start [:start 0]}]
      (let [[current steps] (case algo
                              :a-star (first (peek queue))
                              (peek queue))
            nb-cost     (fn [pt] (+ steps (cost-fn current pt)))
            seen-filter (fn [pt] (or allow-revisits?
                                     (< (nb-cost pt) (or (second (seen pt))
                                                         steps-limit))))]
        (side-effect {:pt current
                      :steps steps
                      :seen seen
                      :queue queue})
        (cond
          (or (nil? current)
              (>= steps steps-limit)
              (end-cond current)) {:steps   steps
                                   :seen    (set (keys seen))
                                   :costs   (into {} (map (fn [[k [_ s]]] [k s]) seen))
                                   :count   (count seen)
                                   :queue   queue
                                   :path    (build-path current seen)
                                   :current current}
          :else
          (let [nbs (if nb-func
                      (filter (every-pred nb-filter seen-filter) (nb-func current))
                      (neighbours nb-num current (every-pred nb-filter seen-filter)))
                nbs+costs (map (fn [pt] [pt (nb-cost pt)]) nbs)]
            (recur
             (reduce (fn [q [pt cost]]
                       (case algo
                         :dfs    (conj q [pt cost])
                         :bfs    (conj q [pt cost])
                         :dijk   (assoc q pt cost)
                         :a-star (assoc q [pt cost] (+ cost (heuristic pt)))))
                     (pop queue)
                     nbs+costs)
             (reduce (fn [s [pt cost]] (assoc s pt [current cost]))
                     seen
                     nbs+costs))))))))


(defn dfs [options]
  (traverse :dfs options))

(defn bfs [options]
  (traverse :bfs options))

(defn a-star [options]
  (traverse :a-star options))

(defn dijkstra [options]
  (traverse :dijk options))








;; ## Utilities
;;
;; Functions for some common AoC stuff.
;;
;; - `transpose`: We often need to iterate through columns, instead of rows.
;;   This transposes the matrix.
;; - `invert-tree`: Connections between nodes in a reverse order.
;; - `count-if`: In many tasks we need to apply some condition and then count
;;   the number of elements which satisfy it. This should be slightly faster
;;   (to type, at least) than `(count (filter ...))`.
;; - `sum-map`: Similarly, in some tasks we need to apply a function to
;;   each row, and then take a sum of the results.
;; - `find-first`: Returns the first element which satisfies a predicate.
;;
(defn transpose [matrix]
  (apply mapv vector matrix))

(defn invert-tree [tree]
  (reduce-kv
   (fn [acc k vs]
     (reduce (fn [acc v]
               (update acc v conj k))
             acc
             vs))
   {}
   tree))

(defn count-if ^long [pred xs]
  (reduce
   (fn [^long acc x]
     (if (pred x) (inc acc) acc))
   0
   xs))

(defmacro do-count
  {:clj-kondo/lint-as 'clojure.core/doseq}
  ;; Similar to the `count-if` function above, but allows for a more
  ;; elaborate predicate, i.e. everything that the `doseq` built-in does.
  [seq-exprs]
  `(let [counter# (atom 0)]
     (doseq ~seq-exprs
       (swap! counter# inc))
     @counter#))

(defn sum-map [f xs]
  (transduce (map f) + xs))

(defn sum-map-indexed [f xs]
  (transduce (map-indexed f) + xs))

(defn sum-pmap [f xs]
  (reduce + (pmap f xs)))

(defn prod-map [f xs]
  (transduce (map f) * xs))

(defn find-first [pred xs]
  (reduce
   (fn [_ x]
     (when (pred x) (reduced x)))
   nil
   xs))


(defn gcd ^long
  ([] 1)
  ([x] x)
  ([^long a ^long b]
   (if (zero? b) a
       (recur b (rem a b)))))

(defn lcm ^long
  ([] 1)
  ([x] x)
  ([^long a ^long b]
   (/ (* a b)
      ^long (gcd a b))))


(defn count-digits [n]
  ;; Slightly faster than `((comp count str) n)`.
  (cond
    (zero? n) 1
    (neg? n) (count-digits (- n))
    :else (-> n
              math/log10
              math/floor
              long
              inc)))









;; ## Need for Speed
;;
;; These functions should be faster than their counterparts in the Clojure's
;; standard library.
;;
(defn none? [pred xs]
  ;; Faster version of `not-any?`.
  (reduce
   (fn [acc x]
     (if (pred x)
       (reduced false)
       acc))
   true
   xs))

(defn array-none? [pred ^longs arr]
  ;; Much much faster version of `not-any?` for long-arrays.
  (loop [idx (dec (alength arr))
         acc true]
    (if (neg? idx)
      acc
      (if (pred (aget arr idx))
        false
        (recur (dec idx) acc)))))


(defn update-2 [m k1 k2 f]
  ;; usually faster than the `update-in` built-in
  (let [m2 (m k1)
        v (m2 k2)]
    (assoc m k1 (assoc m2 k2 (f v)))))

(defn assoc-2 [m k1 k2 v]
  ;; usually faster than the `assoc-in` built-in
  (let [m2 (m k1)]
    (assoc m k1 (assoc m2 k2 v))))

(defn assoc-3 [m k1 k2 k3 v]
  ;; usually faster than the `assoc-in` built-in
  (let [m2 (m k1)]
    (assoc m k1 (assoc-2 m2 k2 k3 v))))
