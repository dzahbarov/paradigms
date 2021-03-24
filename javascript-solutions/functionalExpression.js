"use strict";
// Binary Operations
let binary = (f, lhs, rhs) => x => f(lhs(x), rhs(x));

let addImpl = (lhs, rhs) => lhs + rhs;
let subImpl = (lhs, rhs) => lhs - rhs;
let mulImpl = (lhs, rhs) => lhs * rhs;
let divImpl = (lhs, rhs) => lhs / rhs;

let add = (lhs, rhs) => binary(addImpl, lhs, rhs);
let subtract = (lhs, rhs) => binary(subImpl, lhs, rhs);
let multiply = (lhs, rhs) => binary(mulImpl, lhs, rhs);
let divide = (lhs, rhs) => binary(divImpl, lhs, rhs);

// Unary Operations
let unary = (f, element) => x => f(element(x));

let negateImpl = x => -x;

let negate = element => unary(negateImpl, element);

let cnst = value => x => value;
let variable = name => x => x;

// Test
// let expr = add(
//     subtract(
//         multiply(variable("x"), variable("x")),
//         multiply(cnst(2), variable("x"))
//     ),
//     cnst(1)
// )
//
// for (let i = 0; i < 10; i++) {
//     println("i =", + i + ": " + expr(i).toString())
// }