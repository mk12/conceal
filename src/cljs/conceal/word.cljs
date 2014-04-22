;;; Copyright 2014 Mitchell Kember. Subject to the MIT License.

(ns conceal.word)

(defn empty-grid
  "Creates a grid of n rows and m columns of empty cells."
  [n m]
  {:rows n :cols m :words [] :data (vec (repeat (* n m) :empty))})

(defn rand-letter
  "Returns a random lowercase alphabetic character."
  []
  (char (+ 97 (rand-int 26))))

(defn empty->letter
  "Returns x, or a random letter if x is :empty."
  [x]
  (if (= x :empty) (rand-letter) x))

(defn fill-grid
  "Fills the empty cells in a grid with random letters."
  [grid]
  (update-in grid [:data] (partial map empty->letter)))

(defn nth-index
  "Returns the index in a grid with cols columns of the nth letter in a word
  starting from (r,c) and going in the direction of the vector (dr,dc)."
  [n cols [r c dr dc]]
  (+ (* cols (+ r (* n dr)))
     (+ c (* n dc))))

(defn nth-cell
  "Returns the nth cell in the grid for a word placed with posdir. Returns nil
  if the cell would be out of bounds."
  [n {:keys [cols data]} posdir]
  (get data (nth-index n cols posdir)))

(defn every-indexed?
  "Returns true if (pred i x) is logical true for every index i and item x in
  coll, otherwise returns false."
  [pred coll]
  (letfn [(every-i? [idx coll]
            (or (nil? (seq coll))
                (and (pred idx (first coll))
                     (recur (inc idx) (next coll)))))]
    (every-i? 0 coll)))

(defn word-fits?
  "Returns whether it is possible to place the given word in the grid with
  posdir. A letter can only be placed in a cell if the cell is within bounds and
  either the cell is empty or it already contains the right letter."
  [grid word posdir]
  {:pre [grid posdir]}
  (every-indexed?
    (fn [i letter]
      (let [c (nth-cell i grid posdir)]
        (#{:empty letter} c)))
    word))

(defn add-word
  "Adds a word into the grid with the position and direction given by posdir.
  Assumes the word will not go out of bounds."
  [{:keys [cols words data] :as grid} word posdir]
  {:pre [grid posdir]}
  (if (empty? word)
    grid
    (let [word-inds (range (count word))
          grid-inds (map #(nth-index % cols posdir) word-inds)
          mappings (interleave grid-inds word)
          new-data (apply assoc data mappings)
          new-words (conj words word)]
      (assoc grid :words new-words :data new-data))))

(defn irand
  "Returns a random int between a (defaults to zero) and b (exclusive)."
  ([b] (rand-int b))
  ([a b] (+ a (rand-int (- b a)))))

(defn rand-pos-1d
  "Generates a random position along a dimension of size dim for a word of
  length wlen going in the direction dir."
  [dim wlen dir]
  (case dir
    0 (irand dim)
    1 (irand (inc (- dim wlen)))
    -1 (irand (dec wlen) dim)))

(defn rand-posdir
  "Generates a random valid position-direction vector (row, column, row-dir,
  column-dir) for a word of length wlen on the given grid."
  [{:keys [rows cols]} wlen]
  (let [dr (rand-nth [-1 0 1])
        dc (if (zero? dr)
             (rand-nth [-1 1])
             (rand-nth [-1 0 1]))
        r (rand-pos-1d rows wlen dr)
        c (rand-pos-1d cols wlen dc)]
    [r c dr dc]))

(defn try-placing-word
  "Attempts to randomly place the word. If successful, returns the new grid. If
  still unsuccessful after give-up tries, returns the original grid."
  [grid word give-up]
  (if (or (nil? grid) (zero? give-up))
    grid
    (let [posdir (rand-posdir grid (count word))]
      (if (word-fits? grid word posdir)
        (add-word grid word posdir)
        (recur grid word (dec give-up))))))

(defn make-wordsearch
  "Generates a word search grid with n rows and m columns. Attempts to place
  each word in the word search, but gives up after give-up tries."
  [n m give-up words]
  (let [place (fn [g w] (try-placing-word g w give-up))
        grid (reduce place (empty-grid n m) words)]
    (fill-grid grid)))

(defn grid->dom
  "Converts a grid into a dommy-compatible data structure representing a table."
  [{:keys [cols data]}]
  (letfn [(make-row [r] (into [:tr] (map make-cell r)))
          (make-cell [c] [:td (str c)])]
    (into [:table] (map make-row (partition cols data)))))
