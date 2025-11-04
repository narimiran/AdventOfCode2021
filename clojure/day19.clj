(ns day19
  (:require aoc))


(defn- perms [[a b c]]
   [[a b c] [b c a] [c a b]
    [c b (- a)] [b a (- c)] [a c (- b)]

    [a (- b) (- c)] [b (- c) (- a)] [c (- a) (- b)]
    [c (- b) a] [b (- a) c] [a (- c) b]

    [(- a) b (- c)] [(- b) c (- a)] [(- c) a (- b)]
    [(- c) b a] [(- b) a c] [(- a) c b]

    [(- a) (- b) c] [(- b) (- c) a] [(- c) (- a) b]
    [(- c) (- b) (- a)] [(- b) (- a) (- c)] [(- a) (- c) (- b)]])


(defn- all-rotations [beacon]
  (aoc/transpose (map perms beacon)))

(defn- translate [beacon diff]
  (mapv #(aoc/pt-3d+ % diff) beacon))

(defn- count-common [a b]
  (let [pts (into (set a) b)]
    (- (+ (count a) (count b))
       (count pts))))

(defn- find-diff [a b]
  (some
   (fn [[i j]]
     (let [diff (aoc/pt-3d- i j)
           b+diff (translate b diff)]
       (when (>= (count-common a b+diff) 12)
         [diff b+diff])))
   (for [i a , j b] [i j])))

(defn- rotate-scanner [a b]
  (some #(find-diff a %) (all-rotations b)))


(defn- rotate-all-scanners [scanners]
  (loop [[scanner & scanners'] scanners
         acc [(first scanners)]
         ds []]
    (if (nil? scanner)
      [acc ds]
      (if-let [[d rotated-scanner] (some #(rotate-scanner % scanner) acc)]
        (recur scanners' (conj acc rotated-scanner) (conj ds d))
        (recur (conj (vec scanners') scanner) acc ds)))))


(defn- count-beams [scanners]
  (count
   (reduce
    (fn [acc scanner]
      (into acc scanner))
    #{}
    scanners)))

(defn- find-largest-distance [distances]
  (reduce max (for [a distances
                    b distances]
                (aoc/manhattan-3d a b))))

(defn solve [filename]
  (let [data (-> filename
                 aoc/read-input
                 (aoc/parse-paragraphs :ints)
                 (->> (map rest)))
        [beams distances] (rotate-all-scanners data)]
    [(count-beams beams) (find-largest-distance distances)]))


(solve 19)
