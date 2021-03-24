"use strict";
// Binary Operations
let binary = (f, lhs, rhs) => (x, y, z) => f(lhs(x, y, z), rhs(x, y, z));

let addImpl = (lhs, rhs) => lhs + rhs;
let subImpl = (lhs, rhs) => lhs - rhs;
let mulImpl = (lhs, rhs) => lhs * rhs;
let divImpl = (lhs, rhs) => lhs / rhs;

let add = (lhs, rhs) => binary(addImpl, lhs, rhs);
let subtract = (lhs, rhs) => binary(subImpl, lhs, rhs);
let multiply = (lhs, rhs) => binary(mulImpl, lhs, rhs);
let divide = (lhs, rhs) => binary(divImpl, lhs, rhs);

// Unary Operations
let unary = (f, element) => (x, y, z) => f(element(x, y, z));

let negateImpl = element => -element;

let negate = element => unary(negateImpl, element);

let cnst = (value) => () => value;

let variable = name => (x, y, z) => {
    switch (name) {
        case "x":
            return x;
        case "y":
            return y;
        case "z":
            return z;
    }
}


let one = cnst(1)
let two = cnst(2)

// Test
// let expr = add(
//     subtract(
//         multiply(variable("x"), variable("x")),
//         multiply(cnst(2), variable("x"))
//     ),
//     cnst(1)
// )
// for (let i = 0; i < 10; i++) {
//     println("i =", + i + ": " + expr(i).toString())
// }
