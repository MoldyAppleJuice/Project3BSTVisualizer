//---------------------------------------------------------------------------
// BinarySearchTree.java          by Dale/Joyce/Weems               Chapter 7
//
// Defines all constructs for a reference-based BST.
// Supports three traversal orders Preorder, Postorder & Inorder ("natural")
//---------------------------------------------------------------------------

import java.util.*; // Iterator, Comparator

public class BinarySearchTree<T> {
	protected BSTNode<T> root; // reference to the root of this BST
	protected Comparator<T> comp; // used for all comparisons

	protected boolean found; // used by remove

	public BinarySearchTree()
	// Precondition: T implements Comparable
	// Creates an empty BST object - uses the natural order of elements.
	{
		root = null;
		comp = new Comparator<T>() {
			public int compare(T element1, T element2) {
				return ((Comparable) element1).compareTo(element2);
			}
		};
	}

	public BinarySearchTree(Comparator<T> comp)
	// Creates an empty BST object - uses Comparator comp for order
	// of elements.
	{
		root = null;
		this.comp = comp;
	}

	public boolean isFull()
	// Returns false; this link-based BST is never full.
	{
		return false;
	}

	public boolean isEmpty()
	// Returns true if this BST is empty; otherwise, returns false.
	{
		return (root == null);
	}

	public T min()
	// If this BST is empty, returns null;
	// otherwise returns the smallest element of the tree.
	{
		if (isEmpty())
			return null;
		else {
			BSTNode<T> node = root;
			while (node.getLeft() != null)
				node = node.getLeft();
			return node.getInfo();
		}
	}

	public T max()
	// If this BST is empty, returns null;
	// otherwise returns the largest element of the tree.
	{
		if (isEmpty())
			return null;
		else {
			BSTNode<T> node = root;
			while (node.getRight() != null)
				node = node.getRight();
			return node.getInfo();
		}
	}

	public int recSize(BSTNode<T> node)
	// Returns the number of elements in subtree rooted at node.
	{
		if (node == null)
			return 0;
		else
			return 1 + recSize(node.getLeft()) + recSize(node.getRight());
	}

	public int size()
	// Returns the number of elements in this BST.
	{
		return recSize(root);
	}

	public int size2() throws Exception
	// Returns the number of elements in this BST.
	{
		int count = 0;
		if (root != null) {
			LinkedStack<BSTNode<T>> nodeStack = new LinkedStack<BSTNode<T>>();
			BSTNode<T> currNode;
			nodeStack.push(root);
			while (!nodeStack.isEmpty()) {
				currNode = nodeStack.top();
				nodeStack.pop();
				count++;
				if (currNode.getLeft() != null)
					nodeStack.push(currNode.getLeft());
				if (currNode.getRight() != null)
					nodeStack.push(currNode.getRight());
			}
		}
		return count;
	}

	public boolean recContains(T target, BSTNode<T> node)
	// Returns true if the subtree rooted at node contains info i such that
	// comp.compare(target, i) == 0; otherwise, returns false.
	{
		if (node == null)
			return false; // target is not found
		else if (comp.compare(target, node.getInfo()) < 0)
			return recContains(target, node.getLeft()); // Search left subtree
		else if (comp.compare(target, node.getInfo()) > 0)
			return recContains(target, node.getRight()); // Search right subtree
		else
			return true; // target is found
	}

	public boolean contains(T target)
	// Returns true if this BST contains a node with info i such that
	// comp.compare(target, i) == 0; otherwise, returns false.
	{
		return recContains(target, root);
	}

	public T recGet(T target, BSTNode<T> node)
	// Returns info i from the subtree rooted at node such that
	// comp.compare(target, i) == 0; if no such info exists, returns null.
	{
		if (node == null)
			return null; // target is not found
		else if (comp.compare(target, node.getInfo()) < 0)
			return recGet(target, node.getLeft()); // get from left subtree
		else if (comp.compare(target, node.getInfo()) > 0)
			return recGet(target, node.getRight()); // get from right subtree
		else
			return node.getInfo(); // target is found
	}

	public T get(T target)
	// Returns info i from node of this BST where comp.compare(target, i) == 0;
	// if no such node exists, returns null.
	{
		return recGet(target, root);
	}

	public BSTNode<T> recAdd(T element, BSTNode<T> node)
	// Adds element to tree rooted at node; tree retains its BST property.
	{
		if (node == null)
			// Addition place found
			node = new BSTNode<T>(element);
		else if (comp.compare(element, node.getInfo()) <= 0)
			node.setLeft(recAdd(element, node.getLeft())); // Add in left
															// subtree
		else
			node.setRight(recAdd(element, node.getRight())); // Add in right
																// subtree
		return node;
	}

	public boolean add(T element)
	// Adds element to this BST. The tree retains its BST property.
	{
		root = recAdd(element, root);
		return true;
	}

	public T getPredecessor(BSTNode<T> subtree)
	// Returns the information held in the rightmost node of subtree
	{
		BSTNode<T> temp = subtree;
		while (temp.getRight() != null)
			temp = temp.getRight();
		return temp.getInfo();
	}

	public BSTNode<T> removeNode(BSTNode<T> node)
	// Removes the information at node from the tree.
	{
		T data;
		if (node.getLeft() == null)
			return node.getRight();
		else if (node.getRight() == null)
			return node.getLeft();
		else {
			data = getPredecessor(node.getLeft());
			node.setInfo(data);
			node.setLeft(recRemove(data, node.getLeft()));
			return node;
		}
	}

	public BSTNode<T> recRemove(T target, BSTNode<T> node)
	// Removes element with info i from tree rooted at node such that
	// comp.compare(target, i) == 0 and returns true;
	// if no such node exists, returns false.
	{
		if (node == null)
			found = false;
		else if (comp.compare(target, node.getInfo()) < 0)
			node.setLeft(recRemove(target, node.getLeft()));
		else if (comp.compare(target, node.getInfo()) > 0)
			node.setRight(recRemove(target, node.getRight()));
		else {
			node = removeNode(node);
			found = true;
		}
		return node;
	}

	public boolean remove(T target)
	// Removes a node with info i from tree such that comp.compare(target,i) ==
	// 0
	// and returns true; if no such node exists, returns false.
	{
		root = recRemove(target, root);
		return found;
	}

	public void preOrder(BSTNode<T> node, LinkedQueue<T> q)
	// Enqueues the elements from the subtree rooted at node into q in preOrder.
	{
		if (node != null) {
			q.enqueue(node.getInfo());
			preOrder(node.getLeft(), q);
			preOrder(node.getRight(), q);
		}
	}

	public void inOrder(BSTNode<T> node, LinkedQueue<T> q)
	// Enqueues the elements from the subtree rooted at node into q in inOrder.
	{
		if (node != null) {
			inOrder(node.getLeft(), q);
			q.enqueue(node.getInfo());
			inOrder(node.getRight(), q);
		}
	}

	public void postOrder(BSTNode<T> node, LinkedQueue<T> q)
	{
		if (node != null) {
			postOrder(node.getLeft(), q);
			postOrder(node.getRight(), q);
			q.enqueue(node.getInfo());
		}
	}
	
	public int[] distanceFromCenter(T target, int offset) {
		int distance = offset, side = 0;
		if (comp.compare(target, root.getInfo()) < 0) side = -1;
		else side = 1;

		BSTNode<T> node = root;
		while (comp.compare(target, node.getInfo()) != 0) {
			if (comp.compare(target, node.getInfo()) < 0) {
				node = node.getLeft();
				distance += (-2 * side);
			} else if (comp.compare(target, node.getInfo()) > 0) {
				node = node.getRight();
				distance += (2 * side);
			}
		}
		
		if (distance < 0 ) return distanceFromCenter(target, offset+2);
		
		if (comp.compare(target, root.getInfo()) == 0) distance = 0;
		int[] toReturn = {distance * side, offset};
		return toReturn;
	}
	
	public int getOffset() throws Exception {
		int offset = 0;
		LinkedQueue<T> q = new LinkedQueue<T>();
		preOrder(root, q);
		q.dequeue();
		while (!q.isEmpty()) {
			offset = Math.max(offset, distanceFromCenter(q.dequeue(), offset)[1]);
		}
		return offset;
	}
	
	public int getLeftWidth() throws Exception {
		int offset = getOffset();
		int width = 0;
		LinkedQueue<T> q = new LinkedQueue<T>();
		preOrder(root.getLeft(), q);
		while (!q.isEmpty()) {
			width = Math.min(width, distanceFromCenter(q.dequeue(), offset)[0]);
		}
		return width;
	}

	public BSTNode<T> getLastChild(BSTNode<T> node) {
		if (node == null) return null;
		BSTNode<T> lastChild = null;
		if (node.getLeft() != null) lastChild = node.getLeft();
		if (node.getRight() != null) lastChild = node.getRight();
		return lastChild;
	}

	public String space(int space) {
		String datString = "";
		while (space > 0) {
			datString += " ";
			space--;
		}
		return datString;
	}
	
	public void arrowPos(ArrayList<Integer> pos, BSTNode<T> node, int position, int rep) {
		if (node.getLeft() != null) pos.add(position-2-rep);
		if (node.getRight() != null) pos.add(position+rep);
	}

	public void breadthFirst(BSTNode<T> node, LinkedQueue<BSTNode<T>> q) throws Exception {
		q.enqueue(root);
		int left = Math.abs(getLeftWidth()), offset = getOffset();
		System.out.println(space(left) + root.getInfo());
		BSTNode<T> lastChild = getLastChild(root);
		BSTNode<T> printed = null;
		ArrayList<BSTNode<T>> toPrint = new ArrayList<BSTNode<T>>();
		ArrayList<String> arrows = new ArrayList<String>();
		ArrayList<Integer> pos = new ArrayList<Integer>();
		for (int o=0; o<=offset; o++) {
			arrowPos(pos, root, left+1, o);
		}

		while (!q.isEmpty()) {
			BSTNode<T> current = q.dequeue();
			
			if (current.getLeft() != null) {
				q.enqueue(current.getLeft());
				toPrint.add(current.getLeft());
				arrows.add("/");
				printed = current.getLeft();
			}
			if (current.getRight() != null) {
				q.enqueue(current.getRight());
				toPrint.add(current.getRight());
				arrows.add("\\");
				printed = current.getRight();
			}
			
			if (printed == lastChild || (!toPrint.isEmpty() && lastChild == null)) {
				int leftAdjusted = 0;
				for (int x=0; x<pos.size(); x++) {
					System.out.print(space(pos.get(x)-leftAdjusted) + arrows.get(x%arrows.size()));
					leftAdjusted = pos.get(x) + 1;
					if (x%arrows.size() == 1 && pos.size() > arrows.size()) {
						leftAdjusted = 0;
						System.out.println();
					}
				}
				if (pos.size() == arrows.size()) System.out.println();
				arrows.clear();
				pos.clear();

				leftAdjusted = 0;
				for (int i = 0; i < toPrint.size(); i++) {
					int distance = distanceFromCenter(toPrint.get(i).getInfo(), offset)[0] + left;
					System.out.print(space(distance - leftAdjusted) + toPrint.get(i).getInfo());
					arrowPos(pos, toPrint.get(i), distance + 1, 0);
					leftAdjusted = distance + 1;
				}
				System.out.println();
				toPrint.clear();
				lastChild = getLastChild(lastChild);
			}
		}
	}

	public void printTree() throws Exception {
		System.out.println("PRINT TREE");
		LinkedQueue<BSTNode<T>> nodes = new LinkedQueue<BSTNode<T>>();
		breadthFirst(root, nodes);
	}
}