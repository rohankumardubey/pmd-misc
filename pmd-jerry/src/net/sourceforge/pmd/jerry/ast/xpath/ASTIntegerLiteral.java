/* Generated By:JJTree: Do not edit this line. ASTIntegerLiteral.java */

package net.sourceforge.pmd.jerry.ast.xpath;

import net.sourceforge.pmd.jerry.ast.xpath.custom.ImageNode;

public class ASTIntegerLiteral extends ImageNode {
  public ASTIntegerLiteral(int id) {
    super(id);
  }

  public ASTIntegerLiteral(XPath2Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(XPath2ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
