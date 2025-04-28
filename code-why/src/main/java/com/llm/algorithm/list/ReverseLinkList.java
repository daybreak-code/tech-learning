package com.llm.algorithm.list;

public class ReverseLinkList {
    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {val = x;}
    }

    ListNode reverse(ListNode head){
        if(head.next == null) return head;
        ListNode last = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return last;
    }
}
