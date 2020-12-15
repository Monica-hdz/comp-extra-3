package com.palmartec.analizador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class AnalizadorGUI {

    public static void main(String[] args) {
        Marco m = new Marco();
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

class Marco extends JFrame {

    public Marco() {
        setTitle("Analalizador PHP");
        setSize(800, 1000);
        setLocationRelativeTo(null);
        Lamina l = new Lamina();
        add(l);
        setVisible(true);
    }
}

class Lamina extends JPanel {

    public JLabel lLexico, lSintact;
    public RSyntaxTextArea taSyntax;
    public JTextArea taLexico, taSintact;
    public JScrollPane jspLexico, jspSintact;

    public Lamina() {

        taSyntax = new RSyntaxTextArea(20, 100);
        taSyntax.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
        taSyntax.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(taSyntax);

        JButton btnAnalizarLexico = new JButton("Analizar Lexico");
        JButton btnAnalizarSintactico = new JButton("Analizar Sintactico");

        btnAnalizarLexico.addActionListener(new EventAnalyzeLex());
        btnAnalizarSintactico.addActionListener(new EventAnalyzeSyn());

        lLexico = new JLabel("Analizador Lexico");

        taLexico = new JTextArea(15, 20);
        jspLexico = new JScrollPane(taLexico);

        lSintact = new JLabel("Analizador Sintactico");

        taSintact = new JTextArea(15, 20);
        jspSintact = new JScrollPane(taSintact);

        Box cajaH1 = Box.createHorizontalBox();
        cajaH1.add(btnAnalizarSintactico);
        cajaH1.add(Box.createHorizontalStrut(20));
        cajaH1.add(btnAnalizarLexico);

        Box cajaVertical = Box.createVerticalBox();
        cajaVertical.add(cajaH1);
        cajaVertical.add(Box.createVerticalStrut(10));
        cajaVertical.add(sp);
        cajaVertical.add(Box.createVerticalStrut(10));
        cajaVertical.add(lLexico);
        cajaVertical.add(Box.createVerticalStrut(10));
        cajaVertical.add(jspLexico);
        cajaVertical.add(Box.createVerticalStrut(10));
        cajaVertical.add(lSintact);
        cajaVertical.add(Box.createVerticalStrut(10));
        cajaVertical.add(jspSintact);

        add(cajaVertical, BorderLayout.CENTER);
    }

    private void analizarLexico() {
        int cont = 1;

        String expr = (String) taSyntax.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        String resultado = "LINEA " + cont + "\t\tSIMBOLO\n";
        try {
            while (true) {
                Tokens tokens;

                tokens = lexer.yylex();

                if (tokens == null) {
                    taLexico.setText(resultado);
                    return;
                }
                switch (tokens) {
                    case Linea:
                        cont++;
                        resultado += "LINEA " + cont + "\n";
                        break;
                    case Comillas:
                        resultado += "<Comillas>\t\t " + lexer.lexeme + "\n";
                        break;
                    case Igual:
                        resultado += "<Operador igual>\t " + lexer.lexeme + "\n";
                        break;
                    case Apertura:
                        resultado += "<Simbolo de apertura>\t " + lexer.lexeme + "\n";
                        break;
                    case Cierre:
                        resultado += "<Simbolo de cierre>\t " + lexer.lexeme + "\n";
                        break;
                    case P_coma:
                        resultado += "<Punto y coma>\t " + lexer.lexeme + "\n";
                        break;
                    case Op_booleano:
                        resultado += "<Palabra reservada>\t " + lexer.lexeme + "\n";
                        break;
                    case Identificador:
                        resultado += "<Identificador>\t\t " + lexer.lexeme + "\n";
                        break;
                    case Numero:
                        resultado += "<Numero>\t\t " + lexer.lexeme + "\n";
                        break;
                    case Flotante:
                        resultado += "<Flotante>\t\t " + lexer.lexeme + "\n";
                        break;
                    case Value:
                        resultado += "<Value>\t\t " + lexer.lexeme + "\n";
                        break;
                    case ERROR:
                        resultado += "<Simbolo no definido>\n";
                        break;
                    default:
                        resultado += "  < " + lexer.lexeme + " >\n";
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Lamina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class EventAnalyzeLex implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            analizarLexico();
        }

    }

    private class EventAnalyzeSyn implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String st = taSyntax.getText();
            Sintax s = new Sintax(new com.palmartec.analizador.LexerCup(new StringReader(st)));

            try {
                s.parse();
                taSintact.setText("Analisis realizado correctamente");
                taSintact.setForeground(new Color(25, 111, 61));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                Symbol sym = s.getS();
                taSintact.setText("Error de sintaxis. Linea: "
                        + (sym.right + 1) + " Columna: " + (sym.left + 1)
                        + ", Texto: \"" + sym.value + "\"");
                taSintact.setForeground(Color.RED);
            }
        }

    }
}
