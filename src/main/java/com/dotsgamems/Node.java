package com.dotsgamems;

import lombok.Data;

@Data
public class Node {

    int x;
    int y;
    Node next;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
