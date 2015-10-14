/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.controllers;

import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.project.api.WorkspaceListener;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.msalata.ddm.model.DDMModel;

/**
 *
 * @author Mike <Maciej Sałata>
 */
@ServiceProvider(service = DDMController.class)
public class DDMController
{

    private DDMModel model;

    public DDMController()
    {
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
                model = workspace.getLookup().lookup(DDMModel.class);
                if (model == null)
                {
                    model = new DDMModel();
                    workspace.add(model);
                }
            }

            @Override
            public void unselect(Workspace workspace)
            {
            }

            @Override
            public void close(Workspace workspace)
            {
            }

            @Override
            public void disable()
            {
                model = null;
            }
        });
        if (projectController.getCurrentProject() != null)
        {
            Workspace workspace = projectController.getCurrentWorkspace();
            model = workspace.getLookup().lookup(DDMModel.class);
            if (model == null)
            {
                model = new DDMModel();
                workspace.add(model);
            }
        }
    }

    public DDMModel getModel()
    {
        return model;
    }

    public synchronized DDMModel getModel(Workspace workspace)
    {
        DDMModel annotationModel = workspace.getLookup().lookup(DDMModel.class);
        if (annotationModel == null)
        {
            annotationModel = new DDMModel();
            workspace.add(annotationModel);
        }
        return annotationModel;
    }
}
