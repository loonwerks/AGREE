/*
 * Copyright (c) 2023, Collins Aerospace.
 * Developed with the sponsorship of Defense Advanced Research Projects Agency (DARPA).
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this data, 
 * including any software or models in source or binary form, as well as any drawings, specifications, 
 * and documentation (collectively "the Data"), to deal in the Data without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Data.
 * 
 * THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT 
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 *
 * Generated by Xtext version 2.30.0.
 */

lexer grammar InternalAgreeLexer;

@header {
package com.rockwellcollins.atc.agree.parser.antlr.lexer;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

Linearization : ('L'|'l')('I'|'i')('N'|'n')('E'|'e')('A'|'a')('R'|'r')('I'|'i')('Z'|'z')('A'|'a')('T'|'t')('I'|'i')('O'|'o')('N'|'n');

Uninterpreted : ('U'|'u')('N'|'n')('I'|'i')('N'|'n')('T'|'t')('E'|'e')('R'|'r')('P'|'p')('R'|'r')('E'|'e')('T'|'t')('E'|'e')('D'|'d');

Get_Property : ('G'|'g')('E'|'e')('T'|'t')'_'('P'|'p')('R'|'r')('O'|'o')('P'|'p')('E'|'e')('R'|'r')('T'|'t')('Y'|'y');

Asynchronous : ('A'|'a')('S'|'s')('Y'|'y')('N'|'n')('C'|'c')('H'|'h')('R'|'r')('O'|'o')('N'|'n')('O'|'o')('U'|'u')('S'|'s');

Agree_input : ('A'|'a')('G'|'g')('R'|'r')('E'|'e')('E'|'e')'_'('I'|'i')('N'|'n')('P'|'p')('U'|'u')('T'|'t');

Exclusively : ('E'|'e')('X'|'x')('C'|'c')('L'|'l')('U'|'u')('S'|'s')('I'|'i')('V'|'v')('E'|'e')('L'|'l')('Y'|'y');

Classifier : ('C'|'c')('L'|'l')('A'|'a')('S'|'s')('S'|'s')('I'|'i')('F'|'f')('I'|'i')('E'|'e')('R'|'r');

Connection : ('C'|'c')('O'|'o')('N'|'n')('N'|'n')('E'|'e')('C'|'c')('T'|'t')('I'|'i')('O'|'o')('N'|'n');

Condition : ('C'|'c')('O'|'o')('N'|'n')('D'|'d')('I'|'i')('T'|'t')('I'|'i')('O'|'o')('N'|'n');

Guarantee : ('G'|'g')('U'|'u')('A'|'a')('R'|'r')('A'|'a')('N'|'n')('T'|'t')('E'|'e')('E'|'e');

Initially : ('I'|'i')('N'|'n')('I'|'i')('T'|'t')('I'|'i')('A'|'a')('L'|'l')('L'|'l')('Y'|'y');

No_simult : ('N'|'n')('O'|'o')'_'('S'|'s')('I'|'i')('M'|'m')('U'|'u')('L'|'l')('T'|'t');

Parameter : ('P'|'p')('A'|'a')('R'|'r')('A'|'a')('M'|'m')('E'|'e')('T'|'t')('E'|'e')('R'|'r');

Reachable : ('R'|'r')('E'|'e')('A'|'a')('C'|'c')('H'|'h')('A'|'a')('B'|'b')('L'|'l')('E'|'e');

Reference : ('R'|'r')('E'|'e')('F'|'f')('E'|'e')('R'|'r')('E'|'e')('N'|'n')('C'|'c')('E'|'e');

Synchrony : ('S'|'s')('Y'|'y')('N'|'n')('C'|'c')('H'|'h')('R'|'r')('O'|'o')('N'|'n')('Y'|'y');

Calendar : ('C'|'c')('A'|'a')('L'|'l')('E'|'e')('N'|'n')('D'|'d')('A'|'a')('R'|'r');

Constant : ('C'|'c')('O'|'o')('N'|'n')('S'|'s')('T'|'t')('A'|'a')('N'|'n')('T'|'t');

Contract : ('C'|'c')('O'|'o')('N'|'n')('T'|'t')('R'|'r')('A'|'a')('C'|'c')('T'|'t');

External : ('E'|'e')('X'|'x')('T'|'t')('E'|'e')('R'|'r')('N'|'n')('A'|'a')('L'|'l');

Ordering : ('O'|'o')('R'|'r')('D'|'d')('E'|'e')('R'|'r')('I'|'i')('N'|'n')('G'|'g');

Property : ('P'|'p')('R'|'r')('O'|'o')('P'|'p')('E'|'e')('R'|'r')('T'|'t')('Y'|'y');

Sporadic : ('S'|'s')('P'|'p')('O'|'o')('R'|'r')('A'|'a')('D'|'d')('I'|'i')('C'|'c');

Timefall : ('T'|'t')('I'|'i')('M'|'m')('E'|'e')('F'|'f')('A'|'a')('L'|'l')('L'|'l');

Timerise : ('T'|'t')('I'|'i')('M'|'m')('E'|'e')('R'|'r')('I'|'i')('S'|'s')('E'|'e');

Whenever : ('W'|'w')('H'|'h')('E'|'e')('N'|'n')('E'|'e')('V'|'v')('E'|'e')('R'|'r');

KW__INSERT : '_'('I'|'i')('N'|'n')('S'|'s')('E'|'e')('R'|'r')('T'|'t');

KW__REMOVE : '_'('R'|'r')('E'|'e')('M'|'m')('O'|'o')('V'|'v')('E'|'e');

Applies : ('A'|'a')('P'|'p')('P'|'p')('L'|'l')('I'|'i')('E'|'e')('S'|'s');

Becomes : ('B'|'b')('E'|'e')('C'|'c')('O'|'o')('M'|'m')('E'|'e')('S'|'s');

Binding : ('B'|'b')('I'|'i')('N'|'n')('D'|'d')('I'|'i')('N'|'n')('G'|'g');

Compute : ('C'|'c')('O'|'o')('M'|'m')('P'|'p')('U'|'u')('T'|'t')('E'|'e');

Flatmap : ('F'|'f')('L'|'l')('A'|'a')('T'|'t')('M'|'m')('A'|'a')('P'|'p');

Implies : ('I'|'i')('M'|'m')('P'|'p')('L'|'l')('I'|'i')('E'|'e')('S'|'s');

Indices : ('I'|'i')('N'|'n')('D'|'d')('I'|'i')('C'|'c')('E'|'e')('S'|'s');

Latched : ('L'|'l')('A'|'a')('T'|'t')('C'|'c')('H'|'h')('E'|'e')('D'|'d');

Returns : ('R'|'r')('E'|'e')('T'|'t')('U'|'u')('R'|'r')('N'|'n')('S'|'s');

KW__COUNT : '_'('C'|'c')('O'|'o')('U'|'u')('N'|'n')('T'|'t');

Always : ('A'|'a')('L'|'l')('W'|'w')('A'|'a')('Y'|'y')('S'|'s');

Assert : ('A'|'a')('S'|'s')('S'|'s')('E'|'e')('R'|'r')('T'|'t');

Assign : ('A'|'a')('S'|'s')('S'|'s')('I'|'i')('G'|'g')('N'|'n');

Assume : ('A'|'a')('S'|'s')('S'|'s')('U'|'u')('M'|'m')('E'|'e');

During : ('D'|'d')('U'|'u')('R'|'r')('I'|'i')('N'|'n')('G'|'g');

Exists : ('E'|'e')('X'|'x')('I'|'i')('S'|'s')('T'|'t')('S'|'s');

Forall : ('F'|'f')('O'|'o')('R'|'r')('A'|'a')('L'|'l')('L'|'l');

Jitter : ('J'|'j')('I'|'i')('T'|'t')('T'|'t')('E'|'e')('R'|'r');

Occurs : ('O'|'o')('C'|'c')('C'|'c')('U'|'u')('R'|'r')('S'|'s');

Raises : ('R'|'r')('A'|'a')('I'|'i')('S'|'s')('E'|'e')('S'|'s');

Simult : ('S'|'s')('I'|'i')('M'|'m')('U'|'u')('L'|'l')('T'|'t');

Struct : ('S'|'s')('T'|'t')('R'|'r')('U'|'u')('C'|'c')('T'|'t');

Timeof : ('T'|'t')('I'|'i')('M'|'m')('E'|'e')('O'|'o')('F'|'f');

Within : ('W'|'w')('I'|'i')('T'|'t')('H'|'h')('I'|'i')('N'|'n');

Const : ('C'|'c')('O'|'o')('N'|'n')('S'|'s')('T'|'t');

Delta : ('D'|'d')('E'|'e')('L'|'l')('T'|'t')('A'|'a');

Event : ('E'|'e')('V'|'v')('E'|'e')('N'|'n')('T'|'t');

False : ('F'|'f')('A'|'a')('L'|'l')('S'|'s')('E'|'e');

Floor : ('F'|'f')('L'|'l')('O'|'o')('O'|'o')('R'|'r');

Foldl : ('F'|'f')('O'|'o')('L'|'l')('D'|'d')('L'|'l');

Foldr : ('F'|'f')('O'|'o')('L'|'l')('D'|'d')('R'|'r');

Holds : ('H'|'h')('O'|'o')('L'|'l')('D'|'d')('S'|'s');

Lemma : ('L'|'l')('E'|'e')('M'|'m')('M'|'m')('A'|'a');

Modes : ('M'|'m')('O'|'o')('D'|'d')('E'|'e')('S'|'s');

Occur : ('O'|'o')('C'|'c')('C'|'c')('U'|'u')('R'|'r');

Times : ('T'|'t')('I'|'i')('M'|'m')('E'|'e')('S'|'s');

KW__CLK : '_'('C'|'c')('L'|'l')('K'|'k');

Bool : ('B'|'b')('O'|'o')('O'|'o')('L'|'l');

Each : ('E'|'e')('A'|'a')('C'|'c')('H'|'h');

Else : ('E'|'e')('L'|'l')('S'|'s')('E'|'e');

Enum : ('E'|'e')('N'|'n')('U'|'u')('M'|'m');

Into : ('I'|'i')('N'|'n')('T'|'t')('O'|'o');

Lift : ('L'|'l')('I'|'i')('F'|'f')('T'|'t');

Node : ('N'|'n')('O'|'o')('D'|'d')('E'|'e');

Over : ('O'|'o')('V'|'v')('E'|'e')('R'|'r');

Prev : ('P'|'p')('R'|'r')('E'|'e')('V'|'v');

Real : ('R'|'r')('E'|'e')('A'|'a')('L'|'l');

Then : ('T'|'t')('H'|'h')('E'|'e')('N'|'n');

This : ('T'|'t')('H'|'h')('I'|'i')('S'|'s');

Time : ('T'|'t')('I'|'i')('M'|'m')('E'|'e');

True : ('T'|'t')('R'|'r')('U'|'u')('E'|'e');

Type : ('T'|'t')('Y'|'y')('P'|'p')('E'|'e');

When : ('W'|'w')('H'|'h')('E'|'e')('N'|'n');

With : ('W'|'w')('I'|'i')('T'|'t')('H'|'h');

PlusSignEqualsSignGreaterThanSign : '+''=''>';

LessThanSignEqualsSignGreaterThanSign : '<''=''>';

IAT : ('I'|'i')('A'|'a')('T'|'t');

And : ('A'|'a')('N'|'n')('D'|'d');

Div : ('D'|'d')('I'|'i')('V'|'v');

Fun : ('F'|'f')('U'|'u')('N'|'n');

Int : ('I'|'i')('N'|'n')('T'|'t');

Let : ('L'|'l')('E'|'e')('T'|'t');

Mod : ('M'|'m')('O'|'o')('D'|'d');

Not : ('N'|'n')('O'|'o')('T'|'t');

Pre : ('P'|'p')('R'|'r')('E'|'e');

Tel : ('T'|'t')('E'|'e')('L'|'l');

Var : ('V'|'v')('A'|'a')('R'|'r');

ExclamationMarkEqualsSign : '!''=';

HyphenMinusGreaterThanSign : '-''>';

FullStopFullStop : '.''.';

ColonColon : ':'':';

ColonEqualsSign : ':''=';

LessThanSignEqualsSign : '<''=';

LessThanSignGreaterThanSign : '<''>';

EqualsSignGreaterThanSign : '=''>';

GreaterThanSignEqualsSign : '>''=';

LeftSquareBracketVerticalLine : '[''|';

Eq : ('E'|'e')('Q'|'q');

If : ('I'|'i')('F'|'f');

In : ('I'|'i')('N'|'n');

Or : ('O'|'o')('R'|'r');

To : ('T'|'t')('O'|'o');

VerticalLineRightSquareBracket : '|'']';

LeftParenthesis : '(';

RightParenthesis : ')';

Asterisk : '*';

PlusSign : '+';

Comma : ',';

HyphenMinus : '-';

FullStop : '.';

Solidus : '/';

Colon : ':';

Semicolon : ';';

LessThanSign : '<';

EqualsSign : '=';

GreaterThanSign : '>';

LeftSquareBracket : '[';

RightSquareBracket : ']';

CircumflexAccent : '^';

LeftCurlyBracket : '{';

RightCurlyBracket : '}';

RULE_SL_COMMENT : '--' ~(('\n'|'\r'))* ('\r'? '\n')?;

fragment RULE_EXPONENT : ('e'|'E') ('+'|'-')? RULE_DIGIT+;

fragment RULE_INT_EXPONENT : ('e'|'E') '+'? RULE_DIGIT+;

RULE_REAL_LIT : RULE_DIGIT+ ('_' RULE_DIGIT+)* '.' RULE_DIGIT+ ('_' RULE_DIGIT+)* RULE_EXPONENT?;

RULE_INTEGER_LIT : RULE_DIGIT+ ('_' RULE_DIGIT+)* ('#' RULE_BASED_INTEGER '#' RULE_INT_EXPONENT?|RULE_INT_EXPONENT?);

fragment RULE_DIGIT : '0'..'9';

fragment RULE_EXTENDED_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F');

fragment RULE_BASED_INTEGER : RULE_EXTENDED_DIGIT ('_'? RULE_EXTENDED_DIGIT)*;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ID : ('a'..'z'|'A'..'Z') ('_'? ('a'..'z'|'A'..'Z'|'0'..'9'))*;

RULE_WS : (' '|'\t'|'\r'|'\n')+;
