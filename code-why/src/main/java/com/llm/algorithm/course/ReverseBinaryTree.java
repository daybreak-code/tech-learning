package com.llm.algorithm.course;

import sun.reflect.generics.tree.Tree;

import java.util.LinkedList;
import java.util.Queue;

public class ReverseBinaryTree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x){val = x;}
    }

    public TreeNode invertTree(TreeNode root){
        if (root == null) return null;
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }

//    public TreeNode invertTree(TreeNode root){
//        if (root == null) return null;
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.add(root);
//        while (!queue.isEmpty()){
//            TreeNode node = queue.poll();
//            TreeNode temp = node.left;
//            node.left = node.right;
//            node.right = temp;
//            if (node.left != null){
//                queue.add(node.left);
//            }
//            if (node.right != null){
//                queue.add(node.right);
//            }
//        }
//        return root;
//    }
}
