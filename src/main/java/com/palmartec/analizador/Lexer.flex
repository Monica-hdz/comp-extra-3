package com.palmartec.analizador;
import  static com.palmartec.analizador.Tokens.*;

%%
%class Lexer
%type Tokens
L=\$([a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*)
V=[a-zA-Z]+
D=[0-9]+
F=[0-9]+\.[0-9]+
espacio=[ ,\t,\r]+
%{
    public String lexeme;
%}
%%
{espacio} {/*Ignore*/}
"//".* {/*Ignore*/}
"\n" {return Linea;}
( "\"" ) {lexeme=yytext(); return Comillas;}
"=" {lexeme=yytext(); return Igual;}
"<?php" {lexeme=yytext(); return Apertura;}
"?>" {lexeme=yytext(); return Cierre;}
";" {lexeme=yytext(); return P_coma;}
( true | false ) {lexeme=yytext(); return Op_booleano;}
{L}({L}|{D})* {lexeme=yytext(); return Identificador;}
{V}({V})* {lexeme=yytext(); return Value;}
("(-"{D}+")")|{D}+ {lexeme=yytext(); return Numero;}
("(-"{F}+")")|{F}+ {lexeme=yytext(); return Flotante;}
 . {return ERROR;}