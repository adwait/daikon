package daikon.tools.jtb.cparser.customvisitor;

import daikon.tools.jtb.cparser.syntaxtree.*;
import daikon.tools.jtb.cparser.visitor.*;
import java.util.*;
import java.io.*;


public class ConditionPrinter extends DepthFirstVisitor {

    private Printer printer;
    private Converter converter = new Converter();
    private ArrayList actualStrings;
    private ArrayList stringArrays;
    private HashMap actualStringsByFunction;
    private HashMap stringArraysByFunction;
    private ArrayList localVariables;


    public ConditionPrinter(String fileName) throws IOException {
        printer = new Printer(fileName);
    }

    public void close() throws IOException {
        printer.close();
    }

    public void setActualStrings(HashMap l) {
        this.actualStringsByFunction = l;
    }

    public void setStringArrays(HashMap l) {
        this.stringArraysByFunction = l;
    }


    /**
     * f0 -> [ DeclarationSpecifiers() ]
     * f1 -> Declarator()
     * f2 -> [ DeclarationList() ]
     * f3 -> CompoundStatement()
     */

    public void visit(FunctionDefinition n) {
        String functionName = n.f1.f1.f0.choice.toString();
        this.actualStrings = (ArrayList)actualStringsByFunction.get(functionName);
        this.stringArrays = (ArrayList)stringArraysByFunction.get(functionName);
        converter.actualStrings = this.actualStrings;
        converter.stringArrays = this.stringArrays;
        printer.println();
        printer.println();
        printer.print("PPT_NAME std." + functionName);
        printer.println();
        DeclarationList l = (DeclarationList) n.f3.f1.node;
        localVariables = new ArrayList();
        localVariables.addAll(actualStringsByFunction.keySet());
        if (l!=null) {
            identifyLocalVariables(l.f0.nodes);
        }
        super.visit(n);
    }


    private void identifyLocalVariables(Vector v) {
        for (int i = 0; i < v.size(); i++) {
            Declaration d = (Declaration) v.elementAt(i);
            NodeSequence temp = (NodeSequence)d.f0.f0.choice;
            if (temp.nodes.get(0) instanceof TypeSpecifier) {
                Node n = ((TypeSpecifier)(temp.nodes.get(0))).f0.choice;
                if (n instanceof TypedefName) {
                    TypedefName type = (TypedefName) n;
                    Vector decls = new Vector();
                    InitDeclaratorList list = (InitDeclaratorList) d.f1.node;
                    decls.add(list.f0); // get the first in "boolean first, second,third;"
                    // get the second, third, in "boolean first, second, third"
                    Vector tempVec = list.f1.nodes;
                    for (int k = 0; k < tempVec.size(); k++) {
                        decls.addAll(((NodeSequence)tempVec.get(k)).nodes);
                    }

                    for (int j = 0; j < decls.size(); j++) {
                        InitDeclarator curr=null;;
                        // filter out the "," node tokens
                        if (decls.elementAt(j) instanceof InitDeclarator) {
                            curr = (InitDeclarator) decls.elementAt(j);
                        }
                        // if it is a node token, nothing will be printed
                        if (curr != null) {
                            localVariables.add((curr.f0.f1.f0.choice).toString().trim());
                        }
                    }

                }
                else {
                    Vector decls = new Vector();
                    InitDeclaratorList list = (InitDeclaratorList) d.f1.node;
                    decls.add(list.f0); // get the first in "boolean first, second,third;"
                    // get the second, third, in "boolean first, second, third"
                    Vector tempVec = list.f1.nodes;
                    for (int k = 0; k < tempVec.size(); k++) {
                        decls.addAll(((NodeSequence)tempVec.get(k)).nodes);
                    }
                    for (int j = 0; j < decls.size(); j++) {
                        InitDeclarator curr=null;;
                        // filter out the "," node tokens
                        if (decls.elementAt(j) instanceof InitDeclarator) {
                            curr = (InitDeclarator) decls.elementAt(j);
                        }
                        // if it is a node token, nothing will be printed
                        if (curr != null) {
                            localVariables.add(curr.f0.f1.f0.choice.toString().trim());
                        }
                    }
                }
            }
        }
    }



    public void visit(IterationStatement n) {
        NodeSequence seq = (NodeSequence)n.f0.choice;
        String loop = seq.nodes.get(0).toString();
        if (loop.equals("while")) {
            printExpression((Expression)seq.nodes.get(2));

        }
        else if (loop.equals("do")) {
            printExpression((Expression)seq.nodes.get(4));
        }
        else if (loop.equals("for")) {
            printExpression((Expression)((NodeOptional)seq.nodes.get(4)).node );
        }
        n.f0.accept(this);
    }

    public void printExpression(Expression n) {
	printer.setFilter(localVariables);
       	n.accept(converter);
	n.accept(printer);
	printer.commit();
    }


    private boolean isString(String s) {
        return actualStrings.contains(s.trim());
    }

    /**
     * f0 -> ( <IF> "(" Expression() ")" Statement() [ <ELSE> Statement() ] | <SWITCH> "(" Expression() ")" Statement() )
     */
    public void visit(SelectionStatement n) {
        NodeSequence seq = (NodeSequence) n.f0.choice;
        if (seq.nodes.get(0).toString().equals("if")) {
            printExpression(((Expression)seq.nodes.get(2)));
        }
        n.f0.accept(this);
    }


    // the following methods check that the nodes not not null before visiting them
    // because the AST is modified by the Converter to have null pointers

    public void visit(LogicalANDExpression n) {
	if (n.f0 !=null) {
	    n.f0.accept(this);
	}
	n.f1.accept(this);
    }

    public void visit(LogicalORExpression n) {
	if (n.f0 !=null) {
	    n.f0.accept(this);
	}
	n.f1.accept(this);
    }

    public void visit(EqualityExpression n) {
        if (n.f0 != null) {
            n.f0.accept(this);
        }
        n.f1.accept(this);
    }

    public void visit(RelationalExpression n) {
        if (n.f0 != null) {
            n.f0.accept(this);
        }
        n.f1.accept(this);
    }
}
