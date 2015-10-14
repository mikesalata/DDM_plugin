/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm;

import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mike
 */
@ServiceProvider(service = StatisticsBuilder.class)
public class DDMStatisticBuilder  implements StatisticsBuilder
{
    @Override
    public String getName() {
        return "Dispersal Degree Measure Statistic";
    }

    @Override
    public Statistics getStatistics() {
        return new DDMStatistic();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return DDMStatistic.class;
    }
}
