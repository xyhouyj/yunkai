package com.hyj.algorithm.bat;

/**
 * Created by houyunjuan on 2018/6/13.
 */
public class BinaryTree {

    private TreeNode root;

    private TreeNode node;

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getNode() {
        return node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    public BinaryTree(TreeNode node) {
        this.node = node;
    }
}
