/* Generated By:JavaCC: Do not edit this line. RTSim_ParserVisitor.java Version 5.0 */
package de.uniluebeck.iti.rteasy.frontend;

public interface RTSim_ParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTRtProg node, Object data);
  public Object visit(ASTDecls node, Object data);
  public Object visit(ASTDecl node, Object data);
  public Object visit(ASTRegBusDeclList node, Object data);
  public Object visit(ASTRegBusDecl node, Object data);
  public Object visit(ASTMemDeclList node, Object data);
  public Object visit(ASTMemDecl node, Object data);
  public Object visit(ASTRegArrayDeclList node, Object data);
  public Object visit(ASTRegArrayDecl node, Object data);
  public Object visit(ASTStat_Seq node, Object data);
  public Object visit(ASTOuter_ParStats node, Object data);
  public Object visit(ASTIf_Stat node, Object data);
  public Object visit(ASTSwitch_Case_Stat node, Object data);
  public Object visit(ASTCaseList node, Object data);
  public Object visit(ASTInner_ParStats node, Object data);
  public Object visit(ASTExpr node, Object data);
  public Object visit(ASTNum_Const node, Object data);
  public Object visit(ASTBit_Seq node, Object data);
  public Object visit(ASTStat node, Object data);
}
/* JavaCC - OriginalChecksum=45f922101c397a2274f4c5518e86bdb8 (do not edit this line) */
