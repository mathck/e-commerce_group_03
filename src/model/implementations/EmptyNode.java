package model.implementations;

import model.interfaces.INode;

/**
 * This is a node with no data center
 */

public class EmptyNode implements INode {

    @Override
    public String toString() {
        return "0";
    }
}
