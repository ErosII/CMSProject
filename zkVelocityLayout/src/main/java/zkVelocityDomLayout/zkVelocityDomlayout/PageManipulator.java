package zkVelocityDomLayout.zkVelocityDomlayout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class PageManipulator {

	private boolean debug;
	private final File mainPage;		//the reference to the mainPage is fixed, but the internal content changes!
	private final Document docTree;		//the reference to the docTree is fixed, but the internal content changes!
	
	//CONSTRUCTORS
	
	public PageManipulator(File mainPage, boolean indentation) throws IOException {
		debug = false;
		
		this.mainPage = mainPage;
		docTree = Jsoup.parse(mainPage, null);
		
		if(indentation) {
			docTree.outputSettings().prettyPrint(true).indentAmount(4);	//sets output indentation
		}
	}
	
	public PageManipulator(File mainPage, boolean indentation, boolean debug) throws IOException {
		this(mainPage, indentation);
		this.debug = debug;
	}
	
	//DOM MANIPULATION PUBLIC METHODS
	
	public void addFragment(String fragmentHtml, String fragmentParentId, int fragmentPosition) throws IOException {
		
		Element fragmentTree = getFragmentTreeFromHtml(fragmentHtml);
		addFragment(fragmentTree, fragmentParentId, fragmentPosition);
		saveTreeToFile();
	}
	
	public void removeFragment(String fragmentId) throws IOException {
		
		removeFragment(docTree.getElementById(fragmentId));
		saveTreeToFile();
	}
	
	public void moveFragment(String fragmentId, String newParentId, int newFragmentPosition) throws IOException {
		
		moveFragment(docTree.getElementById(fragmentId), newParentId, newFragmentPosition);
		saveTreeToFile();
	}
	
	public void updateFragment(String newFragmentHtml, String oldFragmentId) throws IOException {
		
		Element newFragmentTree = getFragmentTreeFromHtml(newFragmentHtml);
		updateFragment(newFragmentTree, oldFragmentId);
		saveTreeToFile();
	}
	
	//UTILITIES IN/OUT
	
	private Element getFragmentTreeFromFile(File fragmentInputFile) throws IOException {
		
		String fragmentHtml = FileUtils.readFileToString(fragmentInputFile, "UTF-8");
		Element fragmentTree = getFragmentTreeFromHtml(fragmentHtml);
		return fragmentTree;
	}
	
	//Jsoup automatically wraps a complete html tree around a fragment when parseBodyFragment(), so I had to strip it with this workaround
	private Element getFragmentTreeFromHtml(String fragmentHtml) {
			return Jsoup.parseBodyFragment(fragmentHtml).body().child(0);	//TODO this is just a stub (?)
	}
	
	private void saveTreeToFile() throws IOException {
		FileUtils.writeStringToFile(mainPage, docTree.html(), "UTF-8"); 
		if (debug) {
			System.out.println("\n**** RE-GENERATED PAGE: " + mainPage);
			System.out.println(docTree.html());
			System.out.println("****");
		}
	}
	
	//DOM MANIPULATION
	
	private void addFragment(Element fragmentTree, String fragmentParentId, int fragmentPosition) throws IOException {
		
		if (debug) {
			System.out.println("\n+IN TREE (add)+\n");
			printNodeRecursive(docTree);
			
			System.out.println("\n+FRAGMENT TREE (add)+\n");
			printNodeRecursive(fragmentTree);
		}
		
		Element fragmentParent = docTree.getElementById(fragmentParentId);
		int siblingsSize = fragmentParent.children().size();
		if( siblingsSize == 0 || fragmentPosition > siblingsSize-1) {
			fragmentTree.appendTo(fragmentParent);
		} else {
			//NOTE: do NOT use "fragmentParent.insertChildren(fragmentPosition, fragmentTree)" because it considers all child Nodes and not only child Elements
			//NOTE: do NOT use "fragmentParent.children().add(fragmentPosition, fragmentTree)" because children() gives a COPY
			Element fragmentPreviousSibling = fragmentParent.child(fragmentPosition);	
			fragmentPreviousSibling.before(fragmentTree);
		}
		if (debug) {
			System.out.println("\n+OUT TREE (add)+\n");
			printNodeRecursive(docTree);
		}
	
	}
	
	private void removeFragment(Element fragmentTreeToRemove) throws IOException {
		if (debug) {
			System.out.println("\n+IN TREE (remove)+\n");
			printNodeRecursive(docTree);
		}
		
		fragmentTreeToRemove.remove();
		if (debug) {
			System.out.println("\n+OUT TREE (remove)+\n");
			printNodeRecursive(docTree);
		}
	}
	
	private void moveFragment(Element fragmentTreeToMove, String newParentId, int newFragmentPosition) throws IOException {
		if (debug) {
			System.out.println("\n+IN TREE (move)+\n");
			printNodeRecursive(docTree);
		}
		
		removeFragment(fragmentTreeToMove);
		addFragment(fragmentTreeToMove, newParentId, newFragmentPosition);
		
		if (debug) {
			System.out.println("\n+OUT TREE (move)+\n");
			printNodeRecursive(docTree);
		}
		
	}
	
	private void updateFragment(Element newFragmentTree, String oldFragmentId) throws IOException {
		if (debug) {
			System.out.println("\n+IN TREE (update)+\n");
			printNodeRecursive(docTree);
			
			System.out.println("\n+FRAGMENT TREE (update)+\n");
			printNodeRecursive(newFragmentTree);
		}
		
		Element oldFragmentTree = docTree.getElementById(oldFragmentId);
		oldFragmentTree.replaceWith(newFragmentTree);
		if (debug) {
			System.out.println("\n+OUT TREE (update)+\n");
			printNodeRecursive(docTree);
		}
	}
	
	//DEBUGGING UTILITIES
		
	private void printNodeRecursive(Node n) {
		printNodeRecursive(n,"");
	}
	
	private void printNodeRecursive(Node n, String space) {
			
			if (n == null) return;
			
			else if (n instanceof Element) {
				System.out.println( space + n.nodeName() + " (" + n.attributes() + " )");
			}
	
			else if (n instanceof TextNode) {
				if (! ((TextNode)n).isBlank())	//is needed  otherwise spaces and newlines (by the indentation) would be considered
					System.out.println( space + n.nodeName() + " : " + n );
			}
			
			else {
				System.out.println( space + n.nodeName());
			}
			
			List<Node> children = n.childNodes();
			for(int i=0; i<children.size(); i++)
					printNodeRecursive(children.get(i), space + " - ");		
	}

}

