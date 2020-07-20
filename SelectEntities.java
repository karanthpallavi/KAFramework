/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semdistance;

/**
 *
 * @author DrKM
 */
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
 
/**
 * JList basic tutorial and example
 *
 * @author wwww.codejava.net
 */
public class SelectEntities extends JFrame {
 
    public JList<String> entityList;
    public List<String> chosenValues;
 
    public SelectEntities(String[] entityNames) {
        chosenValues = new ArrayList<String>();
        //create the model and add elements
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(int i = 0;i<entityNames.length;i++)
        {
            listModel.add(i, entityNames[i]);
        }
        
 
        //create the list
        //propertyList = new JList<>(listModel);
        entityList = new JList<>(listModel);
        entityList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    final List<String> selectedValuesList = entityList.getSelectedValuesList();
                    chosenValues = selectedValuesList;
                    System.out.println(selectedValuesList);
                }
            }
        });
 
        add(new JScrollPane(entityList));
 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Choose Entities to compute semantic distance");
        this.setSize(200, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
 
    public List<String> getChosenValues()
    {
        return entityList.getSelectedValuesList();
    }
    
    public int[] getChosenIndices()
    {
        return entityList.getSelectedIndices();
    }
            
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SelectEntities(args);
            }
        });
    }
}
