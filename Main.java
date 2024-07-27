/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que implementa el interprete
*/

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        LinkedList<Token> tokens = new LinkedList<Token>();
        Parser parser = new Parser();
        Token token;
        while(true) {
            System.out.print(">>> ");
            Lexer lexer = new Lexer(new StringReader(bf.readLine()));
            while((token = lexer.nextToken()) != null) {
                tokens.add(token);
            }
            if(!parser.parse(tokens)) {
                System.err.println("BAD INPUT");
            }
            tokens.clear();
        }

    }

}
