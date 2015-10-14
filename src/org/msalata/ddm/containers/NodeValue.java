/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.containers;

import org.gephi.graph.api.Node;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class NodeValue
{

    private Node node;
    private Double nodeValue;
    
    public NodeValue()
    {
    }
    
    public NodeValue(Node node, Double nodeValue)
    {
        this.node = node;
        this.nodeValue = nodeValue;
    }

    public Node getNode()
    {
        return node;
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    public Double getNodeValue()
    {
        return nodeValue;
    }

    public void setNodeValue(Double nodeValue)
    {
        this.nodeValue = nodeValue;
    }

    @Override
    public String toString()
    {
        return "NodeValue{" + "nodeValue=" + nodeValue + '}';
    }
    
    
}
