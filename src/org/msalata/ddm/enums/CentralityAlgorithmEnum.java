/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm.enums;

/**
 *
 * @author Mike <Maciej Sałata>
 */
public enum CentralityAlgorithmEnum
{

    None(1, "None"),
    Degree(2, "Degree Centrality"),
    Eigenvector(3, "Eigenvector Centrality"),
    Closeness(4, "Closeness Centrality"),
    Radius(5, "Radius Centrality"),
    Betweenness(6, "Betweenness Centrality");

    private final Integer key;
    private final String value;

    CentralityAlgorithmEnum(Integer key, String value)
    {
        this.key = key;
        this.value = value;
    }
    
    public String getValue()
    {
        return value;
    }
    public Integer getKey()
    {
        return key;
    }
}
