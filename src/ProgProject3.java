
public class ProgProject3 {
	public static void main(String[] args) throws Exception {
		String[] inputs = {
				"DBACGHJK",
				"DACBEFMLGHJK",
				"JABCDEFISRQPON",
				"NYUEMRACOPTB",
				"PROFESMDYLAGIVHUN",
				"GAQPEDCBMNTVX"
		};
		for (int k=0; k<inputs.length;k++) {
		BinarySearchTree<Visitor> mytree = new BinarySearchTree<Visitor>();
		for (int i =0 ; i< inputs[k].length(); i++) {
			Visitor v = new Visitor("C");
			v.vname = inputs[k].substring(i, i+1);
			mytree.add(v);
		}
		System.out.println("\n\n");
		System.out.println("Tree for input: " + inputs[k]);
		mytree.printTree();
		}
		
	}
	 
}
