(defn create [f] (fn [& operands] (fn [args] (apply f (map #(% args) operands)))))

(def add (create +))
(def subtract (create -))
(def multiply (create *))
(def negate (create -))
(def divide (create (fn ([e] (/ (double 1) (double e)))
                        ([e & rest] (reduce #(/ (double %1) (double %2)) e rest)))))
(def sinh (create #(Math/sinh %)))
(def cosh (create #(Math/cosh %)))

;36-37 mod
(def sum (create (fn [& args] (reduce + args))))
(def avg (create (fn [& args] (/ (reduce + args) (count args)))))

;38-39 mod
(def mean (create (fn [& args] (reduce + (map (partial #(* %2 %1) (/ 1 (count args))) args)))))
(def varn (create (fn [& args] (- (reduce + (map (partial #(* (Math/pow %2 2) %1) (/ 1 (count args))) args))
                                  (Math/pow (reduce + (map (partial #(* %2 %1) (/ 1 (count args))) args)) 2)))))

(defn constant [value] (fn [args] (double value)))
(defn variable [name] (fn [args] (double (args name))))

(def operations {'+ add '- subtract '* multiply '/ divide 'negate negate 'sinh sinh 'cosh cosh 'mean mean 'varn varn 'sum sum 'avg avg})

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
(def toStringSuffix (method :toStringSuffix))
(def diff (method :diff))

(defn Operation [this, & operands] (assoc this :operands operands))

(def OperationPrototype
  {:evaluate       (fn [this, args] (apply (_func this) (map #(evaluate % args) (_operands this))))
   :toString       (fn [this] (str "(" (_sym this) " " (clojure.string/join " " (map #(toString %) (_operands this))) ")"))
   :diff           (fn [this, var] (apply (partial (_diffOp this) var) (_operands this)))
   :toStringSuffix (fn [this] (str "(" (clojure.string/join " " (map #(toStringSuffix %) (_operands this))) " " (_sym this) ")"))})

(defn createOperation [f, sym, diffOp] (constructor Operation OperationPrototype f sym diffOp))

(def Add (createOperation + "+" #(Add (diff %2 %1) (diff %3 %1))))
(def Subtract (createOperation - "-" #(Subtract (diff %2 %1) (diff %3 %1))))
(def Multiply (createOperation * "*" #(Add (Multiply (diff %2 %1) %3) (Multiply %2 (diff %3 %1)))))
(def Divide (createOperation (fn ([e] (/ (double 1) (double e)))
                                 ([e & other] (reduce #(/ (double %1) (double %2)) e other)))
                             "/" #(Divide (Subtract (Multiply (diff %2 %1) %3) (Multiply %2 (diff %3 %1))) (Multiply %3 %3))))
(def Negate (createOperation - "negate" #(Negate (diff %2 %1))))
;------------------------------------------------
(declare Cosh)

(def Sinh (createOperation #(Math/sinh %) "sinh" #(Multiply (Cosh %2) (diff %2 %1))))
(def Cosh (createOperation #(Math/cosh %) "cosh" #(Multiply (Sinh %2) (diff %2 %1))))

(def Sum (createOperation (fn [& args] (reduce + args)) "sum" identity))
(def Avg (createOperation (fn [& args] (/ (reduce + args) (count args))) "avg" identity))
;----------------------------------------------------
(def And (createOperation #(if (and (> %1 0) (> %2 0)) 1 0) "&&" identity))
(def Or (createOperation #(if (or (> %1 0) (> %2 0)) 1 0) "||" identity))
(def Xor (createOperation #(if (or (and (> %1 0) (<= %2 0)) (and (<= %1 0) (> %2 0))) 1 0) "^^" identity))

(declare Constant)

(def ConstantPrototype
  {:evaluate       (fn [this, args] (_value this))
   :toString       (fn [this] (str (_value this)))
   :diff           (fn [this, var] (Constant 0))
   :toStringSuffix (fn [this] (str (_value this)))})

(def Constant (constructor #(assoc %1 :value %2) ConstantPrototype))

(def VariablePrototype
  {:evaluate       (fn [this, args] (args  (str (Character/toLowerCase (get (_name this) 0)))))
   :toString       (fn [this] (_name this))
   :diff           (fn [this, var] (if (= var (_name this)) (Constant 1) (Constant 0)))
   :toStringSuffix (fn [this] (_name this))})

(def Variable (constructor #(assoc %1 :name %2) VariablePrototype))

(def operationsObj {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sinh Sinh 'cosh Cosh})

(defn parseObj [token]
      (cond
        (sequential? token) (apply (get operationsObj (first token)) (map parseObj (rest token)))
        (symbol? token) (Variable (str token))
        :else (Constant (double token))))

(defn parseObject [expression] (parseObj (read-string expression)))

;-------------------------------------------------------------
; КОД С ЛЕКЦИИ
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
      (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                           "!"))
(defn _tabulate [parser inputs]
      (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))

(defn _empty [value] (partial -return value))

(defn _char [p]
      (fn [[c & cs]]
          (if (and c (p c))
            (-return c cs))))

(defn _map [f result]
      (if (-valid? result)
        (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
      (fn [input]
          (let [ar ((force a) input)]
               (if (-valid? ar)
                 (_map (partial f (-value ar))
                       ((force b) (-tail ar)))))))

(defn _either [a b]
      (fn [input]
          (let [ar ((force a) input)]
               (if (-valid? ar)
                 ar
                 ((force b) input)))))

(defn _parser [p]
      (let [pp (_combine (fn [v _] v) p (_char #{\u0000}))]
           (fn [input] (-value (pp (str input \u0000))))))

(defn +char [chars]
      (_char (set chars)))

(defn +char-not [chars]
      (_char (comp not (set chars))))

(defn +map [f parser]
      (comp (partial _map f) parser))

(def +parser _parser)

(def +ignore
  (partial +map (constantly 'ignore)))

(defn iconj [coll value]
      (if (= value 'ignore)
        coll
        (conj coll value)))
(defn +seq [& ps]
      (reduce (partial _combine iconj) (_empty []) ps))

(defn +seqf [f & ps]
      (+map (partial apply f) (apply +seq ps)))

(defn +seqn [n & ps]
      (apply +seqf #(nth %& n) ps))

(defn +or [p & ps]
      (reduce _either p ps))

(defn +opt [p]
      (+or p (_empty nil)))

(defn +star [p]
      (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))]
             (rec)))

(defn +plus [p]
      (+seqf cons p (+star p)))

(defn +str [p] (+map (partial apply str) p))

;---------------------------------------------------------------------

(defn createWord [word]
      (apply +seqf (constantly word) (mapv #(+char (str %)) word)))

(def parseOperations {"+" Add "-" Subtract "*" Multiply "/" Divide "negate" Negate "&&" And "||" Or "^^" Xor})

(def parseObjectSuffix
  (let
    [*all-chars (mapv char (range 0 128))
     *letter (+char (apply str (filter #(Character/isLetter %) *all-chars)))
     *digit (+char (apply str (filter #(Character/isDigit %) *all-chars)))
     *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars)))
     *ws (+ignore (+star *space))
     *number (+map #(Constant (read-string %))
                   (+str (+seqf #(flatten %&) (+opt (+char "-")) (+plus *digit) (+opt (+char ".")) (+opt (+plus *digit)))))
     *variable (+map #(Variable %) (+str (+seqf cons *letter (+star (+or *letter *digit)))))
     *opSymbol (+or (createWord "negate") (createWord "&&") (createWord "||") (createWord "^^") (+char "+/-*"))]
    (letfn [
            (*operation []
                        (+map #(apply (get parseOperations (str (last %))) (butlast %)) (+seqn 1 (+char "(") (*arguments) *ws (+char ")"))))
            (*arguments [] (+opt (+seqf cons *ws (delay (*value)) (+star (+seqn 1 (+char " ") *ws (delay (*value)))))))
            (*value [] (+or *number *opSymbol (*operation) *variable))]
           (+parser (+seqn 0 *ws (*value) *ws)))))
