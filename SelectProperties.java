/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semanticdegreecentrality;

/**
 *
 * @author Pallavi Karanth
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
public class SelectProperties extends JFrame {
 
    private JList<String> propertyList;
    public List<String> chosenValues;
 
    public SelectProperties(String[] propNames) {
        chosenValues = new ArrayList<String>();
        //create the model and add elements
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for(int i = 0;i<propNames.length;i++)
        {
            listModel.add(i, propNames[i]);
        }
        
 
        //create the list
        //propertyList = new JList<>(listModel);
        propertyList = new JList<>(listModel);
        propertyList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    final List<String> selectedValuesList = propertyList.getSelectedValuesList();
                    chosenValues = selectedValuesList;
                    System.out.println(selectedValuesList);
                }
            }
        });
 
        add(new JScrollPane(propertyList));
 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("JList Example");
        this.setSize(200, 200);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
 
    public List<String> getChosenValues()
    {
        return chosenValues;
    }
            
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SelectProperties(args);
            }
        });
    }
}
