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

import static com.google.common.io.Files.map;
import java.io.File;
import com.mycompany.semdistance.SelectEntities;
import com.mycompany.semdistance.OWLEdge;
import static com.mycompany.semdistance.SemDistance.computeDistanceRegularGraph;
import com.mycompany.semdistance.TransformTriplesToGraph;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.lang.Math.*;
import java.util.Optional;
import java.util.Set;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.HermiT.structural.OWLAxioms;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
//import org.semanticweb.owl.vocab.NamespaceOWLOntologyFormat;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectImpl;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Double.POSITIVE_INFINITY;
import java.util.Scanner;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.io.CSVExporter;
import org.jgrapht.io.CSVFormat;
import org.jgrapht.io.ExportException;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLPrimitive;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;


/**
 *
 * @author Pallavi Karanth
 */

    public class SemDistance {
public static DirectedGraph<Integer,DefaultEdge> RegularGraph;
    public static List<OWLObjectImpl> entitiesOfGraph = new IdentityArrayList<>();
    public static List<OWLEdge> edgesOfGraph = new ArrayList<>();
    public static List<String> axiomTypes = new ArrayList<>();
    
    public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException, OWLOntologyCreationException, OWLOntologyStorageException, ExportException  {
        
        String OntologyFile = new String(args[0]);
        System.out.println("The Ontology file which is preprocessed and used for computing Semantic Distance is "+OntologyFile);
        String FolderName = new String(args[1]);
        System.out.println("The folder containing ontology file is "+FolderName);
        String namespace = new String(args[2]);
        String source = new String(args[3]);
        String destination = new String(args[4]);
        
        int success = loadOntology(OntologyFile,FolderName, namespace,source, destination);
        
    }
    
    public static int loadOntology(String fileName, String folderName, String namespace, String source, String destination) throws OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException, OWLOntologyCreationException, OWLOntologyStorageException, ExportException
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
        //OWLEntity entity1 = Entity1;
        //IRI iri = manager.getOntologyDocumentIRI(ontology);
        //String namespace = "";
        /*OWLDocumentFormat format = manager.getOntologyFormat(ontology);
             if(format instanceof NamespaceOWLOntologyFormat) {
                 NamespaceOWLOntologyFormat nsFormat =
(NamespaceOWLOntologyFormat) format;
                 Map<String, String> nsMap =
nsFormat.getNamespacesByPrefixMap();
                 for(String prefix : nsMap.keySet()) {
                     if (prefix.length() != 0) {
                         System.out.println(prefix + " --> " +
nsMap.get(prefix));
                     }
                     else {
                         System.out.println("Default: " +
nsMap.get(""));
                         namespace = nsMap.get("");
                     }
                 }
             }*/
        double SemDist = SemanticDistance(manager,ontology,namespace,source,destination);
        int regularGraphFromOWL = getRegularGraphFromOWL(ontology);
        int regDist = computeDistanceRegularGraph(33,34);
        //TransformTriplesToGraph toGraph = new TransformTriplesToGraph(ontology);
        //owlFile.deleteOnExit();
        folder.deleteOnExit();
        return 1;
    }
    
    public static double SemanticDistance(OWLOntologyManager manager, OWLOntology ontology, String namespace, String src, String dest) throws ExportException
    {
        double SemDist = 0.0;
        
        //System.out.println(toGraph.OWLGraph.edgeSet());
        // Construct source and destination for getting paths
        OWLDataFactory datafactory = manager.getOWLDataFactory();
        OWLEntity source = datafactory.getOWLNamedIndividual(namespace + src);
        OWLEntity destination = datafactory.getOWLNamedIndividual(namespace+dest);
        // Get taxonomy of classes and store to see if there are zig zag paths while computing distance
        //Set<OWLClass> classesInSignature = ontology.getClassesInSignature();
        //OWLReasonerFactory reasonerFactory = new OWLReasonerFactory();
        //OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
        //printHierarchy(reasoner, clazz, 0);
        // Convert all OWL Triples from Ontology to a Graph of JGraph library
        TransformTriplesToGraph toGraph = new TransformTriplesToGraph(ontology,source,destination);
        System.out.println("Source"+source);
        System.out.println("Destination"+destination);

        // Graph is constructed from ontology
        // Call AllDirectedPaths for this graph
        SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> OWLGraph = toGraph.OWLGraph;
        AllDirectedPaths allPathsGraph = new AllDirectedPaths(OWLGraph);
        System.out.println(allPathsGraph.toString());
        List<OWLObjectImpl> entitiesOfGraph = toGraph.entitiesOfGraph;
       DirectedGraph<Integer,DefaultEdge> OWLNonWeightedGraph = toGraph.OWLNonWeightedGraph;
        FloydWarshallShortestPaths shortestPathsGraph = new FloydWarshallShortestPaths(OWLNonWeightedGraph);
        int numVert = OWLNonWeightedGraph.vertexSet().size();
        int indVert = 0;
        int nextVert = indVert + 1;
       
        /*List<OWLObjectImpl> entitiesOfGraph = toGraph.entitiesOfGraph;
        for(int i = 0; i < entitiesOfGraph.size();i++)
        {
            for(int j = 0; j < entitiesOfGraph.size();j++)
            {
                if(i==j)
                    continue;
                double shortestDistance = shortestPathsGraph.shortestDistance(i, j);
                System.out.println("Regular shortest distance between " + i + "and "+ j + "is "+shortestDistance);
            }
        }*/
        
        List allPaths = allPathsGraph.getAllPaths( 1,3,true, 6);
        System.out.println(allPaths);
        
        // Write allPaths to file
        BufferedWriter bw = null;
		FileWriter fw = null;
		try {

			//String content = "This is the content to write into file\n";

			fw = new FileWriter("E:/Pallavi/Ontology/AllPathstxt");
			bw = new BufferedWriter(fw);
			//bw.write(content);
                        bw.write(allPaths.toString());

			System.out.println("Done writing Paths to file");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
        
        // Get property hierarchy tree and send it as a parameter to computeShortestSemanticPath
        DirectedGraph<OWLEntity, DefaultEdge> propertyGraph = toGraph.OWLPropertyGraph;
        //List<OWLObjectImpl> entitiesOfGraph = toGraph.entitiesOfGraph;
        List<OWLEdge> edgesOfGraph = toGraph.edgesOfGraph;
        //SemDist = computeShortestSemanticPath(allPaths,reasoner,ontology,entitiesInGraph,edgesInGraph,allPathsGraph);
        SemDist = computeShortestSemanticPath(allPaths,ontology,allPathsGraph,propertyGraph,entitiesOfGraph,edgesOfGraph,OWLGraph);
        System.out.println("The Semantic Distance between "+source+" and destination " +destination +" is "+SemDist);
        
        
    /*double semdt = din.readDouble();
    System.out.println(semdt);*/
    /*while(scan.hasNextDouble())
    {
        System.out.println(scan.nextDouble());
    }*/
  
  
        return SemDist;
    }
    
    
    
    public static double computeShortestSemanticPath(List allPaths, OWLOntology ontology, AllDirectedPaths allPathsGraph,DirectedGraph<OWLEntity, DefaultEdge> propertyGraph, List<OWLObjectImpl> entitiesOfGraph, List<OWLEdge> edgesOfGraph,SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> OWLGraph )
    {
        NodeSet<OWLObjectPropertyExpression> superObjectProperties = null;
                        NodeSet<OWLObjectPropertyExpression> subObjectProperties=null;
        double SemDistShort = 0.0;
        double SemDist = 0.0;
        // Initialize constants
        double k1=0.1;
        double k2 = 0.5;
        double k3 = 0.7;
        double k4 = 0.9;
        double alpha = 1.0;
        double beta = 0.05;
        double gamma = 1.0;
        int numberOfShortestPaths = 0;
        // Read List elements
        // Each Path
        Iterator listIter = allPaths.iterator();
        List<GraphWalk> allPathsString;
        allPathsString = new ArrayList<GraphWalk>();
        // Check if there is no path between source and destination
        if(allPaths.size()==0)
        {
            System.out.println("No path between source and destination found");
            SemDistShort = POSITIVE_INFINITY;
            return SemDistShort;
        }
        
        int i = 1;
        while(listIter.hasNext())
        {
            //System.out.println("Path "+i);
            //System.out.println(listIter.next());
            GraphWalk path = (GraphWalk) listIter.next();
            allPathsString.add(path);
            // Process paths            
            i++;
        }
        System.out.println("Number of paths are "+i);
        
        boolean onlyTaxonomicalEdges= false;
        boolean onlyNonTaxonomicalEdges = false;
        List<Double> semDistOfPaths = new ArrayList<>();
        List<Integer> OnlyTaxonomicalEdgesPathIndexList = new ArrayList<>();
        // For now, process the length of the last path - path number 121 
        // Later, run the same alg for all paths and find the shortest path
        for(int pathIter = 0; pathIter < i-1;pathIter++)
        {
            //SemDistShort = 0.0;
            SemDist = 0.0;
            List<Integer> numTaxonomicalEdges = new ArrayList<>();
            int taxonomicalTurnCount = 0;
            int ObjectPropertyTurnCount = 0;
            int mixTurnCount = 0;
            
          GraphWalk pathToProcess = allPathsString.get(pathIter);
          // for now, comnted next line for processing and computing distance of all the paths
        //GraphWalk pathToProcess = allPathsString.get(i-2);
        //List vertexList = new List("<http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#Ram>","<http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#Orange>","<http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#VitaminC>","<http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#Scurvy>");
        //GraphWalk pathToProcess = new GraphWalk(allPathsGraph,)
            System.out.println("Processing path ");
            System.out.println(pathToProcess);
            
            // For this path, get vertex list and edge type to compute path length
            Integer startVertex = (Integer) pathToProcess.getStartVertex();
            List edgeList = pathToProcess.getEdgeList();
            System.out.println("Start vertex of this path is "+startVertex+ " ");
            System.out.println(edgeList);
            OWLObjectImpl startVert = entitiesOfGraph.get(startVertex);
            System.out.println(startVert);
            // To get next edges and get type, process length
            List vertexList = pathToProcess.getVertexList();
            for(int index = 0; index < vertexList.size();index++)
            {
                Integer firstVert = (Integer) vertexList.get(index);
                for(int nextVert = index+1; nextVert < vertexList.size();nextVert++)
                {
                    Integer secondVert = (Integer) vertexList.get(nextVert);
                    OWLObjectImpl firstVertObj = entitiesOfGraph.get(firstVert);
                    OWLObjectImpl secondVertObj = entitiesOfGraph.get(secondVert);
                    System.out.println("Edge between "+firstVertObj+ " and "+secondVertObj);
                    //SemDist+=
                    // Get edge weights
                    
                    //OWLEdge edge = new OWLEdge(firstVert,secondVert);
                    //int indexEdge = edgesOfGraph.indexOf(edge);
                    //System.out.println(indexEdge);
                    break;
                }   
            }
            
            for(int j = 0; j<edgeList.size();j++)
            {
                // Retreive edges and their weights in the path
                // Add weights to the distance
                
                DefaultWeightedEdge edge = (DefaultWeightedEdge) edgeList.get(j);
                double edgeWeight = OWLGraph.getEdgeWeight(edge);
                System.out.println(edgeWeight);
                SemDist+=edgeWeight;
                
                for(int nextEdge = j+1;nextEdge<edgeList.size();nextEdge++)
                {
                    DefaultWeightedEdge nextEdgeObj =  (DefaultWeightedEdge) edgeList.get(nextEdge);
                    double edgeWeightNext = OWLGraph.getEdgeWeight(nextEdgeObj);
                    if(edgeWeight==k1&&edgeWeightNext==k1)
                    {
                        // Two taxonomical edges
                        taxonomicalTurnCount++;
                        SemDist+=2*beta;
                        System.out.println("Two taxonomical edges turn");
                        // Find if its a zig zag movement
                        Integer edgeSource = OWLGraph.getEdgeSource(edge);
                        Integer edgeTarget = OWLGraph.getEdgeTarget(edge);
                        Integer nextEdgeTarget = OWLGraph.getEdgeTarget(nextEdgeObj);
                        OWLObjectImpl firstSource = entitiesOfGraph.get(edgeSource);
                        OWLObjectImpl secondSource = entitiesOfGraph.get(edgeTarget);
                        OWLObjectImpl secondTarget = entitiesOfGraph.get(nextEdgeTarget);
                        System.out.println("First Source is "+firstSource);
                        System.out.println("Second Source is "+secondSource);
                        System.out.println("Second Target is "+secondTarget);
                        
                        // Check if its going up and coming down the taxonomy
                        //OWLClass classSourceInQues = firstSource.
                        //System.out.println(classSourceInQues);
                        /*DefaultWeightedEdge nexttonextEdgeObj =  (DefaultWeightedEdge) edgeList.get(nextEdge+1);
                        double nextToNextEdgeWeight = OWLGraph.getEdgeWeight(nexttonextEdgeObj);
                        if(nextToNextEdgeWeight==k1)
                        {
                        Integer edgeSource = OWLGraph.getEdgeSource(edge);
                        Integer edgeTarget = OWLGraph.getEdgeTarget(edge);
                        Integer edgeTarget1 = OWLGraph.getEdgeTarget(nexttonextEdgeObj);
                        }*/
                    }
                    else if((edgeWeight == k2&& edgeWeightNext == k1) || (edgeWeight == k4&& edgeWeightNext == k1) || (edgeWeight == k4 && edgeWeightNext == k1))
                    {//Object Property and Taxonomical
                        mixTurnCount++;
                        SemDist+=alpha;
                        System.out.println("Object Property and Taxonomical turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    else if((edgeWeight == k1 && edgeWeightNext == k2)||(edgeWeight == k1&&edgeWeightNext == k3)||(edgeWeight == k1&&edgeWeightNext == k4 ))
                    {
                        //Taxonomical and Object
                        mixTurnCount++;
                        SemDist+=alpha;
                        System.out.println("Taxonomical and Object Property turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    /*else if((edgeWeight == k1 && edgeWeightNext == k2) || edgeWeightNext == k3||edgeWeightNext == k4)
                    {
                        mixTurnCount++;
                        SemDist+=alpha;
                        System.out.println("Taxonomical and Object Property turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }*/
                    else if((edgeWeight == k2 && edgeWeightNext == k1)||(edgeWeight == k3&& edgeWeightNext == k1)||(edgeWeight == k4  && edgeWeightNext == k1))
                    {
                        mixTurnCount++;
                        SemDist+=alpha;
                        System.out.println("Object Property and Taxonomical turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    else if((edgeWeight == k2 || edgeWeight == k3||edgeWeight == k4) && (edgeWeightNext == k2 ||edgeWeightNext == k3||edgeWeightNext == k4) )
                    {
                        // Distance between properties in the property hierarchy tree
                        // For now, 2.0
                        ObjectPropertyTurnCount++;
                        SemDist+=2.0;
                        System.out.println("Object Property and Object Property turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    else if(edgeWeight == k4 && edgeWeightNext == k4)
                    {
                        // Distance between properties in the property hierarchy tree
                        ObjectPropertyTurnCount++;
                        Integer edgeSource = OWLGraph.getEdgeSource(edge);
                        Integer edgeTarget = OWLGraph.getEdgeTarget(edge);
                        Integer nextEdgeTarget = OWLGraph.getEdgeTarget(nextEdgeObj);
                        OWLObjectImpl firstSource = entitiesOfGraph.get(edgeSource);
                        OWLObjectImpl secondSource = entitiesOfGraph.get(edgeTarget);
                        OWLObjectImpl secondTarget = entitiesOfGraph.get(nextEdgeTarget);
                        System.out.println("First Source is "+firstSource);
                        System.out.println("Second Source is "+secondSource);
                        System.out.println("Second Target is "+secondTarget);
                        // Get property between these source and targets
                        Set<OWLAxiom> referencingAxioms3 = ontology.getReferencingAxioms((OWLPrimitive) firstSource);
                        List<OWLObjectProperty> propertiesInTurn = new ArrayList<>();
                        for(OWLAxiom axiomInQues: referencingAxioms3)
                        {
                            if(axiomInQues.containsEntityInSignature((OWLEntity) secondSource))
                            {
                                //System.out.println("Axiom containing second object");
                                //System.out.println(axiomInQues);
                                Set<OWLObjectProperty> objectPropertiesInSignature = axiomInQues.getObjectPropertiesInSignature();
                                System.out.println("Size of properties set is "+objectPropertiesInSignature.size());
                                OWLObjectProperty prop = objectPropertiesInSignature.iterator().next();
                                System.out.println("Property 1 in turn is "+prop);
                                Reasoner.ReasonerFactory factory = new Reasoner.ReasonerFactory();
                                Configuration configuration=new Configuration();
                                configuration.throwInconsistentOntologyException=false;
                                OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
                                superObjectProperties = reasoner.getSuperObjectProperties(prop);
                                subObjectProperties = reasoner.getSubObjectProperties(prop);
                                propertiesInTurn.add(prop);
                                break;
                            }
                        }
                        Set<OWLAxiom> referencingAxioms4 = ontology.getReferencingAxioms((OWLPrimitive) secondSource);
                        for(OWLAxiom axiomInQues: referencingAxioms4)
                        {
                            if(axiomInQues.containsEntityInSignature((OWLEntity) secondTarget))
                            {
                                //System.out.println("Axiom containing second object");
                                //System.out.println(axiomInQues);
                                Set<OWLObjectProperty> objectPropertiesInSignature = axiomInQues.getObjectPropertiesInSignature();
                                //System.out.println("Size of properties set is "+objectPropertiesInSignature.size());
                                OWLObjectProperty prop = objectPropertiesInSignature.iterator().next();
                                System.out.println("Property 2 in turn is "+prop);
                                propertiesInTurn.add(prop);
                                break;
                            }
                        }
                        OWLObjectProperty prop1 = propertiesInTurn.get(0);
                        OWLObjectProperty prop2 = propertiesInTurn.get(1);
                        if(superObjectProperties.containsEntity(prop2))
                        {
                                SemDist+=1.0;
                                System.out.println("In hierarchy");
                        }
                        else if(subObjectProperties.containsEntity(prop2))
                        {
                            SemDist+=1.0;
                            System.out.println("In hierarchy");
                        }
                        else
                        {
                            SemDist+=2.0;
                            System.out.println("Not in hierarchy");
                        }
                        System.out.println("Object Property and Object Property turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    else if(edgeWeight == k3 && edgeWeightNext == k3)
                    {
                        // Distance between properties in the property hierarchy tree
                        ObjectPropertyTurnCount++;
                        SemDist+=2.0;
                        Integer edgeSource = OWLGraph.getEdgeSource(edge);
                        Integer edgeTarget = OWLGraph.getEdgeTarget(edge);
                        Integer nextEdgeTarget = OWLGraph.getEdgeTarget(nextEdgeObj);
                        OWLObjectImpl firstSource = entitiesOfGraph.get(edgeSource);
                        OWLObjectImpl secondSource = entitiesOfGraph.get(edgeTarget);
                        OWLObjectImpl secondTarget = entitiesOfGraph.get(nextEdgeTarget);
                        System.out.println("First Source is "+firstSource);
                        System.out.println("Second Source is "+secondSource);
                        System.out.println("Second Target is "+secondTarget);
                        // Get property between these source and targets
                        Set<OWLAxiom> referencingAxioms3 = ontology.getReferencingAxioms((OWLPrimitive) firstSource);
                        List<OWLObjectProperty> propertiesInTurn = new ArrayList<>();
                        for(OWLAxiom axiomInQues: referencingAxioms3)
                        {
                            if(axiomInQues.containsEntityInSignature((OWLEntity) secondSource))
                            {
                                //System.out.println("Axiom containing second object");
                                //System.out.println(axiomInQues);
                                Set<OWLObjectProperty> objectPropertiesInSignature = axiomInQues.getObjectPropertiesInSignature();
                                System.out.println("Size of properties set is "+objectPropertiesInSignature.size());
                                OWLObjectProperty prop = objectPropertiesInSignature.iterator().next();
                                System.out.println("Property 1 in turn is "+prop);
                                Reasoner.ReasonerFactory factory = new Reasoner.ReasonerFactory();
                                Configuration configuration=new Configuration();
                                configuration.throwInconsistentOntologyException=false;
                                OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
                                superObjectProperties = reasoner.getSuperObjectProperties(prop);
                                subObjectProperties = reasoner.getSubObjectProperties(prop);
                                propertiesInTurn.add(prop);
                                break;
                            }
                        }
                        Set<OWLAxiom> referencingAxioms4 = ontology.getReferencingAxioms((OWLPrimitive) secondSource);
                        for(OWLAxiom axiomInQues: referencingAxioms4)
                        {
                            if(axiomInQues.containsEntityInSignature((OWLEntity) secondTarget))
                            {
                                //System.out.println("Axiom containing second object");
                                //System.out.println(axiomInQues);
                                Set<OWLObjectProperty> objectPropertiesInSignature = axiomInQues.getObjectPropertiesInSignature();
                                System.out.println("Size of properties set is "+objectPropertiesInSignature.size());
                                OWLObjectProperty prop = objectPropertiesInSignature.iterator().next();
                                System.out.println("Property 2 in turn is "+prop);
                                propertiesInTurn.add(prop);
                                break;
                            }
                        }
                        OWLObjectProperty prop1 = propertiesInTurn.get(0);
                        OWLObjectProperty prop2 = propertiesInTurn.get(1);
                        if(superObjectProperties.containsEntity(prop2))
                        {
                                SemDist+=1.0;
                                System.out.println("In hierarchy");
                        }
                        else if(subObjectProperties.containsEntity(prop2))
                        {
                            SemDist+=1.0;
                            System.out.println("In hierarchy");
                        }
                        else
                        {
                            SemDist+=2.0;
                            System.out.println("Not in hierarchy");
                        }
                        System.out.println("Object Property and Object Property turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    else if(edgeWeight == k2 && edgeWeightNext == k2)
                    {
                        // Distance between properties in the property hierarchy tree
                        ObjectPropertyTurnCount++;
                        Integer edgeSource = OWLGraph.getEdgeSource(edge);
                        Integer edgeTarget = OWLGraph.getEdgeTarget(edge);
                        Integer nextEdgeTarget = OWLGraph.getEdgeTarget(nextEdgeObj);
                        OWLObjectImpl firstSource = entitiesOfGraph.get(edgeSource);
                        OWLObjectImpl secondSource = entitiesOfGraph.get(edgeTarget);
                        OWLObjectImpl secondTarget = entitiesOfGraph.get(nextEdgeTarget);
                        System.out.println("First Source is "+firstSource);
                        System.out.println("Second Source is "+secondSource);
                        System.out.println("Second Target is "+secondTarget);
                        // Get property between these source and targets
                        Set<OWLAxiom> referencingAxioms3 = ontology.getReferencingAxioms((OWLPrimitive) firstSource);
                        List<OWLObjectProperty> propertiesInTurn = new ArrayList<>();
                        for(OWLAxiom axiomInQues: referencingAxioms3)
                        {
                            if(axiomInQues.containsEntityInSignature((OWLEntity) secondSource))
                            {
                                //System.out.println("Axiom containing second object");
                                //System.out.println(axiomInQues);
                                Set<OWLObjectProperty> objectPropertiesInSignature = axiomInQues.getObjectPropertiesInSignature();
                                System.out.println("Size of properties set is "+objectPropertiesInSignature.size());
                                OWLObjectProperty prop = objectPropertiesInSignature.iterator().next();
                                System.out.println("Property 1 in turn is "+prop);
                                Reasoner.ReasonerFactory factory = new Reasoner.ReasonerFactory();
                                Configuration configuration=new Configuration();
                                configuration.throwInconsistentOntologyException=false;
                                OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
                                superObjectProperties = reasoner.getSuperObjectProperties(prop);
                                subObjectProperties = reasoner.getSubObjectProperties(prop);
                                propertiesInTurn.add(prop);
                                break;
                            }
                        }
                        Set<OWLAxiom> referencingAxioms4 = ontology.getReferencingAxioms((OWLPrimitive) secondSource);
                        for(OWLAxiom axiomInQues: referencingAxioms4)
                        {
                            if(axiomInQues.containsEntityInSignature((OWLEntity) secondTarget))
                            {
                                //System.out.println("Axiom containing second object");
                                //System.out.println(axiomInQues);
                                Set<OWLObjectProperty> objectPropertiesInSignature = axiomInQues.getObjectPropertiesInSignature();
                                System.out.println("Size of properties set is "+objectPropertiesInSignature.size());
                                OWLObjectProperty prop = objectPropertiesInSignature.iterator().next();
                                System.out.println("Property 2 in turn is "+prop);
                                propertiesInTurn.add(prop);
                                break;
                            }
                        }
                        OWLObjectProperty prop1 = propertiesInTurn.get(0);
                        OWLObjectProperty prop2 = propertiesInTurn.get(1);
                        if(superObjectProperties.containsEntity(prop2))
                        {
                                SemDist+=1.0;
                                System.out.println("In hierarchy");
                        }
                        else if(subObjectProperties.containsEntity(prop2))
                        {
                            SemDist+=1.0;
                            System.out.println("In hierarchy");
                        }
                        else
                        {
                            SemDist+=2.0;
                            System.out.println("Not in hierarchy");
                        }
                        System.out.println("Object Property and Object Property turn");
                        System.out.println("Turn between "+edge+"and "+" "+nextEdgeObj);
                    }
                    break;
                }
                
            }
            if(edgeList.size()!=1)
            {
                System.out.println("Number of object property turns " +ObjectPropertyTurnCount);
                System.out.println("Number of mix turns " +mixTurnCount);
                System.out.println("Number of taxonomical turns " +taxonomicalTurnCount);
            }
            if(taxonomicalTurnCount==(edgeList.size()-1)&&(edgeList.size()!=1))
            {
                System.out.println("Only taxonomical edges in the path");
                onlyTaxonomicalEdges = true;
                OnlyTaxonomicalEdgesPathIndexList.add(pathIter);
            }
            else if(ObjectPropertyTurnCount>taxonomicalTurnCount)
            {
                if(mixTurnCount==0&&taxonomicalTurnCount==0)
                {
                    System.out.println("Only non taxonomical edges in the path");
                    onlyTaxonomicalEdges = false;
                    onlyNonTaxonomicalEdges = true;
                }
                else
                {
                    System.out.println("Path contains both types of edges");
                    onlyTaxonomicalEdges = false;
                    onlyNonTaxonomicalEdges = false;
                }
            }
            System.out.println("SemDist is "+SemDist);
            System.out.println("Path processed is "+pathToProcess);
            if(edgeList.size()==1)
                {
                    /*if(onlyTaxonomicalEdges)
                    {
                        System.out.println("Only Path contains taxonomical edges only");
                    }
                    else if(onlyNonTaxonomicalEdges)
                    {
                        System.out.println("Only Path contains  non taxonomical edges only");
                    }
                    else
                    {
                        System.out.println("Mix of taxonomical and non taxonomical edges exist");
                    }*/
                    SemDistShort = SemDist;
                    semDistOfPaths.add(SemDist);
                    DefaultWeightedEdge edge = (DefaultWeightedEdge)edgeList.get(0);
                    double weight = OWLGraph.getEdgeWeight(edge);
                    if(weight == k3||weight ==k4)
                    {
                        onlyNonTaxonomicalEdges = true;
                    }
                    else if(weight ==k1)
                    {
                        onlyTaxonomicalEdges = true;
                    }
                    // As only one edge, this is the shortest semantic path
                    System.out.println("Stop processing for other paths");
                    numberOfShortestPaths = 1;
                    break;
                }
            // Detect up/down zigzag movements along taxonomical edges in the path
            // Check the values of SemDist and SemDistShort
            System.out.println("SemDist is "+SemDist+" SemDistShort is "+SemDistShort);
            if(SemDist<SemDistShort||SemDistShort==0.0)
            SemDistShort = SemDist;
            
            if(SemDist==SemDistShort&&SemDist!=0)
            {
                System.out.println("More than one semantic shortest paths exist");
                numberOfShortestPaths++;
            }
        System.out.println("Path got shorter, SemDistShort is "+SemDistShort);
        System.out.println("Index of shorter path is "+pathIter);
          semDistOfPaths.add(SemDist);
            //break;
            /*if(pathIter ==0)
        {
            SemDistShort = SemDist;
        }
        else if(SemDistShort>SemDist)
        {
            SemDistShort = SemDist;
        }*/
        }
        if(onlyTaxonomicalEdges)
        {
            System.out.println("Only taxonomical paths exist between source and destination");
        }
        else
        {
            if(onlyNonTaxonomicalEdges)
            {
                System.out.println("Only  non taxonomical paths exist between source and destination");
            }
            else
            {
                System.out.println("Both taxonomical and mixed paths may exist, need to prioritise");
                Double shortestPathLen = 0.0;
                int indexOfShortestPathLength = 0;
                // Need to get mixed paths as shortest paths
                for(int index = 0; index < i-1;index++)
                {
                    if(index!=i-2)
                    {
                        Double firstLen = semDistOfPaths.get(index);
                        Double secondLen = semDistOfPaths.get(index+1);
                        Double min = Double.min(firstLen, secondLen);
                        System.out.println("Minimum path length is "+min);
                        if(shortestPathLen == 0.0)
                        {
                            shortestPathLen = min;
                            indexOfShortestPathLength = index+1;
                            System.out.println("Index of shortest path is "+indexOfShortestPathLength);
                        }
                        if(min<shortestPathLen)
                        {
                            shortestPathLen=min;
                            indexOfShortestPathLength = index+1;
                            System.out.println("Index of shortest path is "+indexOfShortestPathLength);
                        }
                    }
                }
                if(OnlyTaxonomicalEdgesPathIndexList.contains(indexOfShortestPathLength))
                {
                    // Need to find the next shorter path here... Only taxonomical path might not contain more semnatic relations
                    System.out.println("Shortest semantic path contains only taxonomical edges");
                    System.out.println("Need to find next shorter path which is not purely taxonomical");
                }
                else
                {
                    System.out.println("Shortest semantic path is not a fully taxonomical path");
                    System.out.println("Shortest path length/Semantic Distance is "+shortestPathLen);
                    System.out.println("Shortest Semantic Path is "+allPathsString.get(indexOfShortestPathLength));
                }
            }
        }
        System.out.println("Number of shortest paths is "+numberOfShortestPaths);
        return SemDistShort;
    }
    
    public static int getRegularGraphFromOWL(OWLOntology ontology) throws ExportException
    {
        int success = 1;
        RegularGraph = new DefaultDirectedGraph<Integer,DefaultEdge>(DefaultEdge.class);
        Set<OWLAxiom> axioms = ontology.getAxioms(true);
        for(OWLAxiom eachAxiom : axioms)
        {
            AxiomType<? extends OWLAxiom> AxiomType = eachAxiom.getAxiomType();
            String axiomType = eachAxiom.getAxiomType().getName();
            System.out.println("Axiom "+eachAxiom+ " Type : "+axiomType);
            if(axiomType.equals("ObjectPropertyAssertion"))
            {
                OWLObjectPropertyAssertionAxiom axiomAssertion = (OWLObjectPropertyAssertionAxiom) eachAxiom;
                OWLIndividual subj = axiomAssertion.getSubject();
                OWLIndividual obj = axiomAssertion.getObject();
                OWLEntity entity1 = (OWLEntity)subj;
                OWLEntity entity2 = (OWLEntity)obj;
                int indexOfEnt1 = 0;
                int indexOfEnt2 = 0;
                System.out.println("Subject in property is "+entity1);
                System.out.println("Object in property is "+entity2);
                
                if(!entitiesOfGraph.contains(entity1))
                {
                    entitiesOfGraph.add((OWLNamedIndividualImpl) subj);
                    indexOfEnt1 = entitiesOfGraph.indexOf(entity1);
                    System.out.println("Entity "+entity1+" Index: "+indexOfEnt1);
                    RegularGraph.addVertex(indexOfEnt1);
                }
                else
                {
                    indexOfEnt1 = entitiesOfGraph.indexOf(entity1);
                    System.out.println("Entity "+entity1+" Index: "+indexOfEnt1);
                    RegularGraph.addVertex(indexOfEnt1);
                }
                if(!entitiesOfGraph.contains(entity2))
                {
                    entitiesOfGraph.add((OWLObjectImpl)entity2);
                    indexOfEnt2 = entitiesOfGraph.indexOf(entity2);
                    System.out.println("Entity "+entity2+" Index: "+indexOfEnt2);
                    RegularGraph.addVertex(indexOfEnt2);
                }
                else
                {
                    indexOfEnt2 = entitiesOfGraph.indexOf(entity2);
                    System.out.println("Entity "+entity2+" Index: "+indexOfEnt2);
                    RegularGraph.addVertex(indexOfEnt2);
                }
                OWLEdge edge = new OWLEdge(indexOfEnt1, indexOfEnt2);
                RegularGraph.addEdge(indexOfEnt1, indexOfEnt2);
                edgesOfGraph.add(edge);
                axiomTypes.add(axiomType);
            }// end of if ObjectPropertyAssertion
            
            
        } // End of for loop
            System.out.println("Regular Graph formed");
            //System.out.println(OWLGraph);
            System.out.println("Edges are:");
            System.out.println(RegularGraph.edgeSet());
            System.out.println("Vertices are:");
            System.out.println(RegularGraph.vertexSet());
            System.out.println("Entities of Graph ");
            System.out.println(entitiesOfGraph);
            CSVExporter exporter = new CSVExporter(CSVFormat.MATRIX);
                File matrixOfRegularGraph = new File("E:/Pallavi/Ontology/matrixOfRegularGraphCitationNetwork_Filtered.csv");
                exporter.exportGraph(RegularGraph, matrixOfRegularGraph);
            
            BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			
                        fw = new FileWriter("E:/Pallavi/Ontology/Entities_RegularGraph_CitationNetworkTopCitedAuthors.txt");
			//fw = new FileWriter("E:/Pallavi/Ontology/NonFiltered_Entities_RegularGraph_CitationNetworkCoAuthorAuthorOf.txt");
			bw = new BufferedWriter(fw);
			//bw.write(content);
                        bw.write(entitiesOfGraph.toString());

			System.out.println("Done writing OWL Entities to file");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
        return success;
    }
    
    public static int computeDistanceRegularGraph(int source, int destination)
    {
        double regDist = 0.0;
        int numShortestRegPaths = 0;
        int numRegPathsThruVert = 0;
        double RegBetweennessCentrality = 0.0;
        // Regular Betweenness Centrality computation
       //DirectedGraph<Integer,DefaultEdge> OWLNonWeightedGraph = toGraph.OWLNonWeightedGraph;
        int numVert = RegularGraph.vertexSet().size();
        /*System.out.println("Number of vertices in Non weighted Graph is "+numVert);
        System.out.println("Vertices of Non Weighted Graph "+RegularGraph.vertexSet());
        System.out.println("Edges of Non Weighted Graph "+RegularGraph.edgeSet());*/
        //DijkstraShortestPath shortestPathsGraph = new DijkstraShortestPath(RegularGraph);
        FloydWarshallShortestPaths shortestPathsGraph = new FloydWarshallShortestPaths(RegularGraph);
        //GraphPath path = shortestPathsGraph.getPath(source, destination);
        SingleSourcePaths paths = shortestPathsGraph.getPaths(source);
        GraphPath path1 = paths.getPath(destination);
        System.out.println("Paths "+path1);
        
        int length  = path1.getLength();
        /*if(path1!=null)
        {*/
            //List edgeList = path1.getEdgeList();
            //System.out.println("Edge List in path is "+edgeList);
           //System.out.println("Len :"+path1.getLength());
       
        System.out.println("Regular shortest distance between " + source + " and "+ destination + " is "+path1.getLength() );
        //}
        return length;
    }
}