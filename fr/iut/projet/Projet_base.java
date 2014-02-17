
package fr.iut.projet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Une classe exemple de base pour faire un plugin qui utilise JDT
 */
public class Projet_base implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;

	public Projet_base() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		for (IProject proj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			analyzeProject(proj);
		}
	}
	
	
	
	private void analyzeProject(IProject proj) {
		System.out.println("TRACE: analyzeProject(" +proj.getName()+ ")");
		IJavaProject jProj = JavaCore.create(proj);
		try {
			for (IPackageFragment pckg : jProj.getPackageFragments()) {
				// Package fragments include all packages in the classpath
				// We will only look at the package from the source folder
				// K_BINARY would include also included JARS, e.g. rt.jar
				if ( (pckg.getKind() == IPackageFragmentRoot.K_SOURCE) && pckg.containsJavaResources() ) {
					analyzePackage(pckg);
					}
				}
			
		} catch (JavaModelException e) {
			System.err.println("JavaModelException in analyzeProject");
			e.printStackTrace();
		}
	}

	private void analyzePackage(IPackageFragment pckg) {
		System.out.println("TRACE:   analyzePackage(" +pckg.getElementName()+ ")");
		try {
			for (ICompilationUnit unit : pckg.getCompilationUnits()) {
				analyzeUnit(unit);
			}
		} catch (JavaModelException e) {
			System.err.println("JavaModelException in analyzePackage");
			e.printStackTrace();
		}
	}

	private void analyzeUnit(ICompilationUnit unit) {
		System.out.println("TRACE:     analyzeUnit(" +unit.getElementName()+ ")");
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);

		// AST: Abstract Syntax Tree, un arbre qui représente tout le programme
		ASTNode unitAST = parser.createAST(/*IProgressMonitor*/null);

		for (Object c : ((CompilationUnit)unitAST).getCommentList()) {
			Comment cc = (Comment) c;
			System.out.println("----------");
			System.out.println("Comment class:"+cc.getClass());
			System.out.println(cc.toString());
			System.out.println(cc.getAST());
			System.out.println("length="+cc.getLength());
			int pos = cc.getStartPosition();
			System.out.println("start pos="+pos);
			System.out.println("ligne="+((CompilationUnit)unitAST).getLineNumber(pos)+
								" column="+((CompilationUnit)unitAST).getColumnNumber(pos));
			
		}
	}

	
	
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}