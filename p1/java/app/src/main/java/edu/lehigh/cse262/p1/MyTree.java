package edu.lehigh.cse262.p1;

import java.util.List;
import java.util.function.Function;
import java.util.Iterator;

/**
 * A binary tree, implemented from scratch
 */
public class MyTree<T extends Comparable<T>> {

    //Declearing the members of BST
    private TreeNode root;
    private int size;

    //Define another class TreeNode contains current value, left node and right node
    private class TreeNode{
        T value;
        TreeNode left;
        TreeNode right;
        TreeNode(T value){
            this.value = value;
            left = right = null;
        }
    }
    
    //Initialize tree
    public MyTree(){
        size = 0;
        root = null;
    }


    /**
     * Insert a value into the tree
     * 
     * @param value The value to insert
     */
    void insert(T value) {
        // [CSE 262] Implement Me!
        // Empty tree
        if(root == null){
            root = new TreeNode(value);
        }
        else{
            TreeNode current = root;
            TreeNode parent = null;
            //Iterate till we hit the end
            while(current != null){
                parent = current;
                //go to left
                if(value.compareTo(current.value) < 0){
                    current = current.left;
                }
                //go to right
                else{
                    current = current.right;
                }
            }
            //Set value
            if(value.compareTo(parent.value) < 0){
                parent.left = new TreeNode(value);
            }
            else{
                parent.right = new TreeNode(value);
            }
        }
        size ++;
    }

    /** Clear the tree */
    void clear() {
        // [CSE 262] Implement Me!
        // Since the whole tree is built on a TreeNode, we just make it to null
        root = null;
        size = 0;
    }

    /**
     * Insert all of the elements from some list `l` into the tree
     *
     * @param l The list of elements to insert into the tree
     */
    void inslist(List<T> l) {
        // [CSE 262] Implement Me!
        // Use iterator to add all element to the BST
        Iterator<T> it = l.iterator();
        while(it.hasNext()){
            insert(it.next());
        }
    }

    /**
     * Perform an in-order traversal, applying `func` to every element that is
     * visited
     * 
     * @param func A function to apply to each item
     */
    void inorder(Function<T, T> func){
        //use another recursive method to traverse
        inorder(root, func);
    }
    private void inorder(TreeNode node , Function<T, T> func) {
        TreeNode current = node;
        func.apply(current.value);
        if(current != null){
            inorder(node.left, func);
            System.out.print(node.value + " ");
            inorder(node.right, func);
        }
    }

    /**
     * Perform a pre-order traversal, applying `func` to every element that is
     * visited
     * 
     * @param func A function to apply to each item
     */
    void preorder(Function<T, T> func){
        //use another recursive method to traverse
        preorder(root, func);
    }
    
    private void preorder(TreeNode node ,Function<T, T> func) {
        // [CSE 262] Implement Me!
        TreeNode current = node;
        func.apply(current.value);
        if(current != null){
            System.out.print(node.value + " ");
            preorder(node.left, func);
            preorder(node.right, func);
        }
    }
}