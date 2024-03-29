/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.msalata.ddm;

/**
 *
 * @author Mike
 */
public class DDMPanel extends javax.swing.JPanel
{

    /**
     * Creates new form DispersalDegreeMeasurePanel
     */
    public DDMPanel()
    {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        buttonGroup1 = new javax.swing.ButtonGroup();
        description = new javax.swing.JLabel();
        directedButton = new javax.swing.JRadioButton();
        undirectedButton = new javax.swing.JRadioButton();
        isNormalized = new javax.swing.JCheckBox();
        isChart = new javax.swing.JCheckBox();
        setSizeField = new javax.swing.JTextField();
        setSizeLabel = new javax.swing.JLabel();
        selectAlgorithm = new javax.swing.JComboBox();
        optimizedNodesSetSize = new javax.swing.JTextField();
        optimizedNodesSetSizeLabel = new javax.swing.JLabel();
        selectAlgorithmLabel = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(description, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.description.text")); // NOI18N

        buttonGroup1.add(directedButton);
        org.openide.awt.Mnemonics.setLocalizedText(directedButton, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.directedButton.text")); // NOI18N
        directedButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                directedButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(undirectedButton);
        undirectedButton.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(undirectedButton, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.undirectedButton.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(isNormalized, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.isNormalized.text")); // NOI18N
        isNormalized.setToolTipText(org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.isNormalized.toolTipText")); // NOI18N
        isNormalized.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                isNormalizedActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(isChart, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.isChart.text")); // NOI18N

        setSizeField.setText(org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.setSizeField.text_1")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(setSizeLabel, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.setSizeLabel.text")); // NOI18N

        selectAlgorithm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Degree Centrality", "Eigenvector Centrality", "Closeness Centrality", "Radius Centrality", "Betweenness Centrality" }));

        optimizedNodesSetSize.setText(org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.optimizedNodesSetSize.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(optimizedNodesSetSizeLabel, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.optimizedNodesSetSizeLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(selectAlgorithmLabel, org.openide.util.NbBundle.getMessage(DDMPanel.class, "DDMPanel.selectAlgorithmLabel.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selectAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectAlgorithmLabel))
                    .addComponent(isChart)
                    .addComponent(isNormalized)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(undirectedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(directedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(optimizedNodesSetSize, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(setSizeField, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
                        .addGap(61, 61, 61)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(optimizedNodesSetSizeLabel)
                            .addComponent(setSizeLabel, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(description)
                .addGap(14, 14, 14)
                .addComponent(directedButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(undirectedButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectAlgorithmLabel))
                .addGap(1, 1, 1)
                .addComponent(isNormalized)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(isChart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optimizedNodesSetSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optimizedNodesSetSizeLabel))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setSizeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setSizeLabel))
                .addContainerGap(95, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void directedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directedButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_directedButtonActionPerformed

    private void isNormalizedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isNormalizedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isNormalizedActionPerformed

    public boolean isDirected()
    {
        return directedButton.isSelected();
    }

    public boolean isNormalized()
    {
        return isNormalized.isSelected();
    }
    
    public void setNormalized(boolean normalize)
    {
        isNormalized.setSelected(normalize);
    }

    public void setDirected(boolean directed)
    {
        buttonGroup1.setSelected(directed ? directedButton.getModel() : undirectedButton.getModel(), true);
        if (!directed)
        {
            directedButton.setEnabled(false);
        }
    }

    public boolean isChart()
    {
        return isChart.isSelected();
    }
    
    public String getSetSizeField()
    {
        return setSizeField.getText();
    }
    
    public String getOptimizedSetSizeField()
    {
        return optimizedNodesSetSize.getText();
    }
    
    public Object getSelectedAlgorithm()
    {
        return selectAlgorithm.getSelectedItem();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel description;
    private javax.swing.JRadioButton directedButton;
    private javax.swing.JCheckBox isChart;
    private javax.swing.JCheckBox isNormalized;
    private javax.swing.JTextField optimizedNodesSetSize;
    private javax.swing.JLabel optimizedNodesSetSizeLabel;
    private javax.swing.JComboBox selectAlgorithm;
    private javax.swing.JLabel selectAlgorithmLabel;
    private javax.swing.JTextField setSizeField;
    private javax.swing.JLabel setSizeLabel;
    private javax.swing.JRadioButton undirectedButton;
    // End of variables declaration//GEN-END:variables
}
