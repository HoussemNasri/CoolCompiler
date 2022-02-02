ASSN = 4J
CLASS= cs143
CLASSDIR= /mnt/c/Users/housi/Desktop/Stanford-Compilers/Stanford-CS143-Compilers
MAKEFILEDIR = ${CLASSDIR}/assignments/PA4J
SRC= ClassTable.java TreeConstants.java cool-tree.java good.cl bad.cl README
CSRC = \
	ASTConstants.java \
	ASTLexer.java \
	ASTParser.java \
	AbstractSymbol.java \
	AbstractTable.java \
	BoolConst.java \
	Flags.java \
	IdSymbol.java \
	IdTable.java \
	IntSymbol.java \
	IntTable.java \
	ListNode.java \
	Semant.java \
	StringSymbol.java \
	StringTable.java \
	CgenSupport.java \
	SymbolTable.java \
	SymtabExample.java \
	TokenConstants.java \
	TreeNode.java \
	Utilities.java
TSRC= mysemant mycoolc cool-tree.aps
CGEN= 
LIBS= lexer parser cgen
CFIL= ${CSRC} ${CGEN} ClassTable.java TreeConstants.java cool-tree.java 
HFIL= 
LSRC= Makefile
CLS= ${CFIL:%.java=./classes/%.class}
#CLS= ${CFIL:%.java=%.class}
OUTPUT= good.output bad.output

JAVAC := javac

CUPCLASSPATH := ${CLASSDIR}/lib

CLASSPATH := ${CUPCLASSPATH}:.

source: ${SRC} ${TSRC} ${LSRC} ${LIBS} ${CSRC}

${OUTPUT}:	semant good.cl bad.cl
	@rm -f ${OUTPUT}
	./mysemant good.cl >good.output 2>&1 
	-./mysemant bad.cl >bad.output 2>&1 

semant: Makefile ${CLS}
	@rm -f semant
	echo '#!/bin/sh' >> semant
	echo 'java -classpath ${CLASSPATH}:${MAKEFILEDIR}/classes/:`dirname $$0` Semant $$*' >> semant
	chmod 755 semant

symtab-example: Makefile ${CLS}
	@rm -f symtab-example
	echo '#!/bin/sh' >> symtab-example
	echo 'java -classpath ${CLASSPATH} SymtabExample $$*' >> symtab-example
	chmod 755 symtab-example

dotest:	semant good.cl bad.cl
	@echo "\nRunning semant on good.cl\n"
	-./mysemant good.cl 
	@echo "\nRunning semant on bad.cl\n"
	-./mysemant bad.cl

## cool-tree.class is not a real class file, but we need to have it
## for dependency tracking
${CLS}: ${CFIL}
	${JAVAC} -d ${MAKEFILEDIR}/classes/ -g -nowarn -classpath ${CLASSPATH} ${CFIL}
	touch cool-tree.class
#cp ./classes/Semant.class ./Semant.class

${LIBS}:
	${CLASSDIR}/etc/link-object ${ASSN} $@

# These dependencies allow you to get the starting files for
# the assignment.  They will not overwrite a file you already have.

${SRC} :								
	-${CLASSDIR}/etc/copy-skel ${ASSN} ${SRC}

${LSRC} :
	-${CLASSDIR}/etc/link-shared ${ASSN} ${LSRC}

${TSRC} ${CSRC}:
	-ln -s ${CLASSDIR}/src/PA${ASSN}/$@ $@

${HSRC}:
	-ln -s ${CLASSDIR}/include/PA${ASSN}/$@ $@

submit-clean: ${OUTPUT}
	-rm -f *.s *.class core ${CLS} *~ ${CGEN} parser cgen semant lexer symtab-example

clean :
	-rm -f ${OUTPUT} *.s core ./classes/* *.class ${CLS} ${CGEN} cgen semant symtab-example *~ 