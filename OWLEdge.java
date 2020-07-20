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
import org.semanticweb.owlapi.model.OWLEntity;

/**
 *
 * @author Pallavi Karanth
 */
public class OWLEdge
{
    //OWLEntity src;
    //OWLEntity dest;
    Integer src;
    Integer dest;
    /*public OWLEdge(OWLEntity sr, OWLEntity dt)
    {
        src = sr;
        dest = dt;
        System.out.println("Edge formed between "+src+"and "+dest);
    }*/
    public OWLEdge(Integer sr, Integer dt)
    {
        src = sr;
        dest = dt;
    }
}