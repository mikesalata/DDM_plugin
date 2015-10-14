/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gephi.graph.api.Edge;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public class DDMModel
{

    private static final String properyName = "DDM_NODE_MEASURE_MODEL";
    private final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
    private Map<Edge, Double> results;

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if (!listeners.contains(listener))
        {
            listeners.add(listener);
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        listeners.remove(listener);
    }

    private void firePropertyChangeEvent(String propertyName, Object oldValue, Object newValue)
    {
        PropertyChangeEvent propertyChangeEvent = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners)
        {
            listener.propertyChange(propertyChangeEvent);
        }
    }

    /**
     * @return the results
     */
    public Map<Edge, Double> getResults()
    {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(Map<Edge, Double> results)
    {
        if (!this.results.equals(results))
        {
            Map<Edge, Double> oldValue = this.results;
            this.results = results;
            firePropertyChangeEvent(properyName, oldValue, results);
        }
    }
}
