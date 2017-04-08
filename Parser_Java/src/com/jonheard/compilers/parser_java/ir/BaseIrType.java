package com.jonheard.compilers.parser_java.ir;

import java.util.ArrayList;
import java.util.List;
import com.jonheard.compilers.parser_java.Parser;

// BaseIrType - All intermediate representations extend this class.
public class BaseIrType {
  // Constructors
  public BaseIrType(int row, int column) {
    // bad input check
    if (row < 0) { throw new IllegalArgumentException("Arg1(row): < 0"); }
    if (column < 0) { throw new IllegalArgumentException("Arg2(col): < 0"); }

    this.row = row;
    this.column = column;
  }
  public BaseIrType(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    row = parser.getNextToken().getRow();
    column = parser.getNextToken().getColumn();
  }

  public BaseIrType getChild(int index) {
    // bad input check
    if (index < 0) { throw new IllegalArgumentException("Arg1(index): < 0"); }
    if (index >= children.size()) {
      throw new IllegalArgumentException("Arg1(index): > childCount");
    }

    return children.get(index);
  }

  public void replaceChild(int index, BaseIrType value) {
    // bad input check
    if (index < 0) { throw new IllegalArgumentException("Arg1(index): < 0"); }
    if (index >= children.size()) {
      throw new IllegalArgumentException("Arg1(index): > childCount");
    }

    if (value == children.get(index)) return;
    children.remove(index);
    children.add(index, value);
  }

  @Override
  public String toString() {
    return toString(0);
  }

  public String toString(int tabCount) {
    // bad input check
    if (tabCount < 0) { throw new IllegalArgumentException("Arg1(tabCount): < 0"); }

    StringBuilder result = new StringBuilder();
    String tabs = new String(new char[tabCount]).replace('\0', '	');
    String headerString = getHeaderString();
    if (!headerString.equals("")) { headerString = " " + headerString; }
    headerString = " row='" + getRow() + "'" + headerString;
    if (children.size() <= getFirstPrintedChildIndex()) {
      result.append(tabs + "<" + getIrTypeName() + headerString + "/>\n");
    } else {
      String typeName = getIrTypeName();
      result.append(tabs + "<" + typeName + headerString + ">\n");
      for (int i = getFirstPrintedChildIndex(); i < children.size(); i++) {
        result.append(getChild(i).toString(tabCount + 1));
      }
      result.append(tabs + "</" + typeName + ">\n");
    }
    return result.toString();
  }

  public int getRow() { return row; }
  public int getColumn() { return column; }
  public int getChildCount() { return children.size(); }
  public int getFirstPrintedChildIndex() { return 0; }
  public String getHeaderString() { return ""; }
  public String getIrTypeName() {
    String result = this.getClass().getName();
    result = result.substring(result.lastIndexOf('.') + 1);
    return result;
  }

  protected void prependChild(BaseIrType value) {
    // bad input check
    if (value == null) { throw new IllegalArgumentException("Arg1(value): null"); }

    children.add(0, value);
  }

  protected void addChild(BaseIrType value) {
    // bad input check
    if (value == null) { throw new IllegalArgumentException("Arg1(value): null"); }

    children.add(value);
  }

  protected void removeChild(int index) {
    // bad input check
    if (index < 0) { throw new IllegalArgumentException("Arg1(index): < 0"); }
    if (index >= children.size()) {
      throw new IllegalArgumentException("Arg1(index): > childCount");
    }

    children.remove(index);
  }

  @SuppressWarnings("unchecked")
  protected <T> List<T> getChildren(int startIndex, int endIndex) {
    // bad input check
    if (startIndex < 0) { throw new IllegalArgumentException("Arg1(startIndex): < 0"); }
    if (startIndex >= children.size()) {
      throw new IllegalArgumentException("Arg1(startIndex): > childCount");
    }
    if (endIndex < 0) { throw new IllegalArgumentException("Arg2(endIndex): < 0"); }
    if (endIndex >= children.size()) {
      throw new IllegalArgumentException("Arg2(endIndex): > childCount");
    }

    List<T> result = new ArrayList<T>();
    for (int i = startIndex; i < endIndex; ++i) {
      result.add((T)children.get(i));
    }
    return result;
  }

  private int row, column;
  private List<BaseIrType> children = new ArrayList<BaseIrType>();
}
