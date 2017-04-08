
package com.jonheard.compilers.parser_java.ir;

import java.util.LinkedList;
import java.util.List;
import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class QualifiedId extends BaseIrType {
  public QualifiedId(Parser parser) {
    super(parser);
    addChild(new Id(parser));
    parser.getTokenQueue().remember();
    while (parser.passTokenIfType(TokenType.DOT)) {
      if (Id.getIsNext(parser)) {
        parser.getTokenQueue().rewind();
        parser.requireTokenToBeOfType(TokenType.DOT);
        addChild(new Id(parser));
        parser.getTokenQueue().remember();
      }
    }
    parser.getTokenQueue().rewind();
  }
  private QualifiedId(int row, int column, List<Id> children) {
    super(row, column);
    // bad input check
    if (children == null) { throw new IllegalArgumentException("Arg3(children): null"); }

    for (Id child : children) {
      addChild(child);
    }
  }

  // Replaces the Id's of this QualifiedId with new ones based on the
  // given String representation of a QualifiedId
  public void setValue(String value) {
    // bad input check
    if (value == null) { throw new IllegalArgumentException("Arg1(value): null"); }

    while (getChildCount() > 0) {
      removeChild(0);
    }
    String[] valueItems = value.split("\\.");
    for (int i = 0; i < valueItems.length; i++) {
      addChild(new Id(valueItems[i]));
    }
  }

  // Adds Id's to the beginning of this QualifiedId, based on the
  // given String representation of a QualfieidId
  public void addPrefix(String value) {
    // bad input check
    if (value == null) { throw new IllegalArgumentException("Arg1(value): null"); }

    if (value.equals(""))
      return;
    String[] valueItems = value.split("\\.");
    for (int i = valueItems.length - 1; i >= 0; i--) {
      prependChild(new Id(valueItems[i]));
    }
  }

  // Split this QualifiedId in two at the given index.
  // Is truncated just before the given index, and returns a new
  // QualifiedId representing the rest of the ids.
  public QualifiedId split(int index) {
    // bad input check
    if (index < 0) { throw new IllegalArgumentException("Arg1(index): < 0"); }

    int childCount = getChildCount();
    if (index >= childCount)
      return null;
    List<Id> transfers = new LinkedList<Id>();
    for (int i = index; i < childCount; i++) {
      transfers.add((Id)getChild(i));
    }
    while (getChildCount() > index) {
      removeChild(index);
    }
    return new QualifiedId(getRow(), getColumn(), transfers);
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    return Id.getIsNext(parser);
  }

  @Override
  public String getHeaderString() {
    return "value='" + getValue() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 10000000; }

  public String getValue() {
    StringBuilder result = new StringBuilder();
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      result.append(((Id)getChild(i)).getValue());
      if (i < childCount - 1) {
        result.append(".");
      }
    }
    return result.toString();
  }
  public Id getFirst() { return (Id)getChild(0); }
}
