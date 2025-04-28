package com.llm.algorithm.course;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DfsAndBfs {

    public void dfs(List<List<Integer>> graph, int node, boolean[] visited){
        visited[node] = true;
        for (int neighbor : graph.get(node)){
            if (!visited[neighbor]){
                dfs(graph, neighbor, visited);
            }
        }
    }

    public void bfs(List<List<Integer>> graph, int start){
        boolean[] visited = new boolean[graph.size()];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()){
            int node = queue.poll();
            for (int neighbor: graph.get(node)){
                if (!visited[neighbor]){
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
    }
}
