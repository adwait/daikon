//
// Generated by JTB 1.3.2
//

package jtb.syntaxtree;

/**
 * Grammar production:
 * f0 -> ( ( "public" | "static" | "protected" | "private" | "final" | "abstract" | "synchronized" | "native" | "transient" | "volatile" | "strictfp" | Annotation() ) )*
 */
public class Modifiers implements Node {
   private Node parent;
   public NodeListOptional f0;

   public Modifiers(NodeListOptional n0) {
      f0 = n0;
      if ( f0 != null ) f0.setParent(this);
   }

   public void accept(jtb.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(jtb.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(jtb.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(jtb.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
   public void setParent(Node n) { parent = n; }
   public Node getParent()       { return parent; }
}

