package putils;

import java.util.*;
import java.util.stream.Collectors;
import java.io.Serializable;

enum Type {
    OP,
    VAR,
    VAL,
    NONE
}

public class LispNode implements Serializable {

    public Type type;
    public String name;
    public List<LispNode> children;

    public LispNode(Type type, String name, List<LispNode> children) {
        this.type = type;
        this.name = name;
        this.children = children;
    }

    public String printVoid () {
        String acc = this.name + "(";
        List<String> pchildren = this.children.stream().map(t -> t.printVoid()).collect(Collectors.toList());
        acc += String.join(", ", pchildren);
        acc += ")";
        return acc;
    }

    public long evaluate (List<Long> arg_list) {
        if (this.type == Type.OP) {
            switch (this.name) {
                case "+":
                    return this.children.get(0).evaluate(arg_list) +
                    this.children.get(1).evaluate(arg_list);
                case "-":
                    return this.children.get(0).evaluate(arg_list) -
                    this.children.get(1).evaluate(arg_list);
                default:
                    throw new AssertionError();
            }
        } else if (this.type == Type.VAL) {
            return Long.valueOf(this.name).longValue();
        } else /* type must be var */ {
            int index = Integer.valueOf(this.name.substring(1)).intValue();
            return arg_list.get(index);
        }
    }

    @Override
    public String toString() {
        return this.printVoid();
    }
}
