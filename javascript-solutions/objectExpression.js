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

function parsePrefix(string) {
    if (string.length === 0) {
        throw new ParseError("Пустая строка", 0);
    }
    return new Parser(string).parse();
}

function Parser(string) {
    this.pos = 0;
    this.string = string;
}

Parser.prototype = {

    EOF: "\0",

    parseElements: function () {
        this.skipWhitespaces();

        if (this.test("(")) {
            this.skipWhitespaces();
            if (VARS.includes(this.getSym()) || this.isDigit()) {
                throw new ParseError("Ожидалась операция", this.pos);
            }
            let res = this.parseElements();
            this.skipWhitespaces();
            if (!this.test(")")) {
                throw new ParseError("Нет закрывающейся скобки", this.pos);
            }
            return res;
        } else if (VARS.includes(this.getSym())) {
            return new Variable(this.next());
        } else if (this.isDigit() || this.getSym() === '-' && this.nextIsDigit()) {
            return new Const(this.parseConst());
        } else {
            let op = this.parseOperationName()
            if (op in OPERATIONS) {
                return this.parseOperation(op, NUM_OF_ARGS[op]);
            }
        }
        throw new ParseError("Неопознанный объект", this.pos);
    },

    next: function () {
        let res = this.getSym();
        this.pos++;
        return res;
    },

    getSym: function () {
        if (this.pos < this.string.length) {
            return this.string.charAt(this.pos);
        }
        return this.EOF;
    },

    parse: function () {
        let res = this.parseElements();
        this.skipWhitespaces();
        if (!this.test(this.EOF)) {
            throw new ParseError("Лишние аргументы", this.pos);
        }
        return res;
    },

    test: function (c) {
        if (this.getSym() === c) {
            this.next();
            return true;
        }
        return false;
    },

    nextIsDigit: function (c) {
        return '0' <= this.string[this.pos + 1] && this.string[this.pos + 1] <= '9';
    },

    skipWhitespaces: function () {
        while (this.test(' ')) ;
    },

    parseConst: function () {
        let str = '';
        while (this.isDigit() || this.getSym() === '-') {
            str += this.next();
        }
        return parseInt(str);
    },

    isDigit: function () {
        return '0' <= this.getSym() && this.getSym() <= '9';
    },

    parseArgs: function () {
        let args = [];
        while (!this.test(this.EOF) && this.getSym() !== ")") {
            args.push(this.parseElements());
            this.skipWhitespaces();
            if (this.getSym() in OPERATIONS && !this.nextIsDigit()) {
                throw new ParseError("Операция в неположенном месте", this.pos);
            }
        }
        return args;
    },

    parseOperationName: function () {
        let res = ""
        while (!(res in OPERATIONS) && !this.test(this.EOF)) {
            res += this.next();
        }
        return res;
    },

    parseOperation: function (v, numberOfArgs) {
        this.skipWhitespaces();
        if (this.getSym() in OPERATIONS && !this.nextIsDigit()) {
            throw new ParseError("Операция в неположенном месте", this.pos);
        }
        let args = this.parseArgs();
        if (args.length !== numberOfArgs && numberOfArgs !== Infinity) {
            throw new ParseError("Неверное количество аргументов для операции. Ожидалось: " + numberOfArgs + ". Найдено: " + args.length, this.pos);
        }
        return new OPERATIONS[v](...args);
    }
}

Parser.prototype.constructor = Parser;

let expr = parsePrefix("(x)");

// let expr = new Subtract(
//     new Multiply(
//         new Const(2),
//         new Variable("x")
//     ),
//     new Const(3)
// );
