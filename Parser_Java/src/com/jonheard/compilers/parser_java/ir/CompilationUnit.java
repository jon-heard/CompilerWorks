
package com.jonheard.compilers.parser_java.ir;

import java.util.List;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.util.Logger;

public class CompilationUnit extends BaseIrType {
  public CompilationUnit(Parser parser) {
    super(parser);
    if (Package.getIsNext(parser)) {
      addChild(new Package(parser));
      hasDefaultPackage = false;
    }
    while (Import.getIsNext(parser)) {
      addChild(new Import(parser));
      importCount++;
    }
    while (!parser.getTokenQueue().isEmpty()) {
      if (Class.getIsNext(parser)) {
        addChild(new Class(parser));
      } else if (Interface.getIsNext(parser)) {
        addChild(new Interface(parser));
      } else if (Enum.getIsNext(parser)) {
        addChild(new Enum(parser));
      } else {
        Token next = parser.getTokenQueue().poll();
        Logger.error(
            "class, interface or enum expected",
            parser.getSource(), next.getRow(), next.getLength());
        Logger.error("class, interface or enum expected", parser.getSource().getFilename(),
            next.getRow(), next.getColumn(), parser.getSource().getRowText(next.getRow()));
        --declarationCount;
      }
      ++declarationCount;
    }
  }

  @Override
  public String getHeaderString() {
    return
        "hasDefaultPackage='" + hasDefaultPackage + "' " +
        "importCount='" + getImportCount() + "' " +
        "declarationCount='" + getDeclarationCount() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 0; }

  public Package getPackage() {
    if (hasDefaultPackage) {
      return null;
    } else {
      return (Package) getChild(0);
    }
  }
  public List<Import> getImports() {
    int index = 0;
    if (hasDefaultPackage) { ++index; }
    return getChildren(index, index + importCount);
  }
  public List<TypeDeclaration> getDeclarations() {
    int index = 0;
    if (hasDefaultPackage) { ++index; }
    index += importCount;
    return getChildren(index, index + declarationCount);
  }
  public int getImportCount() { return importCount; }
  public int getDeclarationCount() { return declarationCount; }

  private int importCount = 0;
  private int declarationCount = 0;
  private boolean hasDefaultPackage = true;
}
