package com.palmartec.analizador;
import java_cup.runtime.Symbol;

%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char
L=\$([a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*)
V=[a-zA-Z]+
D=[0-9]+
F=[0-9]+\.[0-9]+
espacio=[ ,\t,\r,\n]+
%{
     private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%
{espacio} {/*Ignore*/}
( "//"(.)* ) {/*Ignore*/}
( "\"" ) {return new Symbol(sym.Comillas, (int)yychar, yyline, yytext());}
( "=" ) {return new Symbol(sym.Igual, (int)yychar, yyline, yytext());}
( "<?php" ) {return new Symbol(sym.Apertura, (int)yychar, yyline, yytext());}
( "?>" ) {return new Symbol(sym.Cierre, (int)yychar, yyline, yytext());}
( ";" ) {return new Symbol(sym.P_coma, (int)yychar, yyline, yytext());}
( true | false ) {return new Symbol(sym.Op_booleano, (int)yychar, yyline, yytext());}
{L}({L}|{D})* {return new Symbol(sym.Identificador, (int)yychar, yyline, yytext());}
{V}({V})* {return new Symbol(sym.Value, (int)yychar, yyline, yytext());}
("(-"{D}+")")|{D}+ {return new Symbol(sym.Numero, (int)yychar, yyline, yytext());}
("(-"{F}+")")|{F}+ {return new Symbol(sym.Flotante, (int)yychar, yyline, yytext());}
 . {return new Symbol(sym.ERROR, (int)yychar, yyline, yytext());}