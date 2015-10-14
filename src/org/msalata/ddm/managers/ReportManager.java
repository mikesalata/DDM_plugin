/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gephi.statistics.plugin.ChartUtils;
import org.gephi.utils.TempDirUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.openide.util.Exceptions;
import org.msalata.ddm.DDMStatistic;
import org.msalata.ddm.containers.CalculationResult;
import org.msalata.ddm.enums.CentralityAlgorithmEnum;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class ReportManager
{

    public static String buildReport(DDMStatistic ddmStatistic)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<HTML> <BODY> <h1>Dispersal degree measure Measure Report </h1> ");
        sb.append("<hr>");
        sb.append("<br>");
        sb.append("<h2> Parameters: </h2>");
        sb.append("Network Interpretation:  ").append(ddmStatistic.isDirected() ? "directed" : "undirected").append("<br />");
        if (ddmStatistic.getIsGivenTestSizeValid())
        {
            sb.append("Given size of the set:  ").append(ddmStatistic.getGivenTestSize()).append("<br />");
        } else
        {
            sb.append("Given size of the set was invalid. Used default: ").append(ddmStatistic.getGivenTestSize()).append("<br />");
        }

        if (ddmStatistic.getSelectedAlgorithm().equals(CentralityAlgorithmEnum.None.getValue()))
        {
            sb.append("Has not not been selected any optimization algorithm.").append("<br />");
        } else
        {
            sb.append("Selected optimization algorithm: ").append(ddmStatistic.getSelectedAlgorithm()).append("<br />");
        }

        if (ddmStatistic.getIsGivenOptimizedSetSizeValid() && (!ddmStatistic.getSelectedAlgorithm().equals(CentralityAlgorithmEnum.None.getValue())))
        {
            sb.append("Given optimized size of the set:  ").append(ddmStatistic.getOptimizedSetSize()).append("<br />");
        } else if ((!ddmStatistic.getSelectedAlgorithm().equals(CentralityAlgorithmEnum.None.getValue())))
        {
            sb.append("Given optimized size of the set was invalid. Used default: ").append(ddmStatistic.getOptimizedSetSize()).append("<br />");
        }

        sb.append("<br /> <h2> Results: </h2>");

        if (ddmStatistic.getBestCalculationResults().isEmpty())
        {
            sb.append("No best node set in graph!");
        } else if (ddmStatistic.getBestCalculationResults().size() == 1)
        {
            String label = ddmStatistic.getBestCalculationResults().get(0).getNodesLabels(ddmStatistic.gethGraph());
            sb.append("Best node set is: <br />").append(label).append(" with dispersal degree measure equals: ").append(ddmStatistic.getBestCalculationResults().get(0).getResultValue());
        } else
        {
            sb.append("Best nodes sets are: <br />");

            for (CalculationResult calculationResult : ddmStatistic.getBestCalculationResults())
            {
                String label = calculationResult.getNodesLabels(ddmStatistic.gethGraph());
                sb.append(label).append(" with dispersal degree measure equals: ").append(calculationResult.getResultValue());
                sb.append("<br />");
            }
        }

        sb.append("<br /><br />");
        String htmlIMG1 = "";
        try
        {
            TempDirUtils.TempDir tempDir = TempDirUtils.createTempDir();
            htmlIMG1 = createImageFile(tempDir, ddmStatistic.getAllCalculationResults(), "Dispersal degree Distribution", "Set id", "Dispersal degree Measure value");
        } catch (IOException ex)
        {
            Exceptions.printStackTrace(ex);
        }
        if (ddmStatistic.isIsChart())
        {
            sb.append(htmlIMG1);
        }
        sb.append("</BODY> </HTML>");

        return sb.toString();
    }

    public static String createImageFile(TempDirUtils.TempDir tempDir, List<CalculationResult> calculationResults, String pName, String pX, String pY)
    {
        //distribution of values
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (CalculationResult calculationResult : calculationResults)
        {
            map.put(calculationResult.getId(), calculationResult.getResultValue());
        }
        //Distribution series

        XYSeries dSeries = ChartUtils.createXYSeries(map, pName);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(dSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                pName,
                pX,
                pY,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false);
        chart.removeLegend();
        ChartUtils.decorateChart(chart);
        ChartUtils.scaleChart(chart, dSeries, false);
        return ChartUtils.renderChart(chart, pName + ".png");
    }

}
