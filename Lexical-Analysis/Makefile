
ASSN = 2J
CLASS= cs143
CLASSDIR= /mnt/c/Users/housi/Desktop/Stanford-Compilers/CS143
MAKEFILEDIR = ${CLASSDIR}/Stanford-CS143-Compilers/Lexical-Analysis
LIB= -L/usr/pubsw/lib -lfl -R/usr/pubsw/lib

SRC= cool.lex test.cl README
CSRC=	\
	AbstractSymbol.java \
	AbstractTable.java \
	BoolConst.java \
	CgenSupport.java \
	Flags.java \
	IdSymbol.java \
	IdTable.java \
	IntSymbol.java \
	IntTable.java \
	Lexer.java \
	StringSymbol.java \
	StringTable.java \
	TokenConstants.java \
	Utilities.java
TSRC= mycoolc
CGEN= CoolLexer.java
LIBS= parser semant cgen
CFIL= ${CSRC} ${CGEN}
LSRC= Makefile
CLS= ${CFIL:%.java=./classes/%.class}
OUTPUT= test.output

CUPCLASSPATH := ${CLASSDIR}/lib/java-cup-11a.jar:${CLASSDIR}/lib/jlex.jar

JAVAC := javac
JLEX := java -classpath ${CLASSDIR}/lib/jlex.jar JLex.Main

# sm: we need rt.jar on Solaris/x86, and I'm hoping it won't
# cause a problem elsewhere
CLASSPATH := ${CUPCLASSPATH}:${MAKEFILEDIR}:/usr/java/lib/rt.jar

source : ${SRC} ${TSRC} ${LSRC} ${LIBS} ${CSRC}

${OUTPUT}:	lexer test.cl
	@rm -f test.output
	-./lexer test.cl >test.output 2>&1 

lexer: Makefile ${CLS}
	@rm -f lexer
	echo '#!/bin/sh' >> lexer
	echo 'java -classpath ${CLASSPATH}:${MAKEFILEDIR}/classes:`dirname $$0` Lexer $$*' >> lexer
	chmod 755 lexer

CoolLexer.java: cool.lex
	${JLEX} $<
	mv cool.lex.java CoolLexer.java

dotest:	lexer test.cl
	${MAKEFILEDIR}/lexer test.cl

${CLS}: ${CFIL}
	${JAVAC} -d ${MAKEFILEDIR}/classes/ -classpath ${CLASSPATH} ${CFIL}

${LIBS}:
	${CLASSDIR}/etc/link-object ${ASSN} $@

${TSRC} ${CSRC}:
	-ln -s ${CLASSDIR}/src/PA${ASSN}/$@ $@

clean :
	-rm -f CoolLexer.java *.s ${OUTPUT} ${CLS} *~ lexer parser cgen semant


