/* Generated By:JJTree: Do not edit this line. ASTAbbrevForwardStep.java */

package net.sourceforge.pmd.jerry.ast.xpath;

import net.sourceforge.pmd.jerry.ast.xpath.custom.StepNode;

public class ASTAbbrevForwardStep extends StepNode {
  public ASTAbbrevForwardStep(int id) {
    super(id);
  }

  public ASTAbbrevForwardStep(XPath2Parser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(XPath2ParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
