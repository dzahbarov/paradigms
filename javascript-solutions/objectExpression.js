"use strict";

function Operation(func, funcName, operands) {
    this.getOperands = () => operands;
    this.getFuncName = () => funcName;
    this.getFunc = () => func;
}

function create(func, funcName) {

    function NewFunction(...args) {
        return new Operation(func, funcName, args)
    }

    NewFunction.prototype = Object.create(Operation.prototype);
    NewFunction.constructor = NewFunction;
    return NewFunction;
}


Operation.prototype = {

    toString: function () {
        return this.getOperands().join(" ") + " " + this.getFuncName();
    },

    evaluate: function (x, y, z) {
        return this.getFunc()(...this.getOperands().map(operand => operand.evaluate(x, y, z)));
    },

    prefix: function () {
        return "(" + this.getFuncName() + " " + this.getOperands().map(operation => operation.prefix()).join(" ") + ")";
    },

    constructor: Operation
}

let Add = create((lhs, rhs) => lhs + rhs, "+");

let Multiply = create((lhs, rhs) => lhs * rhs, "*");

let Divide = create((lhs, rhs) => lhs / rhs, "/");

let Subtract = create((lhs, rhs) => lhs - rhs, "-");

let Negate = create(element => -element, "negate");

let Med3 = create((...elements) => elements.sort((a, b) => (a - b))[1], "med3");

let Avg5 = create((...elements) => elements.reduce((a, b) => (a + b)) / 5, "avg5");

let ArithMean = create((...elements) => elements.reduce((a, b) => (a + b)) / elements.length, "arith-mean");

let GeomMean = create((...elements) => Math.pow(Math.abs(elements.reduce((a, b) => (a * b))), 1 / elements.length), "geom-mean");

let HarmMean = create(function (...elements) {
    let sum = 0;
    for (let i = 0; i < elements.length; i++)
        sum += 1 / elements[i];
    return elements.length / sum;
}, "harm-mean");

function Const(element) {
    this.element = element
}

Const.prototype = {

    evaluate: function (x, y, z) {
        return this.element;
    },

    toString: function () {
        return this.element.toString();
    },

    prefix: function () {
        return this.element.toString();
    },

    constructor: Const
}

function Variable(name) {
    this.name = name
}

Variable.prototype = {

    evaluate: function (x, y, z) {
        switch (this.name) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
    },

    toString: function () {
        return this.name;
    },

    prefix: function () {
        return this.name;
    }
}

function ParseError(message, position) {
    Error.call(this, message);
    this.message = "At position " + position + ": " + message;
}

ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "Parse Error";
ParseError.prototype.constructor = ParseError;


const NUM_OF_ARGS = {
    "+": 2,
    "-": 2,
    '*': 2,
    "/": 2,
    "negate": 1,
    "arith-mean": Infinity,
    "geom-mean": Infinity,
    "harm-mean": Infinity
}

const OPERATIONS = {
    "+": Add,
    "-": Subtract,
    '*': Multiply,
    "/": Divide,
    "negate": Negate,
    "arith-mean": ArithMean,
    "geom-mean": GeomMean,
    "harm-mean": HarmMean
}

const VARS = [
    "x", "y", "z"
]

function parsePrefix(stringp) {
    let pos = 0;
    let string = stringp;

    if (string.length === 0) {
        throw new ParseError("Пустая строка", 0);
    }

    let EOF = "\0";

    function parseElements() {
        skipWhitespaces();

        if (test("(")) {
            skipWhitespaces();
            if (VARS.includes(getSym()) || isDigit()) {
                throw new ParseError("Ожидалась операция", pos);
            }
            let res = parseElements();
            skipWhitespaces();
            if (!test(")")) {
                throw new ParseError("Нет закрывающейся скобки", pos);
            }
            return res;
        } else if (VARS.includes(getSym())) {
            return new Variable(next());
        } else if (isDigit() || getSym() === '-' && nextIsDigit()) {
            return new Const(parseConst());
        } else {
            let op = parseOperationName()
            if (op in OPERATIONS) {
                return parseOperation(op, NUM_OF_ARGS[op]);
            }
        }
        throw new ParseError("Неопознанный объект", pos);
    }

    function next() {
        let res = getSym();
        pos++;
        return res;
    }

    function getSym() {
        if (pos < string.length) {
            return string.charAt(pos);
        }
        return EOF;
    }

    function parse() {
        let res = parseElements();
        skipWhitespaces();
        if (!test(EOF)) {
            throw new ParseError("Лишние аргументы", pos);
        }
        return res;
    }

    function test(c) {
        if (getSym() === c) {
            next();
            return true;
        }
        return false;
    }

    function nextIsDigit(c) {
        return '0' <= string[pos + 1] && string[pos + 1] <= '9';
    }

    function skipWhitespaces() {
        while (test(' ')) ;
    }

    function parseConst() {
        let str = '';
        while (isDigit() || getSym() === '-') {
            str += next();
        }
        return parseInt(str);
    }

    function isDigit() {
        return '0' <= getSym() && getSym() <= '9';
    }

    function parseArgs() {
        let args = [];
        while (!test(EOF) && getSym() !== ")") {
            args.push(parseElements());
            skipWhitespaces();
            if (getSym() in OPERATIONS && !nextIsDigit()) {
                throw new ParseError("Операция в неположенном месте", pos);
            }
        }
        return args;
    }

    function parseOperationName() {
        let res = ""
        while (!(res in OPERATIONS) && !test(EOF)) {
            res += next();
        }
        return res;
    }

    function parseOperation(v, numberOfArgs) {
        skipWhitespaces();
        if (getSym() in OPERATIONS && !nextIsDigit()) {
            throw new ParseError("Операция в неположенном месте", pos);
        }
        let args = parseArgs();
        if (args.length !== numberOfArgs && numberOfArgs !== Infinity) {
            throw new ParseError("Неверное количество аргументов для операции. Ожидалось: " + numberOfArgs + ". Найдено: " + args.length, pos);
        }
        return new OPERATIONS[v](...args);
    }

    return parse();
}


//let expr =  parsePrefix('10');
//let expr = parsePrefix("(x)");

// let expr = new Subtract(
//     new Multiply(
//         new Const(2),
//         new Variable("x")
//     ),
//     new Const(3)
// );
