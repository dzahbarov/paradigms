innerLoop(J, I, MAX_N) :- J =< MAX_N, assert(composite_numbers(J)), NEW_J is J + I, innerLoop(NEW_J, I, MAX_N).
outerLoop(I, MAX_N) :- composite_numbers(I), NEW_I is I + 1, NEW_I * NEW_I =< MAX_N, outerLoop(NEW_I, MAX_N).
outerLoop(I, MAX_N) :- \+ composite_numbers(I), J is I * I, J =< MAX_N, \+ innerLoop(J, I, MAX_N), NEW_I is I + 1, outerLoop(NEW_I, MAX_N).
init(MAX_N) :- \+ outerLoop(2, MAX_N).

prime(N) :-  \+ composite_numbers(N), \+ N is 1.
composite(N) :- composite_numbers(N).

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

concat_n_times(0, Number, []) :- !.
concat_n_times(N, Number, List) :- 0 is N mod 2, NEW_N is N / 2, concat_n_times(NEW_N, Number, L1), concat(L1, L1, List), !.
concat_n_times(N, Number, List) :- 1 is N mod 2, NEW_N is N - 1, concat_n_times(NEW_N, Number, L1), concat(L1, [Number], List), !.

getPowerFact(N, 0, Divisor, []) :- !.
getPowerFact(1, I, Divisor, []) :- !.
getPowerFact(N, I, Divisor, Divisors) :- prime(Divisor), 0 is N mod Divisor, NEW_N is N / Divisor, concat_n_times(I, Divisor, R1), getPowerFact(NEW_N, I, Divisor, R2), concat(R1, R2, Divisors), !.
getPowerFact(N, I, Divisor, Divisors) :- NEW_D is Divisor + 1, NEW_D * NEW_D =< N, getPowerFact(N, I, NEW_D, Divisors), !.
getPowerFact(N, I, Divisor, Divisors) :- concat_n_times(I, N, Divisors), !.

power_divisors(N, I, Divisors) :- getPowerFact(N, I,  2, Divisors).
prime_divisors(N, Divisors) :- power_divisors(N, 1, Divisors).
