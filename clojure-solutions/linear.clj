(defn buildFunction [op]
      (fn [lhs, rhs] (if (vector? lhs) (mapv (buildFunction op) lhs rhs) (op lhs rhs))))

(defn buildPart [op] (fn [lhs, rhs] (mapv (partial op rhs) lhs)))

(def t+ (buildFunction +))
(def t- (buildFunction -))
(def t* (buildFunction *))
(def td (buildFunction /))

(def v+ t+)
(def v- t-)
(def v* t*)
(def vd td)
(def m+ t+)
(def m- t-)
(def m* t*)
(def md td)

(def transpose (fn [matrix] (apply mapv vector matrix)))
(def scalar (fn [v1, v2] (reduce + (v* v1 v2))))

(def v*s (buildPart *))
(def m*v (buildPart scalar))

(def m*m (fn [lhs, rhs] (transpose (mapv (partial m*v lhs) (transpose rhs)))))
(def m*s (buildPart #(v*s %2 %1)))

(def vect
  (fn [v1, v2]
      (vector
        (- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
        (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
        (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0))))))
