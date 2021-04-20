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

let HarmMean = create((...elements) => elements.length / elements.reduce((sum, el) => (sum + 1 / el), 0), "harm-mean");

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

/*---------------------------------------------------------------------------------------------------*/
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

function parsePrefix(str) {
    let pos = 0;

    if (str.length === 0) {
        throw new ParseError("Empty input", 0);
    }

    let EOF = "\0";

    let res = parse();
    skipWhitespaces();
    if (!test(EOF)) {
        throw new ParseError("Extra arguments", pos);
    }
    return res;

    function parse() {

        let element = parseToken();

        if (element === '(') {
            let res = parseOperation();
            skipWhitespaces();
            if (!test(")")) {
                throw new ParseError("No closing parenthesis", pos);
            }
            return res;
        } else if (element === ")") {
            throw new ParseError("Extra closing parenthesis", pos);
        } else if (VARS.includes(element)) {
            return new Variable(element);
        }
        if (!isNaN(+element)) {
            return new Const(+element);
        }
        throw new ParseError("Undefined object", pos);
    }

    function parseToken() {
        skipWhitespaces();
        let res = next();
        while (getSym() !== ' ' && getSym() !== EOF && getSym() !== ")" && getSym() !== "(" && res !== ")" && res !== "(") {
            res += next();
        }
        skipWhitespaces();
        return res;
    }

    function next() {
        let res = getSym();
        pos++;
        return res;
    }

    function getSym() {
        if (pos < str.length) {
            return str.charAt(pos);
        }
        return EOF;
    }

    function test(c) {
        if (getSym() === c) {
            pos++;
            return true;
        }
        return false;
    }

    function skipWhitespaces() {
        while (test(' ')) ;
    }

    function viewToken() {
        let prevPosition = pos;
        let res = parseToken();
        pos = prevPosition;
        return res;
    }

    function parseArgs() {
        let args = [];
        while (!test(EOF) && getSym() !== ")") {
            args.push(parse());
            skipWhitespaces();
            if (viewToken() in OPERATIONS) {
                throw new ParseError("Unexpected operation", pos);
            }
        }
        return args;
    }

    function parseOperation() {
        let op = parseToken();
        let args = parseArgs();
        if (args.length !== NUM_OF_ARGS[op] && NUM_OF_ARGS[op] !== Infinity) {
            throw new ParseError("Wrong number of arguments. Expected: " + NUM_OF_ARGS[op] + ". Found: " + args.length, pos);
        }
        return new OPERATIONS[op](...args);
    }
}


// console.log(expr.prefix())
//let expr = parsePrefix("(x)");

// let expr = new Subtract(
//     new Multiply(
//         new Const(2),
//         new Variable("x")
//     ),
//     new Const(3)
// );
