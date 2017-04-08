
package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.expression.Expression;
import com.jonheard.compilers.parser_java.ir.expression.ExpressionParser;
import com.jonheard.compilers.parser_java.ir.statement.CodeBlock;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Member extends BaseIrType {
  public Member(Parser parser) {
    this(parser, false, false, false, false);
  }

  public Member(Parser parser, boolean forceVariable, boolean forceNoModifiers,
      boolean forceNoInitializer, boolean forceNoSemicolon) {
    super(parser);
    addChild(forceNoModifiers ? new List_Modifier() : new List_Modifier(parser));
    addChild(new Type(parser));
    addChild(new Id(parser));
    if (!forceVariable && parser.passTokenIfType(TokenType.LEFT_PAREN)) {
      isMethod = true;
      addChild(new List_Variable(parser));
      parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
      addChild(new CodeBlock(parser));
    } else {
      Type type = getType();
      while (parser.passTokenIfType(TokenType.LEFT_SQUARE)) {
        parser.requireTokenToBeOfType(TokenType.RIGHT_SQUARE);
        type.incDimensionCount();
      }
      if (!forceNoInitializer && parser.passTokenIfType(TokenType.EQUAL)) {
        addChild(ExpressionParser.parseExpression(parser));
      }
      if (!forceNoSemicolon) {
        parser.requireTokenToBeOfType(TokenType.SEMICOLON);
      }
    }
  }

  public String toJvmDescriptor() {
    StringBuilder result = new StringBuilder();
    if (getIsMethod()) {
      List_Variable params = getParameterList();
      result.append('(');
      for (int i = 0; i < params.getChildCount(); i++) {
        Type pType = ((Member)params.getChild(i)).getType();
        result.append(pType.toJvmDescriptor());
      }
      result.append(')');
    }
    result.append(getType().toJvmDescriptor());
    return result.toString();
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    boolean result = false;
    parser.getTokenQueue().remember();
    new List_Modifier(parser);
    if (Type.getIsNext(parser)) {
      new Type(parser);
      if (Id.getIsNext(parser)) {
        result = true;
      }
    }
    parser.getTokenQueue().rewind();
    return result;
  }

  @Override
  public String getHeaderString() {
    return
        "id='" + getId().getValue() + "' " +
        "type='" + getType().getValue() + "' " +
        "modifiers='" + getModifiers().getValue() + "' " +
        "isMethod='" + getIsMethod() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 3; }

  public void setId(Id value) {
    // bad input check
    if (value == null) { throw new IllegalArgumentException("Arg1(value): null"); }

    replaceChild(2, value);
  }

  public Expression getInitializer() {
    if (getIsMethod()) {
      return null;
    } else if (getChildCount() < 3) {
      return null;
    } else {
      return (Expression)getChild(3);
    }
  }
  public List_Variable getParameterList() {
    if (!getIsMethod())
      return null;
    return (List_Variable)getChild(3);
  }
  public CodeBlock getCodeBlock() {
    if (!getIsMethod())
      return null;
    return (CodeBlock)getChild(4);
  }
  public List_Modifier getModifiers() { return (List_Modifier)getChild(0); }
  public Type getType() { return (Type)getChild(1); }
  public Id getId() { return (Id)getChild(2); }
  public boolean getIsMethod() { return isMethod; }

  private boolean isMethod = false;
}
