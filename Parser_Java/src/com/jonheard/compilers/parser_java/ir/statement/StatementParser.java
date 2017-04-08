
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.ExpressionParser;

public class StatementParser {
  public static BaseIrType getNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    BaseIrType result = null;
    if (CodeBlock.getIsNext(parser)) {
      result = new CodeBlock(parser);
    } else if (If.getIsNext(parser)) {
      result = new If(parser);
    } else if (While.getIsNext(parser)) {
      result = new While(parser);
    } else if (Switch.getIsNext(parser)) {
      result = new Switch(parser);
    } else if (For.getIsNext(parser)) {
      if (EnhancedFor.getIsNext(parser)) {
        result = new EnhancedFor(parser);
      } else {
        result = new For(parser);
      }
    } else if (Do.getIsNext(parser)) {
      result = new Do(parser);
    } else if (Return.getIsNext(parser)) {
      result = new Return(parser);
    } else if (Break.getIsNext(parser)) {
      result = new Break(parser);
    } else if (Semicolon.getIsNext(parser)) {
      result = new Semicolon(parser);
    } else {
      result = ExpressionParser.parseExpressionStatment(parser);
    }
    return result;
  }
}
