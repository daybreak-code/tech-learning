package com.llm.algorithm.list;

import java.util.HashMap;

public class LRUList {

    class Node {
        Node pre;
        Node next;
        int key, value;
        public Node(int key, int val){
            this.key = key;
            this.value = val;
        }
    }

    Node head;
    Node tail;

    int maxSize;
    int currentCacheSize;

    HashMap<Integer, Node> map;

    public LRUList(int capacity) {
        currentCacheSize = 0;
        this.maxSize = capacity;
        map = new HashMap<>();
    }

    public int get(int index){
        if(!map.containsKey(index)){
            return -1;
        }
        transToHead(map.get(index));
        return map.get(index).value;
    }

    public void put(int a, int b){
        Node node = map.get(a);
        if (node == null){
            map.put(a, new Node(a, b));
        }
    }

    public Object remove(int k){
        Node node = map.get(k);
        if (node != null){
            if (node.pre != null){
                node.pre.next = node.next;
            }
            if (node.next != null){
                node.next.pre = node.pre;
            }
            if (node == head){
                head = node.next;
            }
            if (node == tail){
                tail = node.pre;
            }
        }
        return map.remove(k);
    }

    public void transToHead(Node node){
        if (node == head){
            return;
        }
        if (node.next != null){
            node.next.pre = node.pre;
        }
        if (node.pre != null){
            node.pre.next = node.next;
        }
        if (node == tail){
            tail = tail.pre;
        }
        node.next = head;
        head.pre = node;
        head = node;
        head.pre = null;
    }

    public void removeLast(){
        if (tail != null){
            tail = tail.pre;
            if (tail == null){
                head = null;
            } else {
                tail.next = null;
            }
        }
    }
}
