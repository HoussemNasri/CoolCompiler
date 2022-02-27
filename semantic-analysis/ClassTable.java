import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * This class may be used to contain the semantic information such as
 * the inheritance graph.  You may use it or not as you like: it is only
 * here to provide a container for the supplied methods.
 */
class ClassTable extends AbstractTable {
    private int semantErrors = 0;
    private PrintStream errorStream = System.err;
    private List<class_c> basicClasses = new ArrayList<>();
    private List<class_c> classes = new ArrayList<>();
    private List<class_c> userDefinedClasses = new ArrayList<>();

    private SymbolTable symbolTable = new SymbolTable();

    public SymbolTable symbolTable() {
        return symbolTable;
    }

    /**
     * Creates data structures representing basic Cool classes (Object,
     * IO, Int, Bool, String).  Please note: as is this method does not
     * do anything useful; you will need to edit it to make if do what
     * you want.
     */
    private void installBasicClasses() {
        AbstractSymbol filename
                = AbstractTable.stringtable.addString("<basic class>");

        // The following demonstrates how to create dummy parse trees to
        // refer to basic Cool classes.  There's no need for method
        // bodies -- these are already built into the runtime system.

        // IMPORTANT: The results of the following expressions are
        // stored in local variables.  You will want to do something
        // with those variables at the end of this method to make this
        // code meaningful.

        // The Object class has no parent class. Its methods are
        //        cool_abort() : Object    aborts the program
        //        type_name() : Str        returns a string representation
        //                                 of class name
        //        copy() : SELF_TYPE       returns a copy of the object

        class_c Object_class =
                new class_c(0,
                            TreeConstants.Object_,
                            TreeConstants.No_class,
                            new Features(0)
                                    .appendElement(new method(0,
                                                              TreeConstants.cool_abort,
                                                              new Formals(0),
                                                              TreeConstants.Object_,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.type_name,
                                                              new Formals(0),
                                                              TreeConstants.Str,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.copy,
                                                              new Formals(0),
                                                              TreeConstants.SELF_TYPE,
                                                              new no_expr(0))),
                            filename);

        // The IO class inherits from Object. Its methods are
        //        out_string(Str) : SELF_TYPE  writes a string to the output
        //        out_int(Int) : SELF_TYPE      "    an int    "  "     "
        //        in_string() : Str            reads a string from the input
        //        in_int() : Int                "   an int     "  "     "

        class_c IO_class =
                new class_c(0,
                            TreeConstants.IO,
                            TreeConstants.Object_,
                            new Features(0)
                                    .appendElement(new method(0,
                                                              TreeConstants.out_string,
                                                              new Formals(0)
                                                                      .appendElement(new formalc(0,
                                                                                                 TreeConstants.arg,
                                                                                                 TreeConstants.Str)),
                                                              TreeConstants.SELF_TYPE,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.out_int,
                                                              new Formals(0)
                                                                      .appendElement(new formalc(0,
                                                                                                 TreeConstants.arg,
                                                                                                 TreeConstants.Int)),
                                                              TreeConstants.SELF_TYPE,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.in_string,
                                                              new Formals(0),
                                                              TreeConstants.Str,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.in_int,
                                                              new Formals(0),
                                                              TreeConstants.Int,
                                                              new no_expr(0))),
                            filename);

        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        class_c Int_class =
                new class_c(0,
                            TreeConstants.Int,
                            TreeConstants.Object_,
                            new Features(0)
                                    .appendElement(new attr(0,
                                                            TreeConstants.val,
                                                            TreeConstants.prim_slot,
                                                            new no_expr(0))),
                            filename);

        // Bool also has only the "val" slot.
        class_c Bool_class =
                new class_c(0,
                            TreeConstants.Bool,
                            TreeConstants.Object_,
                            new Features(0)
                                    .appendElement(new attr(0,
                                                            TreeConstants.val,
                                                            TreeConstants.prim_slot,
                                                            new no_expr(0))),
                            filename);

        // The class Str has a number of slots and operations:
        //       val                              the length of the string
        //       str_field                        the string itself
        //       length() : Int                   returns length of the string
        //       concat(arg: Str) : Str           performs string concatenation
        //       substr(arg: Int, arg2: Int): Str substring selection

        class_c Str_class =
                new class_c(0,
                            TreeConstants.Str,
                            TreeConstants.Object_,
                            new Features(0)
                                    .appendElement(new attr(0,
                                                            TreeConstants.val,
                                                            TreeConstants.Int,
                                                            new no_expr(0)))
                                    .appendElement(new attr(0,
                                                            TreeConstants.str_field,
                                                            TreeConstants.prim_slot,
                                                            new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.length,
                                                              new Formals(0),
                                                              TreeConstants.Int,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.concat,
                                                              new Formals(0)
                                                                      .appendElement(new formalc(0,
                                                                                                 TreeConstants.arg,
                                                                                                 TreeConstants.Str)),
                                                              TreeConstants.Str,
                                                              new no_expr(0)))
                                    .appendElement(new method(0,
                                                              TreeConstants.substr,
                                                              new Formals(0)
                                                                      .appendElement(new formalc(0,
                                                                                                 TreeConstants.arg,
                                                                                                 TreeConstants.Int))
                                                                      .appendElement(new formalc(0,
                                                                                                 TreeConstants.arg2,
                                                                                                 TreeConstants.Int)),
                                                              TreeConstants.Str,
                                                              new no_expr(0))),
                            filename);

	/* Do somethind with Object_class, IO_class, Int_class,
           Bool_class, and Str_class here */
        basicClasses.addAll(Arrays.asList(Str_class, Object_class, Bool_class, IO_class, Int_class));
        for (class_c class_c : basicClasses) {
            addClass(class_c);
        }
    }

    public ClassTable(Classes classes) {
        class_c mainClass = checkForMainClass(classes);
        checkForMainMethod(mainClass);
        installBasicClasses();
        checkForBasicClassRedefinition(classes);
        installUserDefinedClasses(classes);
        checkForInheritingMissingClass();
        checkForIllegalInheritanceFromBasicClass();
        checkForUserDefinedClassRedefinition();
    }

    private void checkForIllegalInheritanceFromBasicClass() {
        for (class_c classC : userDefinedClasses) {
            if (!canInheritFrom(classC.parent)) {
                fatalSemantError(classC, "Class %s cannot inherit class %s%n", classC.name.str, classC.parent.str);
            }
        }
    }

    private void checkForInheritingMissingClass() {
        for (class_c classC : userDefinedClasses) {
            if (!exists(classC.parent)) {
                fatalSemantError(classC, "Class %s inherits from an undefined class %s%n", classC.name.str, classC.parent.str);
            }
        }
    }

    private void fatalSemantError(class_c classC, String format, Object... args) {
        semantError(classC).printf(format, args);
        System.err.println("Compilation halted due to static semantic errors.");
        System.exit(1);
    }

    private void fatalSemantError(String message) {
        semantError(message);
        System.err.println("Compilation halted due to static semantic errors.");
        System.exit(1);
    }

    private void checkForBasicClassRedefinition(Classes classes) {
        Enumeration elements = classes.getElements();
        while (elements.hasMoreElements()) {
            class_c class_c = ((class_c) elements.nextElement());
            for (class_c basic : basicClasses) {
                if (basic.name.str.equals(class_c.getName().str)) {
                    fatalSemantError(class_c, "Redefinition of basic class %s.%n", basic.name.str);
                    System.exit(1);
                }
            }
        }
    }

    private void checkForUserDefinedClassRedefinition() {
        // TODO('Handle class duplication')
        for (int i = 0; i < classes.size(); i++) {
            class_c selectClass = classes.get(i);
            for (int j = i + 1; j < classes.size(); j++) {
                class_c compareClass = classes.get(j);
                if (selectClass.name.equals(compareClass.name)) {
                    fatalSemantError(compareClass, "Class %s was previously defined.%n", selectClass.name.str);
                }
            }
        }
        //redefinedclass.test:9: Class A was previously defined.
        //Compilation halted due to static semantic errors.
    }

    private class_c checkForMainClass(Classes classes) {
        Enumeration elements = classes.getElements();
        while (elements.hasMoreElements()) {
            class_c classC = (class_c) elements.nextElement();
            if (classC.name.str.equals("Main")) {
                return classC;
            }
        }

        fatalSemantError("Class Main is not defined.");

        return null;
    }

    private void checkForMainMethod(class_c mainClass) {
        if (mainClass == null) {
            return;
        }
        Enumeration elements = mainClass.features.getElements();
        while (elements.hasMoreElements()) {
            Object feature = elements.nextElement();
            if (feature instanceof method) {
                method m = (method) feature;
                if (m.name.str.equals("main")) {
                    return;
                }
            }
        }
        fatalSemantError("Main method is not defined.");
        System.exit(1);
    }

    public void installUserDefinedClasses(Classes classes) {
        Enumeration elements = classes.getElements();
        while (elements.hasMoreElements()) {
            class_c classC = (class_c) elements.nextElement();
            addClass(classC);
            userDefinedClasses.add(classC);
        }
    }

    private void addClass(class_c c) {
        addString(c.name.str);
        classes.add(c);
    }

    /**
     * Prints line number and file name of the given class.
     *
     * Also increments semantic error count.
     *
     * @param c the class
     * @return a print stream to which the rest of the error message is
     * to be printed.
     */
    public PrintStream semantError(class_c c) {
        return semantError(c.getFilename(), c);
    }

    public PrintStream semantError(String message) {
        return semantError().printf("%s%n", message);
    }

    /**
     * Prints the file name and the line number of the given tree node.
     *
     * Also increments semantic error count.
     *
     * @param filename the file name
     * @param t the tree node
     * @return a print stream to which the rest of the error message is
     * to be printed.
     */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /**
     * Increments semantic error count and returns the print stream for
     * error messages.
     *
     * @return a print stream to which the error message is
     * to be printed.
     */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /**
     * Returns true if there are any static semantic errors.
     */
    public boolean hasErrors() {
        return semantErrors != 0;
    }

    public boolean isBasicClass(AbstractSymbol symbol) {
        for (class_c clazz : basicClasses) {
            if (!symbol.str.isEmpty()) {
                if (clazz.name.str.equals(symbol.str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLegalType(AbstractSymbol type) {
        Enumeration symbols = getSymbols();
        while (symbols.hasMoreElements()) {
            AbstractSymbol symbol = (AbstractSymbol) symbols.nextElement();
            if (symbol.str.equals(type.str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the inherited and non-inherited features of {@code classType}.
     */
    public Features getAllClassFeatures(AbstractSymbol classType) {
        for (class_c classC : classes) {
            if (classC.name.equals(classType)) {
                Features allFeatures = new Features(0);
                for (Enumeration e = classC.features.getElements(); e.hasMoreElements(); ) {
                    allFeatures.addElement((TreeNode) e.nextElement());
                }
                for (Enumeration e = classC.getParentFeatures().getElements(); e.hasMoreElements(); ) {
                    allFeatures.addElement((TreeNode) e.nextElement());
                }
                return allFeatures;
            }
        }
        return null;
    }

    public boolean canInheritFrom(AbstractSymbol symbol) {
        return !(symbol.str.equals("Bool") || symbol.str.equals("String") || symbol.str.equals("Int"));
    }

    public boolean canRedefine(AbstractSymbol symbol) {
        return !(symbol.str.equals("Bool") || symbol.str.equals("String") || symbol.str.equals("Int") || symbol.str.equals("IO"));
    }

    @Override
    protected AbstractSymbol getNewSymbol(String s, int len, int index) {
        return new IdSymbol(s, len, index);
    }

    private class_c currentClass = null;

    /**
     * The dispatch type can be omitted if so the current class it is invoked in will be used.
     */
    public void enterClass(class_c classC) {
        this.currentClass = classC;
    }

    public class_c getCurrentClass() {
        return currentClass;
    }

    public class_c lookupClass(AbstractSymbol lookupName) {
        for (class_c classC : classes) {
            if (classC.name.equals(lookupName)) {
                return classC;
            }
        }
        return null;
    }

    public Classes getUserDefinedAndBasicClasses() {
        return new Classes(0, new Vector<>(classes));
    }

    public method lookupMethod(AbstractSymbol methodName, AbstractSymbol fromClass) {
        Features classFeatures = getAllClassFeatures(fromClass);
        if (classFeatures == null) {
            return null;
        }

        List<Feature> allFeatures = Collections.list(getAllClassFeatures(fromClass).getElements());
        for (Feature feature : allFeatures) {
            if (feature.isMethod()) {
                if (feature.asMethod().name.equals(methodName)) {
                    return feature.asMethod();
                }
            }
        }
        return null;
    }

    public attr lookupAttribute(AbstractSymbol attrName) {
        return lookupAttribute(attrName, currentClass.name);
    }

    public attr lookupAttribute(AbstractSymbol attrName, AbstractSymbol fromClass) {
        if (fromClass == null) {
            return null;
        }
        Features classFeatures = getAllClassFeatures(fromClass);
        if (classFeatures == null) {
            return null;
        }

        List<Feature> allFeatures = Collections.list(getAllClassFeatures(fromClass).getElements());
        for (Feature feature : allFeatures) {
            if (feature.isAttribute()) {
                if (feature.asAttr().name.equals(attrName)) {
                    return feature.asAttr();
                }
            }
        }
        return null;
    }

    public boolean isAttribute(AbstractSymbol name, AbstractSymbol clazz) {
        return lookupAttribute(name, clazz) != null;
    }

    public boolean isAttribute(AbstractSymbol name) {
        if (currentClass != null) {
            return lookupAttribute(name, currentClass.name) != null;
        }
        return false;
    }

    public boolean isSubtypeOfClass(AbstractSymbol subtype, AbstractSymbol superType) {
        class_c _superclass = lookupClass(superType);
        class_c _subtype = lookupClass(subtype);

        Objects.requireNonNull(_superclass);
        Objects.requireNonNull(_subtype);

        while (_subtype != null && !(_subtype.name.equals(TreeConstants.No_type))) {
            if (_subtype.name.equals(superType)) {
                return true;
            }
            _subtype = lookupClass(_subtype.getParent());
        }
        return false;
    }
}
			  
    
