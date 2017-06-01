
package com.jonheard.compilers.parser_java.ir;

import java.util.ArrayList;
import java.util.List;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class List_QualifiedId extends BaseIrType {
  public List_QualifiedId(Parser parser) {
    this(parser, false);
  }
  public List_QualifiedId(Parser parser, boolean isEmpty) {
    super(parser);
    if (!isEmpty) {
      do {
        addChild(new QualifiedId(parser));
      } while (parser.passTokenIfType(TokenType.COMMA));
    }
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder();
    String prefix = "";
    List<QualifiedId> ids = getValue();
    for (QualifiedId id : ids) {
      result.append(prefix);
      result.append(id.getValue());
      prefix = ", ";
    }
    return result.toString();
  }
  
  public QualifiedId getChildAsQualifiedId(int index) {
    return (QualifiedId)getChild(index);
  }
  
  public List<QualifiedId> getValue() {
    ArrayList<QualifiedId> result = new ArrayList<>();
    int childCount = getChildCount();
    for (int i = 0; i < childCount; ++i) {
      result.add((QualifiedId)getChild(i));
    }
    return result;
  }
}
