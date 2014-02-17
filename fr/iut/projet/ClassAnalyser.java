package fr.iut.projet;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

public class ClassAnalyser {

	private ICompilationUnit unit;
	private CompilationUnit ast;
	private List<MethodAnalyser> methods;

	public ClassAnalyser(ICompilationUnit unit) {
		this.unit = unit;
		methods = new ArrayList<MethodAnalyser>();
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		ast = (CompilationUnit) parser.createAST(null);
		
	}

	public String getName() {
		return unit.getElementName();
	}

	public int getNbLines() throws JavaModelException {
		Document doc = new Document(unit.getSource());
		return doc.getNumberOfLines();
	}
	
	public CompilationUnit getAST() {
		return ast;
	}

	public List<MethodAnalyser> getMethods() {
		return methods;
	}
}
