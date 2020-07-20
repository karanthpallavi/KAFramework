/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semanticdegreecentrality;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import javax.swing.JButton;
import java.util.HashSet;
import javax.swing.JList;
import javax.swing.JFrame;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import javax.swing.event;

public class MultiSelectionDemo extends JFrame implements ActionListener
{
  JList list;
  private JButton button;
  Object[] selection;

  Set<String> selectedValues = new HashSet();
 
  /*//Default Constructor
  public MultiSelectionDemo()
  {
    Container c = getContentPane(); 	
    c.setLayout(new FlowLayout());
    String names[] = {"Banglore", "Hyderabad", "Ooty", "Chennai", "Mumbai", "Delhi", "Kochi", "Darjeeling"};
    places = new JList(names) ;                    // creating JList object; pass the array as parameter
    places.setVisibleRowCount(5); 
		     
    places.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	
    c.add(new JScrollPane(places));
 
    places.addListSelectionListener(this);
 
    setTitle("Practcing Multiple selection JList");
    setSize(300,300);
    setVisible(true);
  }*/
  public MultiSelectionDemo(Vector listItems)
  {
      this.getContentPane().setLayout(new FlowLayout());
      list = new JList(listItems);
      list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      button = new JButton("Select Properties for Filtering");
      button.addActionListener(this);
      add(list);
      add(button);

      //JFrame window = null;
    /*Container c = getContentPane(); 	
    c.setLayout(new FlowLayout());
    //String names[] = {"Banglore", "Hyderabad", "Ooty", "Chennai", "Mumbai", "Delhi", "Kochi", "Darjeeling"};
    places = new JList(listItems) ;                    // creating JList object; pass the array as parameter
    places.setVisibleRowCount(5); 
		     
    places.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	
    c.add(new JScrollPane(places));
    places.setValueIsAdjusting(true);
    ListSelectionModel listSelectionModel = places.getSelectionModel();
    /*listSelectionModel.addListSelectionListener(
                new SharedListSelectionHandler());*/
    
    //places.addListSelectionListener(this);
    /*listSelectionModel.addListSelectionListener(
                new SharedListSelectionHandler());  */
    /*setTitle("Choose Properties to filter Ontology Graph");
    setSize(300,300);
    setVisible(true);
    //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
  }
  
  
  public void actionPerformed(ActionEvent e) 
  {
        if (e.getActionCommand().equals("Select Properties for Filtering")) {
            selection = list.getSelectedValues();
            for(int i=0;i<selection.length;i++)
    {
      System.out.println("Selected Property "+selection[i]);
    }
            //int index = list.getSelectedIndex();
            //System.out.println("Index Selected: " + index);
            //String s = (String) list.getSelectedValue();
            //System.out.println("Value Selected: " + s);
        }
  }
}
          
  /*public void valueChanged(ListSelectionEvent e)
  {
    String destinations = "";
      List obj=  places.getSelectedValuesList();
    for(int i=0;i<obj.size();i++)
    {
      destinations += (String) obj.get(i);
    }
    
    sendSelection(obj);
    JOptionPane.showMessageDialog( null, "You go: " + destinations,  "Learning Multiple Selections", JOptionPane.PLAIN_MESSAGE);
  }
  public Set<String> sendSelection(List obj)
  {
      
      for(int i=0;i<obj.size();i++)
      {
          selectedValues.add((String) obj.get(i));
          //System.out.println(obj.get(i));
      }
      
      return selectedValues;
  }
  
  

private void jList1MouseClicked(java.awt.event.MouseEvent evt) {

    Object sel =null;

    int[] selectedIx = this.places.getSelectedIndices();      

    for (int i = 0; i < selectedIx.length; i++) {
        sel = places.getModel().getElementAt(selectedIx[i]);
    }

    System.out.println(sel);
}*/


  
  /*class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) { 
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
            selectedValues.addAll(places.getSelectedValuesList());
            Iterator selectedIter = selectedValues.iterator();
            while(selectedIter.hasNext())
            {
                System.out.println(selectedIter.next().toString()+"\n");
            }
        }
  }*/
  
