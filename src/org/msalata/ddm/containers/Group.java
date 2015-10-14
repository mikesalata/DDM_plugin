/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.containers;

import java.util.ArrayList;
import java.util.List;
import org.gephi.graph.api.Node;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class Group
{

    private String groupName;
    private List<Integer> nodesIds;
    private Integer id;

    public Boolean contains(Node node)
    {
        return true;
    }

//    public Group2(String groupName, List<Integer> nodesIds)
//    {
//        this.groupName = groupName;
//        this.nodesIds = nodesIds;
//    }
    
    public Group(String groupName, Integer id)
    {
        this.id = id;
        this.groupName = groupName;
        this.nodesIds = new ArrayList<Integer>();
    }
    
    public Group(String groupName, Integer nodeId, List<Integer> nodesIds)
    {
        this.groupName = groupName;
        this.nodesIds = nodesIds;
        if(nodeId != null)
        {
            this.nodesIds.add(nodeId);
        }
    }
    
    public Group(String groupName, List<Node> nodes)
    {
        this.groupName = groupName;
        this.nodesIds = new ArrayList<Integer>();
        if(nodes != null)
        {
            for (Node node : nodes)
            {
                this.nodesIds.add(node.getId());
            }
        }
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public List<Integer> getNodesIds()
    {
        return nodesIds;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setNodes(List<Integer> nodesIds)
    {
        this.nodesIds = nodesIds;
    }

    public void addNode(Integer nodeIdToAdd)
    {
        this.nodesIds.add(nodeIdToAdd);
    }
}
