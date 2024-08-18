package com.llm.algorithm.list;

public class ReverseList {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    //反转整个链表
    static ListNode reverseNode(ListNode root){
        ListNode prev = null;
        ListNode curr = root;

        while (curr != null){
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    static ListNode successor = null;

    static ListNode reverseNNode(ListNode root, int n){
        if (n == 1){
            successor = root.next;
            return root;
        }
        ListNode last = reverseNNode(root, n - 1);
        root.next.next = root;
        root.next = successor;
        return last;
    }

    static ListNode reverseNodeBetween(ListNode root, int m, int n){
        if (m == 1){
            return reverseNNode(root, n);
        }
        root.next = reverseNodeBetween(root, m - 1, n - 1);
        return root;
    }

    public static void main(String[] args) {
        // 创建链表 1->2->3->4->5
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        ListNode reversedHead = reverseNode(head);

        // 打印反转后的链表
        ListNode curr = reversedHead;
        while (curr != null) {
            System.out.print(curr.val + " ");
            curr = curr.next;
        }
    }

}
