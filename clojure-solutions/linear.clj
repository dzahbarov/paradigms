(defn buildFunction [vecOp numOp]
      (fn [lhs, rhs] (if (vector? lhs) (mapv vecOp lhs rhs) (numOp lhs rhs))))

(defn t+ [lhs, rhs] ((buildFunction t+ +) lhs rhs))
(defn t- [lhs, rhs] ((buildFunction t- -) lhs rhs))
(defn t* [lhs, rhs] ((buildFunction t* *) lhs rhs))
(defn td [lhs, rhs] ((buildFunction td /) lhs rhs))

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
(def v*s (fn [v, s] (mapv (partial * s) v)))
(def m*v (fn [matrix, vec] (mapv (partial scalar vec) matrix)))
(def m*m (fn [lhs, rhs] (transpose (mapv (partial m*v lhs) (transpose rhs)))))
(def m*s (fn [m, s] (mapv v*s m (vec (to-array (repeat (count m) s))))))

(def vect
  (fn [v1, v2]
      (vector
        (- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
        (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
        (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0))))))
