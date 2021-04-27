(defn create [f] (fn [& operands] (fn [args] (apply f (map #(% args) operands)))))

(def add (create +))
(def subtract (create -))
(def multiply (create *))
(def negate (create -))

(def divide (create (fn [lhs, rhs] (/ (double lhs) (double rhs)))))

(defn constant [value] (fn [args] (double value)))
(defn variable [name] (fn [args] (double (args name))))


(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate})
(def numberOfArgs {'+ 2 '- 2 '* 2 '/ 2 'negate 1})

(declare parseArg)

(defn parseBrace
  ([brace] (if (sequential? brace)
             (apply (get operations (nth brace 0)) (map (partial parseBrace brace) (range 1 (+ (get numberOfArgs (nth brace 0)) 1))))
             (parseArg brace)))
  ([brace, num] (parseArg (nth brace num))))

(defn parseArg [token]
  (cond
    (sequential? token) (parseBrace token)
    (symbol? token) (variable (str token))
    :else (constant (double token))))

(defn parseFunction [expression] (parseBrace (read-string expression)))
