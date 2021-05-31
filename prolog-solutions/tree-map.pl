count([], 0).
count([_ | T], R) :- count(T, TR), R is TR + 1.

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

node(Key, Value, Lhs, Rhs).

getMid([(K, V) | T], Mid, K, V, Mid).
getMid([_ | T], Mid, K, V, Current) :- NewCur is Current + 1, getMid(T, Mid, K, V, NewCur).

builder(ListMap, node(Key, Value, null, null), Start, End) :-
										End is Start + 1,
										getMid(ListMap, Start, Key, Value, 0), !.

builder(ListMap, null, Start, Start) :- !.

builder(ListMap, node(Key, Value, Lhs, Rhs), Start, End) :-
                                       Mid is div(Start + End, 2),
                                       getMid(ListMap, Mid, Key, Value, 0),
                                       builder(ListMap, Lhs, Start, Mid),
                                       not End is Mid + 1,
                                       NewMid is Mid + 1,
                                       builder(ListMap, Rhs, NewMid, End), !.

builder(ListMap, node(Key, Value, Lhs, null), Start, End) :-
                                       Mid is div(Start + End, 2),
                                       getMid(ListMap, Mid, Key, Value, 0),
                                       builder(ListMap, Lhs, Start, Mid), !.

map_build([], null) :- !.
map_build(ListMap, TreeMap) :- count(ListMap, R), builder(ListMap, TreeMap, 0, R).

map_get(node(Key, Value, Lhs, Rhs), Key, Value) :- !.
map_get(node(Key, Value, Lhs, Rhs), FKey,FValue) :- FKey < Key, map_get(Lhs, FKey, FValue), !.
map_get(node(Key, Value, Lhs, Rhs), FKey,FValue) :- map_get(Rhs, FKey, FValue).

map_keys(null, []) :- !.
map_keys(node(Key, Value, null, null), [Key]) :- !.
map_keys(node(Key, Value, Lhs, null), Keys) :- map_keys(Lhs, R), concat(R, [Key], Keys), !.
map_keys(node(Key, Value, Lhs, Rhs), Keys) :- map_keys(Lhs, R1), map_keys(Rhs, R2), concat(R1, [Key], R), concat(R, R2, Keys), !.

map_values(null, []) :- !.
map_values(node(Key, Value, null, null), [Value]) :- !.
map_values(node(Key, Value, Lhs, null), Values) :- map_values(Lhs, R), concat(R, [Value], Values), !.
map_values(node(Key, Value, Lhs, Rhs), Values) :- map_values(Lhs, R1), map_values(Rhs, R2), concat(R1, [Value], R), concat(R, R2, Values), !.

