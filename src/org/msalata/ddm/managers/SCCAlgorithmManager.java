package org.msalata.ddm.managers;

/**
 *
 * @author Mike <Maciej Sałata>
 */
import java.util.*;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.msalata.ddm.containers.Group;

public class SCCAlgorithmManager
{

    DirectedGraph graph2;
    Map<Node, Boolean> visited2;
    Stack<Node> stack;
    int time;
    Map<Node, Integer> lowLinkSet;
    List<List<Node>> components;

    /**
     * Method calculated stronly connected component using implementation of
     * Tarjan's algorithm
     *
     * @param graph2
     * @return
     */
    public List<Group> calculateGroupsDirected(DirectedGraph graph2)
    {
        this.graph2 = graph2;
        visited2 = new HashMap<Node, Boolean>();
        lowLinkSet = new HashMap<Node, Integer>();
        for (Node node : graph2.getNodes())
        {
            visited2.put(node, false);
            lowLinkSet.put(node, 0);
        }
        stack = new Stack<Node>();
        time = 0;
        components = new ArrayList<List<Node>>();
        for (Node node : graph2.getNodes())
        {
            if (!visited2.get(node))
            {
                dfs(node);
            }
        }

        List<Group> groups = new ArrayList<Group>();
        Integer iterator = 0;
        for (List<Node> component : components)
        {
            groups.add(new Group(iterator.toString(), component));
            iterator++;
        }
        return groups;
    }

    void dfs(Node u)
    {
        time = time + 1;
        lowLinkSet.remove(u);
        lowLinkSet.put(u, time);

        visited2.remove(u);
        visited2.put(u, true);
        stack.add(u);
        boolean isComponentRoot = true;

        for (Node v : graph2.getSuccessors(u))
        {
            if (!visited2.get(v))
            {
                dfs(v);
            }
            if (lowLinkSet.get(u) > lowLinkSet.get(v))
            {
                lowLinkSet.remove(u);
                lowLinkSet.put(u, lowLinkSet.get(v));
                isComponentRoot = false;
            }
        }

        if (isComponentRoot)
        {
            List<Node> component = new ArrayList<Node>();
            while (true)
            {
                Node x = stack.pop();
                component.add(x);
                lowLinkSet.remove(x);
                lowLinkSet.put(x, Integer.MAX_VALUE);
                if (x == u)
                {
                    break;
                }
            }
            components.add(component);
        }
    }

    /**
     *
     * Stack implementation for calculating connected components based on depth
     * first search
     *
     * @param hgraph
     * @return
     */
    public List<Group> calculateGroupsUndirected(Graph hgraph)
    {
        int groupNumber = 0;
        Stack<Node> stack = new Stack<Node>();
        Map<Node, Integer> nodeGroups = new HashMap<Node, Integer>();

        for (Node node : hgraph.getNodes())
        {
            if (nodeGroups.get(node) == null)
            {
                groupNumber++;
                stack.push(node);
                nodeGroups.put(node, groupNumber);
                while (!stack.empty())
                {
                    for (Node neighbour : hgraph.getNeighbors(stack.pop()))
                    {
                        if (nodeGroups.get(neighbour) == null)
                        {
                            stack.push(neighbour);
                            nodeGroups.put(neighbour, groupNumber);
                        }
                    }
                }
            }
        }
        Integer i = 1;
        List<Group> groups = new ArrayList<Group>();
        while (i <= groupNumber)
        {
            Group group = new Group(i.toString(), i);

            for (Node nodeToAdd : nodeGroups.keySet())
            {
                if (group.getId().equals(nodeGroups.get(nodeToAdd)))
                {
                    group.addNode(nodeToAdd.getId());
                }
            }
            groups.add(group);
            i++;
        }
        return groups;
    }
}
