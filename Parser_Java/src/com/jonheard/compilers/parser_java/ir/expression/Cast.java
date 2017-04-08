
package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;

public class Cast extends Expression {
  public Cast(Token next, QualifiedId castTo, Expression rhs) {
    super(ExpressionType.CAST, next);
    addChild(castTo);
    addChild(rhs);
  }
}
