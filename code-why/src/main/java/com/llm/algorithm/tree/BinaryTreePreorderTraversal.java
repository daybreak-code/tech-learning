package com.llm.algorithm.tree;

import java.util.ArrayList;
import java.util.List;

public class BinaryTreePreorderTraversal {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        ArrayList<Integer> results = new ArrayList<>();
        preorderTraversal(root, results);
        return results;
    }

    public void preorderTraversal(TreeNode root, List<Integer> results) {
        if (root == null){
            return;
        }
        preorderTraversal(root.left, results);
        preorderTraversal(root.right, results);
        results.add(root.val);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        BinaryTreePreorderTraversal traversal = new BinaryTreePreorderTraversal();
        List<Integer> result = traversal.preorderTraversal(root);
        System.out.println(result); // 输出 [1, 2, 4, 5, 3]
    }


}
