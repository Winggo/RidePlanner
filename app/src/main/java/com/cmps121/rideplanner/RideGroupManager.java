package com.cmps121.rideplanner;

public class RideGroupManager {

//import Dictionary.Node;

//-----------------------------------------------------------------------------
//Mitchell Pon
//mjpon
//12B
//The goal of this program is to modify and create BST ADT in the form of linkedlists
//3/15/18
// Binary Search Tree implementation of the Dictionary ADT
//-----------------------------------------------------------------------------

        // Fields for the IntegerList class
        private Node root; // reference to first Node in List
        // private String key; // key in this IntegerList
        // private String value; // etc
        private int numItems; // number of items in this IntegerList
        // private inner Node class

        // constructor for the Dictonary
        public RideGroupManager() {
            root = null;
            // numItems = 1;
            numItems = 0;
        }

        // private inner class that creates the node
        private class Node {
            // int item;
            String key;
            String collegeAff;
            double distance;
            Node left;
            Node right;
            Node next;

            Node(String x, String college, double dist) {
                key = x;
                collegeAff = college;
                distance = dist;
                next = null;
            }
        }


        // findKey() // tis is broken here, dont even
        // returns the Node containing key k in the subtree rooted at R, or returns
        // null if no such Node exists
//	Node findKey(Node R, String k) {
//		if (R == null || R.key.compareTo(k) == 0)
//			return R;
//		if (R.key.compareTo(k) < 0)
//			return findKey(R.left, k);
//		else // strcmp(k, R.key)>0
//			return findKey(R.right, k);
//	}

        // findParent()
        // returns the parent of N in the subtree rooted at R, or returns null
        // if N is equal to R. (pre: R != null)
        public Node findParent(Node N, Node R) {
            Node P = null;
            if (N != R) {
                P = R;
                while (P.left != N && P.right != N) {
                    if (N.key.compareTo(P.key) < 0)
                        P = P.left;
                    else
                        P = P.right;
                }
            }
            return P;
        }

        // findLeftmost()
        // returns the leftmost Node in the subtree rooted at R, or null if R is null.
        public Node findLeftmost(Node R) {
            Node L = R;
            if (L != null)
                for (; L.left != null; L = L.left)
                    ;
            return L;
        }

        // printInOrder()
        // prints the (key, value) pairs belonging to the subtree rooted at R in order
        // of increasing keys to file pointed to by out.
        // void printInOrder(Node R){
        // if( R!=null ){
        // printInOrder(R.left);
        // fprintf("%s %s\n", R.key, R.value);
        // printInOrder(R.right);
        // }
        // }

        // deleteAll()
        // deletes all Nodes in the subtree rooted at N.
        public void deleteAll(Node N) {
            if (N != null) {
                deleteAll(N.left);
                deleteAll(N.right);
                // // freeNode(&N);
            }
        }

        // isEmpty()
        // returns 1 (true) if D is empty, 0 (false) otherwise
        // pre: none
        public boolean isEmpty() {
//		if (root == null) {
//			throw new KeyNotFoundException("Dictionary Error: calling insert() on null Dictionary reference\n");
//		}
            return (numItems == 0);
        }

        // size()
        // returns the number of (key, value) pairs in D
        // pre: none
        public int size() {
            if (root == null) {
                throw new KeyNotFoundException("Dictionary Error: calling insert() on null Dictionary reference\n");
            }
            return (numItems);
        }

        // lookup()
        // returns the value v such that (k, v) is in D, or returns null if no
        // such value v exists.
        // pre: none
        // looks for the node with the key needed
        public String lookup(String key) {
            Node N = findKey(key);
            if(N != null) return N.collegeAff;
            else return null;
        }

        // insert()
        // inserts new (key,value) pair into D
        // pre: lookup(D, k)==null
        public void insert(String k, String v, double d) {
            Node D = root;
            Node N, A, B;

            if (lookup(k) != null) { // if there is the key already in the dictonary
                throw new DuplicateKeyException("\"Dictionary Error: cannot insert() duplicate key");
            }
            N = new Node(k, v, d);
            B = null;
            A = root;
            while (A != null) {
                B = A;
                if (k.compareTo(A.key) < 0)
                    A = A.left;
                else
                    A = A.right;
            }
            if (B == null)
                root = N;
            else if (k.compareTo(D.key) < 0)
                B.left = N;
            else
                B.right = N;
            numItems++;
        }

        // delete()
        // deletes pair with the key k
        // pre: lookup(D, k)!=null
        public void delete(String k) {
            Node D = root;
            Node N, P, S;
            if (D == null) {
                throw new KeyNotFoundException("Dictionary Error: calling insert() on null Dictionary reference\n");
            }

            N = findKey(k);
            if (N.left == null && N.right == null) { // case 1 (no children)
                if (N == root) {
                    root = null;
                    // // freeNode(&N);
                } else {
                    P = findParent(N, root);
                    if (P.right == N)
                        P.right = null;
                    else
                        P.left = null;
                    // // freeNode(&N);
                }
            } else if (N.right == null) { // case 2 (left but no right child)
                if (N == root) {
                    root = N.left;
                    // freeNode(&N);
                } else {
                    P = findParent(N, root);
                    if (P.right == N)
                        P.right = N.left;
                    else
                        P.left = N.left;
                    // freeNode(&N);
                }
            } else if (N.left == null) { // case 2 (right but no left child)
                if (N == root) {
                    root = N.right;
                    // freeNode(&N);
                } else {
                    P = findParent(N, root);
                    if (P.right == N)
                        P.right = N.right;
                    else
                        P.left = N.right;
                    // freeNode(&N);
                }
            } else { // case3: (two children: N.left!=null && N.right!=null)
                S = findLeftmost(N.right);
                N.key = S.key;
                N.collegeAff = S.collegeAff;
                P = findParent(S, N);
                if (P.right == S)
                    P.right = S.right;
                else
                    P.left = S.right;
                // freeNode(&N);
            }
            numItems--;
        }

        // makeEmpty()
        // re-sets D to the empty state.
        // pre: none
        public void makeEmpty() {
            root = null; // points the head to like nowhere killing all of its followers
            numItems = 0;

        }

        // printInOrder()
        // prints the (key, value) pairs belonging to the subtree rooted at R in order
        // of increasing keys to file pointed to by out.
        public void printInOrder(Node R){
            if( R != null ){
                printInOrder(R.left);
                System.out.printf("%s %s\n", R.key, R.collegeAff);
                printInOrder(R.right);
            }
        }

        // printDictionary()
        // pre: none
        // prints a text representation of D to the file pointed to by out
        // changes it to a string format, the node part
        public String toString() {
            printInOrder(root);
            return "";
//		StringBuffer sb = new StringBuffer();
//		Node N = root;
//
//		for (; N != null; N = N.next) {
//			System.out.println("help");
//			sb.append(N.key).append(" ").append(N.value); // basically combines the string stuff together
//			sb.append("\n");
//
//		}
//		return new String(sb);

        }
        //Finding the key?
        //very nice helper function
        private Node findKey(String key) {
            Node N = root;
            //tranversing through the node
            while(N != null) {
                //too lazy to put brackets
                if(N.key == key) return N;
                    // well, if the key doesnt... GO LEFT
                else if(key.compareTo(N.key) < 0) N = N.left;
                    // GO LEFT IF IT DOESNT SEEM BIGGER
                else N = N.right;
            }
            return null;
        }


    }

