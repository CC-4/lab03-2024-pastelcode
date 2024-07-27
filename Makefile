#
#    Laboratorio No. 3 - Recursive Descent Parsing
#    CC4 - Compiladores
#
#    Makefile ...
#

JFLAGS=
SUBDIR=$(PWD)/tree
CLASSPATH=$(PWD)/lib/jlex.jar

lexer: lexer.lex
	java -classpath $(CLASSPATH) JLex.Main lexer.lex
	mv lexer.lex.java Lexer.java
	javac Lexer.java
	$(RM) lexer
	echo '#!/bin/sh' >> lexer
	echo 'java Lexer $$*' >> lexer
	chmod a+x lexer

parser: Parser.java lexer
	javac Main.java
	$(RM) parser
	echo '#!/bin/sh' >> parser
	echo 'java Main' >> parser
	chmod a+x parser

.PHONY: clean

clean:
	$(RM) lexer
	$(RM) parser
	$(RM) Lexer.java
	$(RM) -r *.class
