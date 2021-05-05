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
    (sequential? token) (apply (get operations (first token)) (map parse (rest token)))
    (symbol? token) (variable (str token))
    :else (constant (double token))))

(defn parseFunction [expression] (parse (read-string expression)))
;------------------------------------------------------------------------------------------
(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (proto-get (obj :proto) key)
    :else nil))

(defn proto-call [obj key & args] (apply (proto-get obj key) obj args))

(defn field [key] #(proto-get % key))

(defn method [key] #(apply proto-call % key %&))

(defn constructor
  ([ctor, proto, func, sym, dif] (fn [& args] (apply ctor {:proto proto, :func func :sym sym :diffOp dif} args)))
  ([ctor, proto] (fn [& args] (apply ctor {:proto proto} args))))

(def _operands (field :operands))
(def _func (field :func))
(def _sym (field :sym))
(def _value (field :value))
(def _name (field :name))
(def _diffOp (field :diffOp))
(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))

(defn Operation [this, & operands] (assoc this :operands operands))

(def OperationPrototype
  {:evaluate (fn [this, args] (apply (_func this) (map #(evaluate % args) (_operands this))))
   :toString (fn [this] (str "(" (_sym this) " " (clojure.string/join " " (map #(toString %) (_operands this))) ")"))
   :diff     (fn [this, var] (apply (partial (_diffOp this) var) (_operands this)))})

(defn createOperation [f, sym, diffOp] (constructor Operation OperationPrototype f sym diffOp))

(def Add (createOperation + "+" #(Add (diff %2 %1) (diff %3 %1))))
(def Subtract (createOperation - "-" #(Subtract (diff %2 %1) (diff %3 %1))))
(def Multiply (createOperation * "*" #(Add (Multiply (diff %2 %1) %3) (Multiply %2 (diff %3 %1)))))
(def Divide (createOperation #(/ (double %1) (double %2)) "/" #(Divide (Subtract (Multiply (diff %2 %1) %3) (Multiply %2 (diff %3 %1))) (Multiply %3 %3))))
(def Negate (createOperation - "negate" #(Negate (diff %2 %1))))
;------------------------------------------------
(declare Cosh)

(def Sinh (createOperation #(Math/sinh %) "sinh" #(Multiply (Cosh %2) (diff %2 %1))))
(def Cosh (createOperation #(Math/cosh %) "cosh" #(Multiply (Sinh %2) (diff %2 %1))))

(declare Constant)

(def ConstantPrototype
  {:evaluate (fn [this, args] (_value this))
   :toString (fn [this] (str (format "%.1f" (_value this))))
   :diff     (fn [this, var] (Constant 0))})

(def Constant (constructor #(assoc %1 :value %2) ConstantPrototype))

(def VariablePrototype
  {:evaluate (fn [this, args] (args (_name this)))
   :toString (fn [this] (_name this))
   :diff     (fn [this, var] (if (= var (_name this)) (Constant 1) (Constant 0)))})

(def Variable (constructor #(assoc %1 :name %2) VariablePrototype))

(def operationsObj {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sinh Sinh 'cosh Cosh})

(defn parseObj [token]
  (cond
    (sequential? token) (apply (get operationsObj (first token)) (map parseObj (rest token)))
    (symbol? token) (Variable (str token))
    :else (Constant (double token))))

(defn parseObject [expression] (parseObj (read-string expression)))