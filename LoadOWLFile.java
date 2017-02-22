/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.preprocessontology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLException;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
//import java.net.*;
import java.io.*;
import java.io.File;


/**
 *
 * @author DrKM
 */
public class LoadOWLFile
{
    public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException {
        //String reasonerFactoryClassName = null;
        
        String fileName = args[0];
        System.out.println("Ontology being processed is " + fileName); 
        
        String folderName = args[1];
        System.out.println("Folder containing other imported ontologies is "+folderName);
        try
        {
            int successLoading = loadOntology(fileName,folderName);
        }
        catch(OWLException e)
                {
                    e.printStackTrace();
                }
}
    
    public static int loadOntology(String fileName, String folderName) throws OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException
    {
        // We first need to obtain a copy of an
        // OWLOntologyManager, which, as the name
        // suggests, manages a set of ontologies.
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // We load an ontology from the URI specified
        // on the command line
        File owlFile = new File(fileName);
        File folder = new File(folderName);
        AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
        manager.addIRIMapper(mapper);
        //IRI documentIRI = IRI.create(x.trim());
        //InputStream is = LoadOWLFile.class.getResourceAsStream("/resources/food.owl");
        // Now load the ontology.
        //if(is.available()!=0)
        //{
        //OWLOntology ontology = manager.loadOntologyFromOntologyDocument(is);
        //OWLOntology ontology = manager.loadOntologyFromOntologyDocument(documentIRI);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(owlFile);
        
        // Report information about the ontology
        System.out.println("Ontology Loaded...");
        //System.out.println("Document IRI: " + documentIRI);
        System.out.println("Ontology : " + ontology.getOntologyID());
        System.out.println("Format      : " + manager.getOntologyFormat(ontology));
        //}
        owlFile.deleteOnExit();
        folder.deleteOnExit();
        return 1;
    }
    
}