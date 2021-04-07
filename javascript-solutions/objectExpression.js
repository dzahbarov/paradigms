"use strict";

function Operation(func, funcName, operands) {
    this.getOperands = () => operands;
    this.getFuncName = () => funcName;
    this.getFunc = () => func;
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

function Add(...operands) {
    Operation.call(this, (lhs, rhs) => lhs + rhs, "+", operands);
}

Add.prototype = Object.create(Operation.prototype);
Add.prototype.constructor = Add;

function Multiply(...operands) {
    Operation.call(this, (lhs, rhs) => lhs * rhs, "*", operands);
}

Multiply.prototype = Object.create(Operation.prototype);
Multiply.prototype.constructor = Multiply;

function Divide(...operands) {
    Operation.call(this, (lhs, rhs) => lhs / rhs, "/", operands);
}

Divide.prototype = Object.create(Operation.prototype);
Divide.prototype.constructor = Divide;

function Subtract(...operands) {
    Operation.call(this, (lhs, rhs) => lhs - rhs, "-", operands);
}

Subtract.prototype = Object.create(Operation.prototype);
Subtract.prototype.constructor = Subtract;


function Negate(...operands) {
    Operation.call(this, element => -element, "negate", operands);
}

Negate.prototype = Object.create(Operation.prototype);
Negate.prototype.constructor = Negate;

function Med3(...operands) {
    Operation.call(this, (...elements) => elements.sort((a, b) => (a - b))[1], "med3", operands);
}

Med3.prototype = Object.create(Operation.prototype);
Med3.prototype.constructor = Med3;


function Avg5(...operands) {
    Operation.call(this, (...elements) => elements.reduce((a, b) => (a + b)) / 5, "avg5", operands);
}

Avg5.prototype = Object.create(Operation.prototype);
Avg5.prototype.constructor = Avg5;

function ArithMean(...operands) {
    Operation.call(this, (...elements) => elements.reduce((a, b) => (a + b)) / operands.length, "arith-mean", operands);
}

ArithMean.prototype = Object.create(Operation.prototype);
ArithMean.prototype.constructor = ArithMean;

function GeomMean(...operands) {
    Operation.call(this, (...elements) => Math.pow((elements.reduce((a, b) => (a * b))), 1 / operands.length), "geom-mean", operands);
}

GeomMean.prototype = Object.create(Operation.prototype);
GeomMean.prototype.constructor = GeomMean;

function HarmMean(...operands) {
    // let tmp = operands.reduce((a, b) => (1/a + 1/b));
    // console.log(tmp);
    Operation.call(this, (...elements) => operands.length / elements.reduce((a, b) => (1 / a + 1 / b)), "harm-mean", operands);
}

HarmMean.prototype = Object.create(Operation.prototype);
HarmMean.prototype.constructor = HarmMean;

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
ParseError.prototype.name = "CustomError";
ParseError.prototype.constructor = ParseError;


function parsePrefix(string) {
    return new Parser(string).parse();
}

function Parser(string) {
    this.string = string;
    this.pos = 0;
}

const OPERATIONS = {
    "+": Add,
    "-": Subtract,
    '*': Multiply,
    "/": Divide,
    "negate": Negate
}

const VARS = [
    "x", "y", "z"
]
Parser.prototype = {

    EOF: "\0",

    getPos: function () {
        return this.pos;
    },

    next: function () {
        let res = this.getSym();
        this.pos++;
        return res;
    },

    getSym: function () {
        if (this.getPos() < this.string.length) {
            return this.string.charAt(this.pos);
        }
        return this.EOF;
    },

    parse: function () {
        let res = this.parseElements();
        this.skipWhitespaces();
        if (!this.test(this.EOF)) {
            throw new ParseError("Лишние аргументы", this.getPos());
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

    skipWhitespaces: function () {
        while (this.test(' ')) ;
    },

    parseConst: function () {
        let str = '';
        while (this.isDigit()) {
            str = str + this.next();
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
        }
        return args;
    },

    parseElements: function () {
        this.skipWhitespaces();

        if (this.test("(")) {
            let res = this.parseElements();
            this.skipWhitespaces();
            if (!this.test(")")) {
                throw new ParseError("Нет закрывающейся скобки", this.getPos());
            }
            return res;
        } else if (this.isDigit()) {
            return new Const(this.parseConst());
        } else if (VARS.includes(this.getSym())) {
            return new Variable(this.next());
        } else if (this.test('n')) {
            if (this.test('e') && this.test('g') && this.test('a') && this.test('t') && this.test('e')) {
                return this.pparse("negate", 1);
            }
            throw new ParseError("Не опознанный объект", this.getPos());
        } else if (this.test('-')) {
            if (this.isDigit()) {
                return new Const(-this.parseConst());
            } else {
                return this.pparse("-", 2);
            }
        } else if (this.getSym() in OPERATIONS) {
            let v = this.next();
            return this.pparse(v, 2);
        } else if (this.testString("arith-mean")) {
            let args = this.parseArgs();
            return new ArithMean(...args);
        } else if (this.testString("geom-mean")) {
            let args = this.parseArgs();
            return new GeomMean(...args);
        } else if (this.testString("harm-mean")) {
            let args = this.parseArgs();
            return new HarmMean(...args);
        }
        throw new ParseError("Не опознанный объект", this.getPos());
    },

    testString: function (string) {
        for (let ch of string) {
            if (!this.test(ch)) {
                return false;
            }
        }
        return true;
    },

    pparse: function (v, numberOfArgs) {
        let args = this.parseArgs();
        if (args.length !== numberOfArgs) {
            throw new ParseError("Неверное количество аргументов для " + OPERATIONS[v].name, this.getPos());
        }
        return new OPERATIONS[v](...args);
    }
}
Parser.prototype.constructor = Parser;


// let expr = new Subtract(
//     new Multiply(
//         new Const(2),
//         new Variable("x")
//     ),
//     new Const(3)
// );
