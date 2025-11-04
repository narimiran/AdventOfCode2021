(ns day23
  (:require aoc
            [clojure.data.priority-map :refer [priority-map]]))


(defn- char-list [x] (apply list x))

(def input-1 (mapv char-list [nil nil "CD" nil "CA" nil "BB" nil "DA" nil nil]))
(def input-2 (mapv char-list [nil nil "CDDD" nil "CCBA" nil "BBAB" nil "DACA" nil nil]))

(def output-1 (mapv char-list [nil nil "AA" nil "BB" nil "CC" nil "DD" nil nil]))
(def output-2 (mapv char-list [nil nil "AAAA" nil "BBBB" nil "CCCC" nil "DDDD" nil nil]))

(def rooms {\A 2 \B 4 \C 6 \D 8})
(def homes {2 \A 4 \B 6 \C 8 \D})
(def room-positions #{2 4 6 8})

(def move-costs {\A 1 \B 10 \C 100 \D 1000})

(def ^:dynamic *size* 0)


(defn- can-reach? [board start-idx end-idx]
  (let [a (min start-idx end-idx)
        b (max start-idx end-idx)]
    (every? (fn [x] (or (room-positions x)
                        (empty? (board x))
                        (= start-idx x)))
            (range a (inc b)))))

(defn- home-available? [board piece]
    (every? #{piece} (board (rooms piece))))

(defn- all-moves [board idx piece]
  (->> (range (count board))
       (keep (fn [dest]
               (when (and (not= dest idx)
                          (or (not (room-positions dest)) ; hallway, or
                              (and (= (rooms piece) dest) ; own room available
                                   (home-available? board piece)))
                          (can-reach? board idx dest))
                 dest)))))


(defn- find-moves [board idx [hd & _]]
  (when-let [home (rooms hd)]
    (cond
      (and (can-reach? board idx home) (home-available? board hd)) ; go to own room
      [home]

      (or (not (room-positions idx)) ; can't move from hallway to hallway
          (and (= idx home) (home-available? board hd))) ; already correct position
      nil

      :else
      (all-moves board idx hd))))


(defn- calc-cost [board start-idx end-idx]
  (let [start-col  (board start-idx)
        start-vert (if (room-positions start-idx)
                     (inc (- *size* (count start-col)))
                     0)
        end-col    (board end-idx)
        end-vert   (if (room-positions end-idx)
                     (- *size* (count end-col))
                     0)
        horizontal (abs (- start-idx end-idx))]
     (* (move-costs (first start-col))
        (+ start-vert horizontal end-vert))))

(defn- update-board [board start-idx end-idx]
  (-> board
      (update start-idx rest)
      (update end-idx conj (first (board start-idx)))))

(defn- make-single-move [board idx new-pos]
  (when (some? new-pos)
    [(update-board board idx new-pos)
     (calc-cost board idx new-pos)]))

(defn- move-col [board [idx col]]
  (->> (find-moves board idx col)
       (mapv #(make-single-move board idx %))))


(defn- find-candidates [board cost seen]
  (->> board
       (map-indexed vector)
       (mapcat #(move-col board %))
       (map (fn [[b c]]
              [b (+ cost c)]))
       (filter (fn [[b c]]
                 (< c (seen b 99999))))))


(defn play [board solution]
  (loop [queue (priority-map board 0)
         seen {board 0}]
    (let [[board cost] (peek queue)]
      (if (= solution board) cost
          (let [candidates (find-candidates board cost seen)]
            (recur (into (pop queue) candidates)
                   (into seen candidates)))))))


(defn solve []
  [(binding [*size* 2] (play input-1 output-1))
   (binding [*size* 4] (play input-2 output-2))])


(solve)
