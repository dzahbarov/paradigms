(defn create [f] (fn [& operands] (fn [args] (apply f (map #(% args) operands)))))

(def add (create +))
(def subtract (create -))
(def multiply (create *))
(def negate (create -))
(def divide (create (fn [lhs, rhs] (/ (double lhs) (double rhs)))))
(def sinh (create #(Math/sinh %)))
(def cosh (create #(Math/cosh %)))

(defn constant [value] (fn [args] (double value)))
(defn variable [name] (fn [args] (double (args name))))

(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate 'sinh sinh 'cosh cosh})

(defn parse [token]
      (cond
        (sequential? token) (apply (get operations (peek token)) (map parse (pop token)))
        (symbol? token) (variable (str token))
        :else (constant (double token))))

(defn parseFunction [expression] (parse (read-string expression)))
