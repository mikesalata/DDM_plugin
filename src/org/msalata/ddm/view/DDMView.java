/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.view;

import org.msalata.ddm.DDMPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.project.api.WorkspaceListener;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.msalata.ddm.model.DDMModel;
import org.msalata.ddm.DDMStatistic;
import org.msalata.ddm.controllers.DDMController;

/**
 *
 * @author lukasz
 */
@ServiceProvider(service = StatisticsUI.class)
public class DDMView implements StatisticsUI, PropertyChangeListener
{
    
    private DDMPanel panel;
    private DDMStatistic myMetric;
    
    private DDMModel model;
    
    public DDMView()
    {
        final DDMController DDMController = Lookup.getDefault().lookup(DDMController.class);
        final DDMView me = this;
        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        projectController.addWorkspaceListener(new WorkspaceListener()
        {
            
            @Override
            public void initialize(Workspace workspace)
            {
            }
            
            @Override
            public void select(Workspace workspace)
            {
                model = DDMController.getModel(workspace);
                model.addPropertyChangeListener(me);
                refreshModel();
            }
            
            @Override
            public void unselect(Workspace workspace)
            {
                if (model != null)
                {
                    model.removePropertyChangeListener(me);
                }
            }
            
            @Override
            public void close(Workspace workspace)
            {
            }
            
            @Override
            public void disable()
            {
                model = null;
                refreshModel();
            }
        });
        if (projectController.getCurrentProject() != null)
        {
            Workspace workspace = projectController.getCurrentWorkspace();
            model = DDMController.getModel(workspace);
        }
        refreshModel();
    }
    
    private void refreshModel()
    {
        if (model != null)
        {
            //Enable controls
        } else
        {
            //Model is null, disable controls
        }
    }
    
    @Override
    public String getDisplayName()
    {
        return "Dispersal degree measure statistic";
    }
    
    @Override
    public String getCategory()
    {
        return "Centralities";
    }
    
    @Override
    public int getPosition()
    {
        return 800;
    }
    
    @Override
    public Class<? extends Statistics> getStatisticsClass()
    {
        return DDMStatistic.class;
    }
    
    @Override
    public JPanel getSettingsPanel()
    {
        panel = new DDMPanel();
        return panel;
    }
    
    @Override
    public void setup(Statistics statistics)
    {
        this.myMetric = (DDMStatistic) statistics;
        if (panel != null)
        {
            panel.setDirected(myMetric.isDirected());
            panel.setNormalized(myMetric.isNormalized());
        }
    }
    
    @Override
    public void unsetup()
    {
        if (panel != null)
        {
            myMetric.setDirected(panel.isDirected());
            myMetric.setNormalized(panel.isNormalized());
            myMetric.setIsChart(panel.isChart());
            myMetric.setSetSizeField(panel.getSetSizeField());
            myMetric.setOptimizedSetSizeField(panel.getOptimizedSetSizeField());
            myMetric.setSelectedAlgorithm( (String) panel.getSelectedAlgorithm());
            myMetric.setOptimizedSetSizeField(panel.getOptimizedSetSizeField());
        }
        panel = null;
    }
    
    @Override
    public String getValue()
    {
        return "";
    }
    
    @Override
    public String getShortDescription()
    {
        return "Statistic calculates dispersal degree measure over nodes.";
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        //Model property change
    }
}
