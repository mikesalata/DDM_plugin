/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import static org.msalata.ddm.DDMStatistic.DDMNAME;
import org.msalata.ddm.containers.CalculationResult;
import org.msalata.ddm.containers.Group;
import org.msalata.ddm.enums.CentralityAlgorithmEnum;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class CalculationManager
{

    public static double findDeviation(List<Double> nums)
    {
        double mean = findMean(nums);

        double squareSum = 0;

        for (Double num : nums)
        {
            squareSum += Math.pow(num - mean, 2);
        }

        return Math.sqrt((squareSum) / (nums.size()));

    }

    public static double findMean(List<Double> nums)
    {

        double sum = 0;

        for (Double num : nums)
        {
            sum += num;
        }

        return sum / nums.size();

    }

    public static List<CalculationResult> getBestCalculations(List<CalculationResult> allCalculationResults)
    {
        List<CalculationResult> results = new ArrayList<CalculationResult>();
        Double bestCalculation = 0.0;
        for (CalculationResult calculationResult : allCalculationResults)
        {
            if (bestCalculation < calculationResult.getResultValue())
            {
                results.clear();
                bestCalculation = calculationResult.getResultValue();
                results.add(calculationResult);
            } else if (bestCalculation.equals(calculationResult.getResultValue()))
            {
                results.add(calculationResult);
            }
        }
        return results;
    }

    public static List<CalculationResult> normalizeCalculations(List<CalculationResult> calculationResultsToNormalize, Double bestResultValue, Double worstResultValue)
    {
        for (CalculationResult calculationResultToNormalize : calculationResultsToNormalize)
        {
            if ((bestResultValue - worstResultValue) != 0)
            {
                calculationResultToNormalize.setResultValue((calculationResultToNormalize.getResultValue() - worstResultValue) / (bestResultValue - worstResultValue));
            } else
            {
                calculationResultToNormalize.setResultValue(1.0);
            }
        }
        return calculationResultsToNormalize;
    }

    public static Double getBestCalculation(List<CalculationResult> allCalculationResults)
    {
        Double bestCalculation = 0.0;
        for (CalculationResult result : allCalculationResults)
        {
            if (result.getResultValue() > bestCalculation)
            {
                bestCalculation = result.getResultValue();
            }
        }
        return bestCalculation;
    }

    public static CalculationResult getBestCalculationResult(List<CalculationResult> allCalculationResults)
    {
        Double bestCalculation = 0.0;
        CalculationResult bestCalculationResult = null;
        for (CalculationResult result : allCalculationResults)
        {
            if (bestCalculationResult == null)
            {
                bestCalculationResult = result;
                bestCalculation = result.getResultValue();
            }
            if (result.getResultValue() > bestCalculation)
            {
                bestCalculation = result.getResultValue();
                bestCalculationResult = result;
            }
        }
        return bestCalculationResult;
    }

    public static Double getWorstCalculation(List<CalculationResult> allCalculationResults)
    {
        Double worstCalculation = null;
        for (CalculationResult result : allCalculationResults)
        {
            if (worstCalculation == null)
            {
                worstCalculation = result.getResultValue();
            } else if (result.getResultValue() < worstCalculation)
            {
                worstCalculation = result.getResultValue();
            }
        }
        return worstCalculation;
    }

    public static HashMap<Integer, Node> createIndexedNodesMap(Graph hgraph)
    {
        HashMap<Integer, Node> indexedNodes = new HashMap<Integer, Node>();
        int index = 0;
        for (Node s : hgraph.getNodes())
        {
            indexedNodes.put(index, s);
            index++;
        }
        return indexedNodes;
    }

    public static HashMap<Integer, Node> createIndexedNodesMapSwitch(Graph hgraph, Integer givenSetSize, Boolean isDirected, String selectedAlgorithm, Integer optimizedSetSize)
    {
        HashMap<Integer, Node> nodesIndiciesed;
        if (selectedAlgorithm.equals(CentralityAlgorithmEnum.None.getValue()))
        {
            nodesIndiciesed = CalculationManager.createIndexedNodesMap(hgraph);
        } else
        {
            nodesIndiciesed = CalculationManager.createOptimizedIndexedNodesMap(hgraph, givenSetSize, isDirected, selectedAlgorithm, optimizedSetSize);
        }
        return nodesIndiciesed;
    }

    public static void saveCalculatedNodesValues(Graph hgraph, CalculationResult bestCalculationResult)
    {
        for (Node node : hgraph.getNodes())
        {
            node.getAttributes().setValue(DDMNAME, 0.0);
            for (Integer calculatedNode : bestCalculationResult.getNodes())
            {
                if (calculatedNode == node.getId())
                {
                    node.getAttributes().setValue(DDMNAME, 1.0);
                }
            }

        }
    }

    public static Map<List<Integer>, Double> calculateDDM(GraphModel graphModel, boolean isDirected, ArrayList<List<Integer>> setList, HashMap<Integer, Node> nodesIndiciesed, ProgressTicket progress)
    {
        Map<List<Integer>, Double> reslutsList = new HashMap<List<Integer>, Double>();
        int setNumber = 1;

        for (List<Integer> nodesToRemoveList : setList)
        {
            List<Integer> nodesToRemoveListNode = new ArrayList<Integer>();
            graphModel.copy();
            GraphView newView = graphModel.newView();     //Duplicate main view
            Graph graphToCalculate = graphModel.getGraph(newView);
            List<Group> groups;
            for (Integer nodeToRemoveIndex : nodesToRemoveList)
            {
                nodesToRemoveListNode.add(nodesIndiciesed.get(nodeToRemoveIndex).getId());
                graphToCalculate.removeNode(nodesIndiciesed.get(nodeToRemoveIndex));
            }
            setNumber++;
            if (isDirected)
            {
                groups = (new SCCAlgorithmManager().calculateGroupsDirected((DirectedGraph) graphToCalculate));
            } else
            {
                groups = (new SCCAlgorithmManager().calculateGroupsUndirected(graphToCalculate));
            }
            List<Double> groupsCountList = new ArrayList<Double>();
            for (Group group : groups)
            {
                groupsCountList.add(new Double(group.getNodesIds().size()));
            }

            Double deviation = CalculationManager.findDeviation(groupsCountList);
            reslutsList.put(nodesToRemoveListNode, ((deviation < 1) ? (2.0 * groups.size()) : ((groups.size() * (1.0) / (deviation)) + groups.size())));
        
            Progress.progress(progress, setNumber);
        
        }
        return reslutsList;
    }

    public static HashMap<Integer, Node> createOptimizedIndexedNodesMap(Graph hgraph, Integer givenSetSize, Boolean isDirected, String selectedAlgorithm, Integer optimizedSetSize)
    {
        List<Node> bestNodes;
        bestNodes = CentralityAlgorithmManager.getBestNodes(hgraph, optimizedSetSize, isDirected, selectedAlgorithm);
        HashMap<Integer, Node> indexedNodes = new HashMap<Integer, Node>();
        int index = 0;
        for (Node s : bestNodes)
        {
            indexedNodes.put(index, s);
            index++;
        }
        return indexedNodes;
    }

}
