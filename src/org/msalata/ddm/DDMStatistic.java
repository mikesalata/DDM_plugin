/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm;

import org.msalata.ddm.containers.CalculationResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeOrigin;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.data.attributes.api.AttributeType;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.statistics.spi.Statistics;
import org.gephi.utils.longtask.spi.LongTask;
import org.gephi.utils.progress.Progress;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.Lookup;
import org.msalata.ddm.managers.CalculationManager;
import org.msalata.ddm.managers.CombinationManager;
import org.msalata.ddm.managers.ReportManager;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class DDMStatistic implements Statistics, LongTask
{

    public static final String DDMNAME = "Dispersal degree measure name";
    private boolean cancel = false;
    private List<CalculationResult> allCalculationResults;
    private List<CalculationResult> bestCalculationResults;
    private boolean isDirected;
    private ProgressTicket progress;
    private boolean isCanceled;
    private boolean isNormalized;
    private boolean isChart;
    private String setSizeField;
    private Integer givenSetSize = null;
    private Boolean isGivenSetSizeValid = true;
    private Boolean isGivenOptimizedSetSizeValid = true;
    private String selectedAlgorithm = null;
    private String optimizedSetSizeField;
    private Integer optimizedSetSize = null;
    private Graph hGraph = null;

    public DDMStatistic()
    {
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        if (graphController != null && graphController.getModel() != null)
        {
            isDirected = graphController.getModel().isDirected();
        }
    }

    public void execute(Graph hgraph, AttributeModel attributeModel, GraphModel graphModel)
    {
        hGraph = hgraph;
        hgraph.readUnlockAll();
        isCanceled = false;
        try
        {
            givenSetSize = Integer.parseInt(setSizeField);
            if (givenSetSize > hgraph.getNodeCount())
            {
                givenSetSize = 1;
                isGivenSetSizeValid = false;
            }
        } catch (Exception e)
        {
            givenSetSize = 1;
            isGivenSetSizeValid = false;
        }

        try
        {
            optimizedSetSize = Integer.parseInt(optimizedSetSizeField);
            if (optimizedSetSize > hgraph.getNodeCount())
            {
                optimizedSetSize = hgraph.getNodeCount();
                isGivenOptimizedSetSizeValid = false;
            }
        } catch (Exception e)
        {
            optimizedSetSize = hgraph.getNodeCount();
            isGivenOptimizedSetSizeValid = false;
        }

        initializeAttributeColunms(attributeModel);

        HashMap<Integer, Node> nodesIndiciesed;
        nodesIndiciesed = CalculationManager.createIndexedNodesMapSwitch(hgraph, givenSetSize, isDirected, getSelectedAlgorithm(), optimizedSetSize);

//        ArrayList<List<Integer>> setList = CombinationManager.calculateCombinations(givenSetSize, nodesIndiciesed.size());
        ArrayList<List<Integer>> setList = CombinationManager.getCombinations(nodesIndiciesed.size(), givenSetSize);
        Progress.start(progress, setList.size());
        Map<List<Integer>, Double> reslutsList = CalculationManager.calculateDDM(graphModel, isDirected, setList, nodesIndiciesed, progress);
        allCalculationResults = new ArrayList<CalculationResult>();
        Integer id = 1;
        for (List<Integer> setList1 : reslutsList.keySet())
        {
            allCalculationResults.add(new CalculationResult(reslutsList.get(setList1), setList1, id));
            id++;
        }

        if (isNormalized == true)
        {
            Double bestResultValue = CalculationManager.getBestCalculation(allCalculationResults);
            Double worstResultValue = CalculationManager.getWorstCalculation(allCalculationResults);
            CalculationManager.normalizeCalculations(allCalculationResults, bestResultValue, worstResultValue);
        }

        bestCalculationResults = CalculationManager.getBestCalculations(allCalculationResults);
        CalculationManager.saveCalculatedNodesValues(hgraph, CalculationManager.getBestCalculationResult(allCalculationResults));
    }

    @Override
    public void execute(GraphModel graphModel, AttributeModel attributeModel)
    {
        Graph graph;
        if (isDirected)
        {
            graph = graphModel.getDirectedGraphVisible();
        } else
        {
            graph = graphModel.getUndirectedGraphVisible();
        }
        execute(graph, attributeModel, graphModel);
    }

    @Override
    public String getReport()
    {
        return ReportManager.buildReport(this);
    }

    @Override
    public boolean cancel()
    {
        this.cancel = true;
        return true;
    }

    @Override
    public void setProgressTicket(ProgressTicket pt)
    {
        this.progress = pt;
    }

    private void initializeAttributeColunms(AttributeModel attributeModel)
    {
        AttributeTable nodeTable = attributeModel.getNodeTable();
        if (!nodeTable.hasColumn(DDMNAME))
        {
            nodeTable.addColumn(DDMNAME, AttributeType.DOUBLE, AttributeOrigin.DATA);
        }
    }

    public void setNormalized(boolean isNormalized)
    {
        this.isNormalized = isNormalized;
    }

    public boolean isNormalized()
    {
        return isNormalized;
    }

    public void setDirected(boolean isDirected)
    {
        this.isDirected = isDirected;
    }

    public boolean isDirected()
    {
        return isDirected;
    }

    public boolean isIsChart()
    {
        return isChart;
    }

    public void setIsChart(boolean isChart)
    {
        this.isChart = isChart;
    }

    public Integer getGivenSetSize()
    {
        return givenSetSize;
    }

    public void setGivenSetSize(Integer givenSetSize)
    {
        this.givenSetSize = givenSetSize;
    }

    public String getSetSizeField()
    {
        return setSizeField;
    }

    public void setSetSizeField(String setSizeField)
    {
        this.setSizeField = setSizeField;
    }

    public boolean isCancel()
    {
        return cancel;
    }

    public void setCancel(boolean cancel)
    {
        this.cancel = cancel;
    }

    public List<CalculationResult> getAllCalculationResults()
    {
        return allCalculationResults;
    }

    public void setAllCalculationResults(List<CalculationResult> allCalculationResults)
    {
        this.allCalculationResults = allCalculationResults;
    }

    public List<CalculationResult> getBestCalculationResults()
    {
        return bestCalculationResults;
    }

    public void setBestCalculationResults(List<CalculationResult> bestCalculationResults)
    {
        this.bestCalculationResults = bestCalculationResults;
    }

    public boolean isIsDirected()
    {
        return isDirected;
    }

    public void setIsDirected(boolean isDirected)
    {
        this.isDirected = isDirected;
    }

    public ProgressTicket getProgress()
    {
        return progress;
    }

    public void setProgress(ProgressTicket progress)
    {
        this.progress = progress;
    }

    public boolean isIsCanceled()
    {
        return isCanceled;
    }

    public void setIsCanceled(boolean isCanceled)
    {
        this.isCanceled = isCanceled;
    }

    public boolean isIsNormalized()
    {
        return isNormalized;
    }

    public void setIsNormalized(boolean isNormalized)
    {
        this.isNormalized = isNormalized;
    }

    public Integer getGivenTestSize()
    {
        return givenSetSize;
    }

    public void setGivenTestSize(Integer givenTestSize)
    {
        this.givenSetSize = givenTestSize;
    }

    public Boolean getIsGivenTestSizeValid()
    {
        return isGivenSetSizeValid;
    }

    public void setIsGivenTestSizeValid(Boolean isGivenTestSizeValid)
    {
        this.isGivenSetSizeValid = isGivenTestSizeValid;
    }

    public String getSelectedAlgorithm()
    {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(String selectedAlgorithm)
    {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public void setOptimizedSetSizeField(String optimizedSetSizeField)
    {
        this.optimizedSetSizeField = optimizedSetSizeField;
    }

    public Boolean getIsGivenSetSizeValid()
    {
        return isGivenSetSizeValid;
    }

    public void setIsGivenSetSizeValid(Boolean isGivenSetSizeValid)
    {
        this.isGivenSetSizeValid = isGivenSetSizeValid;
    }

    public Boolean getIsGivenOptimizedSetSizeValid()
    {
        return isGivenOptimizedSetSizeValid;
    }

    public void setIsGivenOptimizedSetSizeValid(Boolean isGivenOptimizedSetSizeValid)
    {
        this.isGivenOptimizedSetSizeValid = isGivenOptimizedSetSizeValid;
    }

    public Integer getOptimizedSetSize()
    {
        return optimizedSetSize;
    }

    public void setOptimizedSetSize(Integer optimizedSetSize)
    {
        this.optimizedSetSize = optimizedSetSize;
    }
    
    public Graph gethGraph()
    {
        return hGraph;
    }

    public void sethGraph(Graph hGraph)
    {
        this.hGraph = hGraph;
    }
}
