/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.preprocessontology;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.formats.*;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom; 
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.HermiT.Reasoner; 
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
//import java.net.*;
import java.io.*;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration; 
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory; 
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor; 
import org.semanticweb.owlapi.reasoner.SimpleConfiguration; 
import org.semanticweb.owlapi.reasoner.NullReasonerProgressMonitor;
import org.semanticweb.owlapi.util.InferredAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredOntologyGenerator; 
import org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator; 
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator; 
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;
import org.semanticweb.owlapi.reasoner.InferenceType; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.Collections; 



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
        
        String inferredfileName = args[2];
        System.out.println("Inferred Ontology is saved at path "+inferredfileName);
        
        String inferredfileTriples = args[3];
        System.out.println("Inferred Ontology in triples format is saved at path "+inferredfileTriples);
        
        try
        {
            //int successLoading = loadOntology(fileName,folderName);
            System.out.println("Call to infer function");
            //inferAndSaveOntology(fileName,folderName,inferredfileName);
            //System.out.println("Call to infer function");
            inferOntology(fileName, folderName, inferredfileName, inferredfileTriples);
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
        /*List<InferredAxiomGenerator<? extends OWLAxiom>> axiomGenerators = Collections.unmodifiableList( 
                new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>( 
                        Arrays.asList( 
                                new InferredClassAssertionAxiomGenerator(), 
                                new InferredDataPropertyCharacteristicAxiomGenerator(), 
                                new InferredEquivalentClassAxiomGenerator(), 
                                new InferredEquivalentDataPropertiesAxiomGenerator(), 
                                new InferredEquivalentObjectPropertyAxiomGenerator(), 
                                new InferredInverseObjectPropertiesAxiomGenerator(), 
                                new InferredObjectPropertyCharacteristicAxiomGenerator(), 
                                new InferredPropertyAssertionGenerator(), 
                                new InferredSubClassAxiomGenerator(), 
                                new InferredSubDataPropertyAxiomGenerator(), 
                                new InferredSubObjectPropertyAxiomGenerator() 
                        ))); ;*/
        
        //}
        //owlFile.deleteOnExit();
        //folder.deleteOnExit();
        return 1;
    }
    
    public static void inferAndSaveOntology(String fileName, String folderName, String inferredFileName) throws OWLOntologyCreationException, OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException, OWLOntologyStorageException
    {
        OWLReasonerFactory reasonerFactory = null;
            // Uncomment the line below
        reasonerFactory = new PelletReasonerFactory();
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        
        OWLDataFactory df = manager.getOWLDataFactory();
        
        File owlFile = new File(fileName);
        File folder = new File(folderName);
        AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
        manager.addIRIMapper(mapper);
        
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(owlFile);
        
        // Create the reasoner and classify the ontology
            OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
            reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);

            // To generate an inferred ontology we use implementations of inferred axiom generators
            // to generate the parts of the ontology we want (e.g. subclass axioms, equivalent classes
            // axioms, class assertion axiom etc. - see the org.semanticweb.owlapi.util package for more
            // implementations).  
            // Set up our list of inferred axiom generators
            List<InferredAxiomGenerator<? extends OWLAxiom>> gens = new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
            gens.add(new InferredSubClassAxiomGenerator());

            // Put the inferred axioms into a fresh empty ontology - note that there
            // is nothing stopping us stuffing them back into the original asserted ontology
            // if we wanted to do this.
            OWLOntology infOnt = manager.createOntology();

            // Now get the inferred ontology generator to generate some inferred axioms
            // for us (into our fresh ontology).  We specify the reasoner that we want
            // to use and the inferred axiom generators that we want to use.
            InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, gens);
            iog.fillOntology(df, infOnt);

            // Save the inferred ontology. (Replace the URI with one that is appropriate for your setup)
            manager.saveOntology(infOnt, IRI.create(inferredFileName));
    }
    
    public static void inferOntology(String fileName, String folderName, String inferredFileName, String inferredfileTriples) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException, OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException
    {
        // First, we create an OWLOntologyManager object. The manager will load and 
    // save ontologies. 
        System.getProperty("java.class.path");
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        
        // Now, we create the file from which the ontology will be loaded. 
    // Here the ontology is stored in a file locally in the ontologies subfolder
    // of the examples folder.
        File inputOntologyFile = new File(fileName);
        File folder = new File(folderName);
        manager.clearIRIMappers();
        
        AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
        manager.addIRIMapper(mapper);
        // We use the OWL API to load the ontology. 
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(inputOntologyFile);
        OWLDataFactory df = manager.getOWLDataFactory();
        
        
        // Now we can start and create the reasoner. Since materialisation of axioms is controlled 
        // by OWL API classes and is not natively supported by HermiT, we need to instantiate HermiT 
        // as an OWLReasoner. This is done via a ReasonerFactory object. 
        /*ReasonerFactory factory = new ReasonerFactory();
        // The factory can now be used to obtain an instance of HermiT as an OWLReasoner. 
        Configuration c=new Configuration();
        c.reasonerProgressMonitor=new ConsoleProgressMonitor();
        
        OWLReasoner reasoner=factory.createReasoner(ontology, c);*/
        // The following call causes HermiT to compute the class, object, 
        // and data property hierarchies as well as the class instances. 
        // Hermit does not yet support precomputation of property instances. 
        Reasoner.ReasonerFactory rf = new Reasoner.ReasonerFactory() {
            @Override
            public OWLReasoner createReasoner(OWLOntology ontology,
OWLReasonerConfiguration config) {
                Configuration configuration = new Configuration();
                configuration.ignoreUnsupportedDatatypes = true;
                return super.createReasoner(ontology, configuration);
                }
        };
          
        Reasoner reasoner=(Reasoner)rf.createReasoner(ontology);
        boolean consistencyCheck = reasoner.isConsistent();
        if(consistencyCheck)
        {
            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY, InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
        // We now have to decide which kinds of inferences we want to compute. For different types 
        // there are different InferredAxiomGenerator implementations available in the OWL API and 
        // we use the InferredSubClassAxiomGenerator and the InferredClassAssertionAxiomGenerator 
        // here. The different generators are added to a list that is then passed to an 
        // InferredOntologyGenerator. 
            List<InferredAxiomGenerator<? extends OWLAxiom>> generators=new ArrayList<InferredAxiomGenerator<? extends OWLAxiom>>();
            generators.add(new InferredSubClassAxiomGenerator());
            generators.add(new InferredClassAssertionAxiomGenerator());
            generators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
    generators.add(new InferredEquivalentClassAxiomGenerator());
    generators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
    generators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
    generators.add(new InferredInverseObjectPropertiesAxiomGenerator());
    generators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());
    
    // NOTE: InferredPropertyAssertionGenerator significantly slows down
    // inference computation
    generators.add(new org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator());
    
    generators.add(new InferredSubClassAxiomGenerator());
    generators.add(new InferredSubDataPropertyAxiomGenerator());
    generators.add(new InferredSubObjectPropertyAxiomGenerator());
    List<InferredIndividualAxiomGenerator<? extends OWLIndividualAxiom>> individualAxioms= new ArrayList<InferredIndividualAxiomGenerator<? extends OWLIndividualAxiom>>();
		  generators.addAll(individualAxioms);
		  
        // We dynamically overwrite the default disjoint classes generator since it tries to 
        // encode the reasoning problem itself instead of using the appropriate methods in the 
        // reasoner. That bypasses all our optimisations and means there is not progress report :-( 
        // We don't want that!
        generators.add(new InferredDisjointClassesAxiomGenerator() {
            boolean precomputed=false;
            protected void addAxioms(OWLClass entity, OWLReasoner reasoner, OWLDataFactory dataFactory, Set<OWLDisjointClassesAxiom> result) {
                if (!precomputed) {
                    reasoner.precomputeInferences(InferenceType.DISJOINT_CLASSES);
                    precomputed=true;
                }
                for (OWLClass cls : reasoner.getDisjointClasses(entity).getFlattened()) {
                    result.add(dataFactory.getOWLDisjointClassesAxiom(entity, cls));
                }
            }
        });
        // We can now create an instance of InferredOntologyGenerator. 
        InferredOntologyGenerator iog=new InferredOntologyGenerator(reasoner,generators);
        // Before we actually generate the axioms into an ontology, we first have to create that ontology. 
        // The manager creates the for now empty ontology for the inferred axioms for us. 
        OWLOntology inferredAxiomsOntology=manager.createOntology();
        OWLOntology inferredTriplesOntology = manager.createOntology();
        // Now we use the inferred ontology generator to fill the ontology. That might take some 
        // time since it involves possibly a lot of calls to the reasoner.    
        iog.fillOntology( df, inferredAxiomsOntology);
        // To add original axioms to the inferred ontology
        manager.addAxioms(inferredAxiomsOntology, ontology.getAxioms());
        iog.fillOntology(df,inferredTriplesOntology);
        manager.addAxioms(inferredTriplesOntology, ontology.getAxioms());
        // Now the axioms are computed and added to the ontology, but we still have to save 
        // the ontology into a file. Since we cannot write to relative files, we have to resolve the 
        // relative path to an absolute one in an OS independent form. We do this by (virtually) creating a 
        // file with a relative path from which we get the absolute file.  
        File inferredOntologyFile=new File(inferredFileName);
        if (!inferredOntologyFile.exists())
            inferredOntologyFile.createNewFile();
        inferredOntologyFile=inferredOntologyFile.getAbsoluteFile();
        
        File inferredTriplesFile = new File(inferredfileTriples);
        if(!inferredTriplesFile.exists())
            inferredTriplesFile.createNewFile();
        inferredTriplesFile=inferredTriplesFile.getAbsoluteFile();
        // Now we create a stream since the ontology manager can then write to that stream. 
        OutputStream outputStream=new FileOutputStream(inferredOntologyFile);
        // We use the same format as for the input ontology.
        manager.saveOntology(inferredAxiomsOntology, manager.getOntologyFormat(ontology), outputStream);
        OutputStream outputStreamTriples=new FileOutputStream(inferredTriplesFile);
        NTriplesDocumentFormat nt = new NTriplesDocumentFormat();
        manager.saveOntology(inferredAxiomsOntology,nt , outputStreamTriples);
        // Now that ontology that contains the inferred axioms should be in the ontologies subfolder 
        // (you Java IDE, e.g., Eclipse, might have to refresh its view of files in the file system) 
        // before the file is visible.  
        System.out.println("The ontology in " + inferredFileName + " should now contain all inferred axioms (you might need to refresh the IDE file view). ");
        }// End if consistencyCheck
        else
        {
            System.out.println("Inconsistent input Ontology, Please check the OWL File");
        }
    }
}