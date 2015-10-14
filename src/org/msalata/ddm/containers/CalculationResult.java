/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.containers;

import java.util.List;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public final class CalculationResult
{

    private Double resultValue;
    private List<Integer> nodes;
    private Integer id;

    public CalculationResult(Double resultValue, List<Integer> nodes, Integer id)
    {
        this.nodes = nodes;
        this.resultValue = resultValue;
        this.id = id;
    }

    public CalculationResult(Double resultValue, List<Integer> nodes)
    {
        this.nodes = nodes;
        this.resultValue = resultValue;
    }

    public CalculationResult()
    {
    }

    public Double getResultValue()
    {
        return resultValue;
    }

    public void setResultValue(Double resultValue)
    {
        this.resultValue = resultValue;
    }

    public List<Integer> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<Integer> nodes)
    {
        this.nodes = nodes;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "CalculationResult{" + "resultValue=" + resultValue + ", nodes=" + nodes + '}';
    }

    public String getNodesLabels(Graph hGraph)
    {
        String label = "";
        for (Integer node : nodes)
        {
            if (hGraph.getNode(node).getNodeData().getLabel() != null)
            {
                label += hGraph.getNode(node).getNodeData().getLabel() + ";   ";
            } else
            {
                label += "unnamed(id=" + hGraph.getNode(node).getNodeData().getId() + ");   ";
            }
        }
        return label;
    }

    public String getNodesIds()
    {
        String label = "";
        for (Integer node : nodes)
        {
            label += node + ";  ";
        }
        return label;
    }
}
