/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.lang.Math;
import java.util.LinkedList;
import java.util.Stack;

public class Parser {

  // Puntero next que apunta al siguiente token
  private int next;
  // Stacks para evaluar en el momento
  private Stack<Double> operandos;
  private Stack<Token> operadores;
  // LinkedList de tokens
  private LinkedList<Token> tokens;

  // Funcion que manda a llamar main para parsear la expresion
  public boolean parse(LinkedList<Token> tokens) {
    this.tokens = tokens;
    this.next = 0;
    this.operandos = new Stack<Double>();
    this.operadores = new Stack<Token>();

    // Recursive Descent Parser
    // Imprime si el input fue aceptado
    // System.out.println("Aceptada? " + S());

    // If it's not a valid input, cancel operation.
    if (!S()) {
      return false;
    }

    // Shunting Yard Algorithm
    // Imprime el resultado de operar el input
    // System.out.println("Resultado: " + this.operandos.peek());
    System.out.println(operandos.peek());

    // Verifica si terminamos de consumir el input
    if (this.next != this.tokens.size()) {
      return false;
    }
    return true;
  }

  // Verifica que el id sea igual que el id del token al que apunta next
  // Si si avanza el puntero es decir lo consume.
  private boolean term(int id) {
    if (
      this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)
    ) {
      // Codigo para el Shunting Yard Algorithm
      if (id == Token.NUMBER) {
        // Encontramos un numero
        // Debemos guardarlo en el stack de operandos
        operandos.push(this.tokens.get(this.next).getVal());
      } else if (id == Token.SEMI) {
        // Encontramos un punto y coma
        // Debemos operar todo lo que quedo pendiente
        while (!this.operadores.empty()) {
          popOp();
        }
      } else {
        // Encontramos algun otro token, es decir un operador
        // Lo guardamos en el stack de operadores
        // Que pushOp haga el trabajo, no quiero hacerlo yo aqui
        pushOp(this.tokens.get(this.next));
      }

      this.next++;
      return true;
    }
    return false;
  }

  // Funcion que verifica la precedencia de un operador
  private int pre(Token op) {
    /* TODO: Su codigo aqui */
    /* El codigo de esta seccion se explicara en clase */
    switch (op.getId()) {
      case Token.UNARY:
        return 4;
      case Token.EXP:
        return 3;
      case Token.MULT:
      case Token.DIV:
      case Token.MOD:
        return 2;
      case Token.PLUS:
      case Token.MINUS:
        return 1;
      default:
        return -1;
    }
  }

  private void popOp() {
    /* TODO: Su codigo aqui */
    /* El codigo de esta seccion se explicara en clase */
    Token op = operadores.pop();
    double result = 0;
    switch (op.getId()) {
      case Token.PLUS: {
        double b = operandos.pop();
        double a = operandos.pop();
        System.out.printf("suma %f + %f\n", a, b);
        result = a + b;
        break;
      }
      case Token.MULT: {
        double b = operandos.pop();
        double a = operandos.pop();
        System.out.printf("mult %f * %f\n", a, b);
        result = a * b;
        break;
      }
      case Token.DIV: {
        double b = operandos.pop();
        double a = operandos.pop();
        System.out.printf("div %f / %f\n", a, b);
        result = a / b;
        break;
      }
      case Token.MOD: {
        double b = operandos.pop();
        double a = operandos.pop();
        System.out.printf("mod %f %% %f\n", a, b);
        result = a % b;
        break;
      }
      case Token.EXP: {
        double b = operandos.pop();
        double a = operandos.pop();
        System.out.printf("exp %f ^ %f\n", a, b);
        result = Math.pow(a, b);
        break;
      }
      case Token.UNARY: {
        double a = operandos.pop();
        System.out.printf("neg ~%f\n", a);
        result = a * -1;
        break;
      }
    }
    operandos.push(result);
  }

  private void pushOp(Token op) {
    /* TODO: Su codigo aqui */
    /* Casi todo el codigo para esta seccion se vera en clase */

    // Si no hay operandos automaticamente ingresamos op al stack
    if (operadores.size() == 0) {
      operadores.push(op);
      return;
    }

    // Si sí hay operandos:

    // Left parents are freely pushed into operators stack.
    if (op.getId() == Token.LPAREN) {
      operadores.push(op);
      return;
    }

    if (op.getId() == Token.RPAREN) {
      // Reach the nearest left parenthesis by operating operators in the middle.
      while (operadores.peek().getId() != Token.LPAREN) {
        popOp();
      }
      // Take left parenthesis out.
      operadores.pop();
      // Finish stage.
      return;
    }

    // Obtenemos la precedencia de op
    int operatorPrecedence = pre(op);

    // Obtenemos la precedencia de quien ya estaba en el stack
    // Comparamos las precedencias y decidimos si hay que operar
    // Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
    while (
      !operadores.isEmpty() && operatorPrecedence <= pre(operadores.peek())
    ) {
      popOp();
    }

    // Al terminar operaciones pendientes, guardamos op en stack
    operadores.push(op);
  }

  private boolean S() {
    return E() && term(Token.SEMI);
  }

  private boolean E1() {
    return term(Token.UNARY) && E() && N();
  }

  private boolean E2() {
    return term(Token.LPAREN) && E() && term(Token.RPAREN) && N();
  }

  private boolean E3() {
    return term(Token.NUMBER) && N();
  }

  private boolean E() {
    int saved = next;

    next = saved;
    if (E1()) {
      return true;
    }

    next = saved;
    if (E2()) {
      return true;
    }

    next = saved;
    if (E3()) {
      return true;
    }

    return false;
  }

  private boolean N1() {
    return term(Token.PLUS) && E() && N();
  }

  private boolean N2() {
    return term(Token.MINUS) && E() && N();
  }

  private boolean N3() {
    return term(Token.MULT) && E() && N();
  }

  private boolean N4() {
    return term(Token.DIV) && E() && N();
  }

  private boolean N5() {
    return term(Token.MOD) && E() && N();
  }

  private boolean N6() {
    return term(Token.EXP) && E() && N();
  }

  private boolean N7() {
    return true;
  }

  private boolean N() {
    int saved = next;

    next = saved;
    if (N1()) {
      return true;
    }

    next = saved;
    if (N2()) {
      return true;
    }

    next = saved;
    if (N3()) {
      return true;
    }

    next = saved;
    if (N4()) {
      return true;
    }

    next = saved;
    if (N5()) {
      return true;
    }

    next = saved;
    if (N6()) {
      return true;
    }

    next = saved;
    if (N7()) {
      return true;
    }

    return false;
  }
  /* TODO: sus otras funciones aqui */
}
