/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.EdgeIterable;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.msalata.ddm.containers.NodeValue;
import org.msalata.ddm.enums.CentralityAlgorithmEnum;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class CentralityAlgorithmManager
{

    static List<Node> getBestNodes(Graph hgraph, Integer size, Boolean isDirected, String selectedAlgorithm)
    {
        List<Node> bestNodes = new ArrayList<Node>();
        Map<Node, Double> results;

        int N = hgraph.getNodeCount();
        double[] centralities = new double[N];
        HashMap<Integer, Node> indicies = new HashMap<Integer, Node>();
        HashMap<Node, Integer> invIndicies = new HashMap<Node, Integer>();
        fillIndiciesMaps(hgraph, centralities, indicies, invIndicies);

        results = calculateAlgorithm(hgraph, isDirected, indicies, invIndicies, centralities, selectedAlgorithm);

        List<NodeValue> nodesValues = new ArrayList<NodeValue>();
        for (Map.Entry<Node, Double> result : results.entrySet())
        {
            nodesValues.add(new NodeValue(result.getKey(), result.getValue()));
        }
        Collections.sort(nodesValues, new Comparator<NodeValue>()
        {

            @Override
            public int compare(NodeValue nodeValue1, NodeValue nodeValue2)
            {
                return nodeValue2.getNodeValue().compareTo(nodeValue1.getNodeValue());
            }
        });

        for (NodeValue nodeValue : nodesValues)
        {
            if (bestNodes.size() < size)
            {
                bestNodes.add(nodeValue.getNode());
            }
        }
        return bestNodes;
    }

    public static void fillIndiciesMaps(Graph hgraph, double[] eigCentralities, HashMap<Integer, Node> indicies, HashMap<Node, Integer> invIndicies)
    {
        if (indicies == null || invIndicies == null)
        {
            return;
        }

        int count = 0;
        for (Node u : hgraph.getNodes())
        {
            indicies.put(count, u);
            invIndicies.put(u, count);
            eigCentralities[count] = 1;
            count++;
        }
    }

    private static Map<Node, Double> calculateAlgorithm(Graph hgraph, Boolean directed, HashMap<Integer, Node> indicies, HashMap<Node, Integer> invIndicies, double[] centralities, String selectedAlgorithm)
    {
        Map<Node, Double> results = null;
        if (selectedAlgorithm.equals(CentralityAlgorithmEnum.Degree.getValue()))
        {
            results = calculateDegreeAlgorithm(hgraph, directed, indicies, invIndicies, centralities);
        } else if (selectedAlgorithm.equals(CentralityAlgorithmEnum.Eigenvector.getValue()))
        {
            results = calculateEigenvectorAlgorithm(hgraph, directed, indicies, invIndicies, centralities);
        } else if (selectedAlgorithm.equals(CentralityAlgorithmEnum.Closeness.getValue()))
        {
            results = calculateClosenessAlgorithm(hgraph, invIndicies, indicies, directed, true);
        } else if (selectedAlgorithm.equals(CentralityAlgorithmEnum.Radius.getValue()))
        {
            results = calculateRadiusAlgorithm(hgraph, invIndicies, indicies, directed, true);
        }
        
        else if (selectedAlgorithm.equals(CentralityAlgorithmEnum.Betweenness.getValue()))
        {
            results = calculateBetweennessAlgorithm(hgraph, invIndicies, indicies, directed, true);
        }

        return results;
    }

    static Map<Node, Double> calculateDegreeAlgorithm(Graph graph, Boolean isDirected, Map<Integer, Node> indicies, Map<Node, Integer> invIndicies, double[] centralities)
    {
        Map<Node, Double> results = new HashMap<Node, Double>();

        double max = 0.;
        int N = graph.getNodeCount();
        double[] tempValues = new double[N];
        for (int i = 0; i < N; i++)
        {
            Node node = indicies.get(i);
            EdgeIterable iter;
            if (isDirected)
            {
                iter = ((DirectedGraph) graph).getInEdges(node);
            } else
            {
                iter = graph.getEdges(node);
            }

            for (Edge e : iter)
            {
                Node v = graph.getOpposite(node, e);
                Integer id = invIndicies.get(v);
                tempValues[i] += centralities[id];
            }
            max = Math.max(max, tempValues[i]);
        }

        for (int k = 0; k < N; k++)
        {
            if (max != 0)
            {
                centralities[k] = tempValues[k] / max;
            }
        }

        for (int index : indicies.keySet())
        {
            results.put(indicies.get(index), centralities[index]);
        }

        return results;
    }

    static Map<Node, Double> calculateEigenvectorAlgorithm(Graph graph, Boolean isDirected, Map<Integer, Node> indicies, Map<Node, Integer> invIndicies, double[] centralities)
    {
        int iterations = 100;
        Map<Node, Double> results = new HashMap<Node, Double>();

        double max = 0.;
        int N = graph.getNodeCount();
        double[] tempValues = new double[N];
        for (int iteration = 0; iteration < iterations; iteration++)
        {

            for (int i = 0; i < N; i++)
            {
                Node node = indicies.get(i);
                EdgeIterable iter;
                if (isDirected)
                {
                    iter = ((DirectedGraph) graph).getInEdges(node);
                } else
                {
                    iter = graph.getEdges(node);
                }

                for (Edge e : iter)
                {
                    Node v = graph.getOpposite(node, e);
                    Integer id = invIndicies.get(v);
                    tempValues[i] += centralities[id];
                }
                max = Math.max(max, tempValues[i]);
            }

            for (int k = 0; k < N; k++)
            {
                if (max != 0)
                {
                    centralities[k] = tempValues[k] / max;
                }
            }

            for (int index : indicies.keySet())
            {
                results.put(indicies.get(index), centralities[index]);
            }

        }

        return results;
    }

    public static Map<Node, Double> calculateClosenessAlgorithm(Graph hgraph, Map<Node, Integer> indicies, Map<Integer, Node> invIndicies, boolean directed, boolean normalized)
    {

        int diameter = 0;
        int shortestPaths = 0;
        int n = hgraph.getNodeCount();
        double avgDist = 0.0;

        double[] nodeCloseness = new double[n];

        int count = 0;

        for (Node s : hgraph.getNodes())
        {
            Stack<Node> S = new Stack<Node>();

            LinkedList<Node>[] P = new LinkedList[n];
            double[] theta = new double[n];
            int[] d = new int[n];

            int s_index = indicies.get(s);

            for (int j = 0; j < n; j++)
            {
                P[j] = new LinkedList<Node>();
                theta[j] = 0;
                d[j] = -1;
            }
            theta[s_index] = 1;
            d[s_index] = 0;

            LinkedList<Node> Q = new LinkedList<Node>();
            Q.addLast(s);
            while (!Q.isEmpty())
            {
                Node v = Q.removeFirst();
                S.push(v);
                int v_index = indicies.get(v);

                EdgeIterable edgeIter;
                if (directed)
                {
                    edgeIter = ((DirectedGraph) hgraph).getOutEdges(v);
                } else
                {
                    edgeIter = hgraph.getEdges(v);
                }

                for (Edge edge : edgeIter)
                {
                    Node reachable = hgraph.getOpposite(v, edge);

                    int r_index = indicies.get(reachable);
                    if (d[r_index] < 0)
                    {
                        Q.addLast(reachable);
                        d[r_index] = d[v_index] + 1;
                    }
                    if (d[r_index] == (d[v_index] + 1))
                    {
                        theta[r_index] = theta[r_index] + theta[v_index];
                        P[r_index].addLast(v);
                    }
                }
            }
            double reachable = 0;
            for (int i = 0; i < n; i++)
            {
                if (d[i] > 0)
                {
                    avgDist += d[i];
                    nodeCloseness[s_index] += d[i];
                    diameter = Math.max(diameter, d[i]);
                    reachable++;
                }
            }

            if (reachable != 0)
            {
                nodeCloseness[s_index] /= reachable;
            }

            shortestPaths += reachable;

            double[] delta = new double[n];
            while (!S.empty())
            {
                Node w = S.pop();
                int w_index = indicies.get(w);
                ListIterator<Node> iter1 = P[w_index].listIterator();
                while (iter1.hasNext())
                {
                    Node u = iter1.next();
                    int u_index = indicies.get(u);
                    delta[u_index] += (theta[u_index] / theta[w_index]) * (1 + delta[w_index]);
                }
            }
            count++;
        }

        avgDist /= shortestPaths;//mN * (mN - 1.0f);

        for (Node s : hgraph.getNodes())
        {

            int s_index = indicies.get(s);

            if (normalized)
            {
                nodeCloseness[s_index] = (nodeCloseness[s_index] == 0) ? 0 : 1.0 / nodeCloseness[s_index];
            }
        }

        Map<Node, Double> values;
        values = new HashMap<Node, Double>();
        for (Node s : hgraph.getNodes())
        {
            int s_index = indicies.get(s);
            values.put(s, nodeCloseness[s_index]);
        }

        return values;
    }

    public static Map<Node, Double> calculateRadiusAlgorithm(Graph hgraph, HashMap<Node, Integer> indicies, HashMap<Integer, Node> invIndicies, boolean directed, boolean normalized)
    {
        int radius = Integer.MAX_VALUE;
        int diameter = 0;
        int shortestPaths = 0;
        double avgDist = 0.0;
        int n = hgraph.getNodeCount();

        HashMap<String, double[]> metrics = new HashMap<String, double[]>();

        double[] nodeEccentricity = new double[n];

        metrics.put("ecc", nodeEccentricity);
        int count = 0;

        for (Node s : hgraph.getNodes())
        {
            Stack<Node> S = new Stack<Node>();

            LinkedList<Node>[] P = new LinkedList[n];
            double[] theta = new double[n];
            int[] d = new int[n];

            int s_index = indicies.get(s);

            for (int j = 0; j < n; j++)
            {
                P[j] = new LinkedList<Node>();
                theta[j] = 0;
                d[j] = -1;
            }
            theta[s_index] = 1;
            d[s_index] = 0;

            LinkedList<Node> Q = new LinkedList<Node>();
            Q.addLast(s);
            while (!Q.isEmpty())
            {
                Node v = Q.removeFirst();
                S.push(v);
                int v_index = indicies.get(v);

                EdgeIterable edgeIter;

                if (directed)
                {
                    edgeIter = ((DirectedGraph) hgraph).getOutEdges(v);
                } else
                {
                    edgeIter = hgraph.getEdges(v);
                }

                for (Edge edge : edgeIter)
                {
                    Node reachable = hgraph.getOpposite(v, edge);

                    int r_index = indicies.get(reachable);
                    if (d[r_index] < 0)
                    {
                        Q.addLast(reachable);
                        d[r_index] = d[v_index] + 1;
                    }
                    if (d[r_index] == (d[v_index] + 1))
                    {
                        theta[r_index] = theta[r_index] + theta[v_index];
                        P[r_index].addLast(v);
                    }
                }
            }
            double reachable = 0;
            for (int i = 0; i < n; i++)
            {
                if (d[i] > 0)
                {
                    avgDist += d[i];
                    nodeEccentricity[s_index] = (int) Math.max(nodeEccentricity[s_index], d[i]);
                    diameter = Math.max(diameter, d[i]);
                    reachable++;
                }
            }

            radius = (int) Math.min(nodeEccentricity[s_index], radius);
            shortestPaths += reachable;

            count++;
        }

        avgDist /= shortestPaths;//mN * (mN - 1.0f);

        Map<Node, Double> values;
        values = new HashMap<Node, Double>();
        for (Node s : hgraph.getNodes())
        {
            int s_index = indicies.get(s);
            values.put(s, 1 / nodeEccentricity[s_index]);
        }

        return values;
    }

    public static Map<Node, Double> calculateBetweennessAlgorithm(Graph hgraph, HashMap<Node, Integer> indicies, Map<Integer, Node> invIndicies, boolean directed, boolean normalized)
    {
        int n = hgraph.getNodeCount();

        double[] nodeBetweenness = new double[n];
        for (Node s : hgraph.getNodes())
        {
            Stack<Node> S = new Stack<Node>();

            LinkedList<Node>[] P = new LinkedList[n];
            double[] theta = new double[n];
            int[] d = new int[n];

            int s_index = indicies.get(s);

            for (int j = 0; j < n; j++)
            {
                P[j] = new LinkedList<Node>();
                theta[j] = 0;
                d[j] = -1;
            }
            theta[s_index] = 1;
            d[s_index] = 0;

            LinkedList<Node> Q = new LinkedList<Node>();
            Q.addLast(s);
            while (!Q.isEmpty())
            {
                Node v = Q.removeFirst();
                S.push(v);
                int v_index = indicies.get(v);

                EdgeIterable edgeIter;
                if (directed)
                {
                    edgeIter = ((DirectedGraph) hgraph).getOutEdges(v);
                } else
                {
                    edgeIter = hgraph.getEdges(v);
                }

                for (Edge edge : edgeIter)
                {
                    Node reachable = hgraph.getOpposite(v, edge);

                    int r_index = indicies.get(reachable);
                    if (d[r_index] < 0)
                    {
                        Q.addLast(reachable);
                        d[r_index] = d[v_index] + 1;
                    }
                    if (d[r_index] == (d[v_index] + 1))
                    {
                        theta[r_index] = theta[r_index] + theta[v_index];
                        P[r_index].addLast(v);
                    }
                }
            }

            double[] delta = new double[n];
            while (!S.empty())
            {
                Node w = S.pop();
                int w_index = indicies.get(w);
                ListIterator<Node> iter1 = P[w_index].listIterator();
                while (iter1.hasNext())
                {
                    Node u = iter1.next();
                    int u_index = indicies.get(u);
                    delta[u_index] += (theta[u_index] / theta[w_index]) * (1 + delta[w_index]);
                }
                if (w != s)
                {
                    nodeBetweenness[w_index] += delta[w_index];
                }
            }
        }

       

        for (Node s : hgraph.getNodes()) {

            int s_index = indicies.get(s);

            if (!directed) {
                nodeBetweenness[s_index] /= 2;
            }
            if (normalized) {
                nodeBetweenness[s_index] /= directed ? (n - 1) * (n - 2) : (n - 1) * (n - 2) / 2;
            }
        }

        Map<Node, Double> values;
        values = new HashMap<Node, Double>();
        for (Node s : hgraph.getNodes())
        {
            int s_index = indicies.get(s);
            values.put(s, nodeBetweenness[s_index]);
        }

        return values;
    }
}
