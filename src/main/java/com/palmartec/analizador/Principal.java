/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.palmartec.analizador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author USER-PC
 */
public class Principal {

    public static void main(String[] args) {
        String ruta = "C:/Users/USER-PC/Documents/NetBeansProjects/Analizador/src/main/java/com/palmartec/analizador/";

        try {
            generarLexer(ruta);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void generarLexer(String ruta) throws IOException, Exception {
        String[] ruta1 = {ruta + "Lexer.flex"};
        String[] ruta2 = {ruta + "LexerCup.flex"};

        String[] rutaS = {"-parser", "Sintax", ruta + "Sintax.cup"};

        Path rutaSym = Paths.get("C:/Users/USER-PC/Documents/NetBeansProjects/Analizador/sym.java");
        if (Files.exists(rutaSym)) {
            System.out.println("Existe la ruta");
            Files.delete(rutaSym);
        }

        Path rutaSin = Paths.get("C:/Users/USER-PC/Documents/NetBeansProjects/Analizador/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }

        jflex.Main.generate(ruta1);
        jflex.Main.generate(ruta2);
        java_cup.Main.main(rutaS);

        Files.move(
                Paths.get("C:/Users/USER-PC/Documents/NetBeansProjects/Analizador/sym.java"),
                Paths.get(ruta + "sym.java")
        );

        Files.move(
                Paths.get("C:/Users/USER-PC/Documents/NetBeansProjects/Analizador/Sintax.java"),
                Paths.get(ruta + "Sintax.java")
        );
    }
}
