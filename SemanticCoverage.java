/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semanticcoverage;



//import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.AutoIRIMapper;



/**
 *
 * @author Pallavi Karanth
 */
public class SemanticCoverage {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException, Exception  {
        
        String OntologyFile = new String(args[0]);
        System.out.println("The Ontology file which is preprocessed and used for computing Semantic Distance is "+OntologyFile);
        String FolderName = new String(args[1]);
        System.out.println("The folder containing ontology file is "+FolderName);
        String base = args[2];
        List<String> propertyNames = new ArrayList<>();
        //System.out.println("Number of arguments is "+args.length);
        for(int i = 3; i < args.length;i++)
        {
            propertyNames.add(args[i]);
        }
        int success = loadOntology(OntologyFile,FolderName,propertyNames,base);
        
    }
    
     public static int loadOntology(String fileName, String folderName, List<String> propertyNames, String base) throws OWLException, InstantiationException, IllegalAccessException,
        ClassNotFoundException, IOException, Exception
    {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File owlFile = new File(fileName);
        File folder = new File(folderName);
        AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
        manager.addIRIMapper(mapper);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(owlFile);
        // Report information about the ontology
        System.out.println("Ontology Loaded...");
        //System.out.println("Document IRI: " + documentIRI);
        System.out.println("Ontology : " + ontology.getOntologyID());
        System.out.println("Format      : " + manager.getOntologyFormat(ontology));
        Reasoner.ReasonerFactory factory = new Reasoner.ReasonerFactory();
         Configuration configuration=new Configuration();
         configuration.throwInconsistentOntologyException=false;
         OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
        int success = domainCoverage(ontology,manager,propertyNames,base,reasoner);
        //int successRangeCoverage = rangeCoverage(ontology,manager,propertyNames,base);
        int successClassCoverage = classCoverage(ontology,manager,base,reasoner);
        return 1;
    }
     
     public static int domainCoverage(OWLOntology ontology, OWLOntologyManager manager, List<String> propertyNames, String base, OWLReasoner reasoner)
     {
         int success = 1;
         
         OWLDataFactory dataFactory = manager.getOWLDataFactory();
         /*OWLClass  topClass = dataFactory.getOWLClass(IRI.create(base+"OWL:Thing"));
        Set<OWLClass> subClasses = reasoner.getSubClasses(topClass, false).getFlattened();*/
         Stream<OWLClass> stream = ontology.classesInSignature();
        Set<OWLClass> setClasses = ontology.classesInSignature().collect(Collectors.toSet());
        int numOfClasses = setClasses.size();
        System.out.println("Number of classes "+numOfClasses);
        List<OWLNamedIndividual> instancesOfAllClasses = new ArrayList<>();
        List<Set<OWLNamedIndividual>> setOfInstancesOfEachClass = new ArrayList<>();
        List<OWLClass> namesOfClasses = new ArrayList<>();
         for(OWLClass eachClass : setClasses)
         {
             //System.out.println("Class Name: "+eachClass);
             Set<OWLNamedIndividual> instancesOfEachClass = reasoner.getInstances(eachClass).getFlattened();
             setOfInstancesOfEachClass.add(instancesOfEachClass);
             boolean addAll = instancesOfAllClasses.addAll(instancesOfEachClass);
             namesOfClasses.add(eachClass);
         }
         int numberofInstancesTotal = instancesOfAllClasses.size();
         System.out.println("Total number of instances "+numberofInstancesTotal);
         for(String eachProp: propertyNames)
         {
             int instancesWithValueForProp = 0;
             int[] NumberOfInstancesWithValueForPropForEachClass = new int[numOfClasses];
            OWLObjectProperty prop = dataFactory.getOWLObjectProperty(IRI.create(base+eachProp));
             Set<OWLObjectPropertyExpression> subProperties = reasoner.getSubObjectProperties(prop).getFlattened();
            //System.out.println("For Property : "+prop);
            for(int index = 0; index < setOfInstancesOfEachClass.size(); index++)
            {
                 Set<OWLNamedIndividual> instancesOfClass = setOfInstancesOfEachClass.get(index);
                for(OWLNamedIndividual indiv: instancesOfClass)
                {
                    Set<OWLNamedIndividual> objectPropertyValuesForIndivForEachProp = reasoner.getObjectPropertyValues(indiv, prop).getFlattened();
                    int numberOfValues = objectPropertyValuesForIndivForEachProp.size();
                    if(numberOfValues>0)
                    {
                        instancesWithValueForProp+=1;
                        NumberOfInstancesWithValueForPropForEachClass[index]+=1;
                    }
                    else
                    {
                        // Check if the individual has value for any of the sub properties
                        for(OWLObjectPropertyExpression expr : subProperties)
                        {
                            Set<OWLNamedIndividual> objectPropertyValuesForIndivSubProperty = reasoner.getObjectPropertyValues(indiv, expr).getFlattened();
                            int numberOfValuesForSubProp = objectPropertyValuesForIndivSubProperty.size();
                            if(numberOfValuesForSubProp>0)
                            {
                               instancesWithValueForProp+=1; 
                               NumberOfInstancesWithValueForPropForEachClass[index]+=1;
                            }
                        }
                    }
                  //System.out.println("Number of Object Property Values of Individual "+indiv + " for property " + prop + " is: "+numberOfValues);
                }
                if(NumberOfInstancesWithValueForPropForEachClass[index]>0)
                {
                    OWLClass className = namesOfClasses.get(index);
                    System.out.println("Number of instances in class "+className+ " with value for property "+ prop + " is "+NumberOfInstancesWithValueForPropForEachClass[index]);
                    double ratio = (double)NumberOfInstancesWithValueForPropForEachClass[index]/numberofInstancesTotal;
                    double classDomainCoverage = ratio*100;
                    System.out.println("Class domain coverage of class "+className + " for Property " + prop + " is: "+classDomainCoverage);
                }
            }
            /*for(OWLNamedIndividual indiv: instancesOfAllClasses)
            {
                 Set<OWLNamedIndividual> objectPropertyValuesForIndivForEachProp = reasoner.getObjectPropertyValues(indiv, prop).getFlattened();
                 int numberOfValues = objectPropertyValuesForIndivForEachProp.size();
                  if(numberOfValues>0)
                  {
                    instancesWithValueForProp+=1;
                  }
                  //System.out.println("Number of Object Property Values of Individual "+indiv + " for property " + prop + " is: "+numberOfValues);
            }*/
            
            System.out.println("Number of instances with value for property "+prop+" is : "+instancesWithValueForProp);
             double ratio =  (double)instancesWithValueForProp/numberofInstancesTotal;
             //System.out.println("Ratio "+ratio);
             double domainCoverageForProp = (double)ratio*100;
            System.out.println("Domain Coverage for property "+ prop + " is "+domainCoverageForProp);
         }
         return success;
     }
     
     public static int rangeCoverage(OWLOntology ontology, OWLOntologyManager manager, List<String> propertyNames, String base)
     {
         int success = 1;
         Reasoner.ReasonerFactory factory = new Reasoner.ReasonerFactory();
         Configuration configuration=new Configuration();
         configuration.throwInconsistentOntologyException=false;
         OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
         OWLDataFactory dataFactory = manager.getOWLDataFactory();
         /*OWLClass  topClass = dataFactory.getOWLClass(IRI.create(base+"OWL:Thing"));
        Set<OWLClass> subClasses = reasoner.getSubClasses(topClass, false).getFlattened();*/
         Stream<OWLClass> stream = ontology.classesInSignature();
        Set<OWLClass> setClasses = ontology.classesInSignature().collect(Collectors.toSet());
        int numOfClasses = setClasses.size();
        System.out.println("Number of classes "+numOfClasses);
        List<OWLNamedIndividual> instancesOfAllClasses = new ArrayList<>();
        List<Set<OWLNamedIndividual>> setOfInstancesOfEachClass = new ArrayList<>();
        List<OWLClass> namesOfClasses = new ArrayList<>();
         for(OWLClass eachClass : setClasses)
         {
             //System.out.println("Class Name: "+eachClass);
             Set<OWLNamedIndividual> instancesOfEachClass = reasoner.getInstances(eachClass).getFlattened();
             setOfInstancesOfEachClass.add(instancesOfEachClass);
             boolean addAll = instancesOfAllClasses.addAll(instancesOfEachClass);
             namesOfClasses.add(eachClass);
         }
         int numberofInstancesTotal = instancesOfAllClasses.size();
         System.out.println("Total number of instances "+numberofInstancesTotal);
         for(String eachProp: propertyNames)
         {
             int instancesWithValueForProp = 0;
             int[] NumberOfInstancesWithValueForPropForEachClass = new int[numOfClasses];
            OWLObjectProperty prop = dataFactory.getOWLObjectProperty(IRI.create(base+eachProp));
             Set<OWLObjectPropertyExpression> subProperties = reasoner.getSubObjectProperties(prop).getFlattened();
            System.out.println("For Property : "+prop);
            for(int index = 0; index < setOfInstancesOfEachClass.size(); index++)
            {
                 Set<OWLNamedIndividual> instancesOfClass = setOfInstancesOfEachClass.get(index);
                for(OWLNamedIndividual indiv: instancesOfClass)
                {
                    OWLClass classOfThisInstance = namesOfClasses.get(index);
                    System.out.println("Individual "+indiv+ " Class "+classOfThisInstance);
                    Set<OWLClass> objectPropertyRanges = reasoner.getObjectPropertyRanges(prop).getFlattened();
                    for(OWLClass eachRangeClass : objectPropertyRanges)
                    {
                        System.out.println("Property "+prop + " Range "+eachRangeClass);
                        if(!eachRangeClass.isOWLThing())
                        System.out.println("ObjectPropertyRanges " + " of Property "+ prop + " are "+eachRangeClass );
                        break;
                    }
                }
                if(NumberOfInstancesWithValueForPropForEachClass[index]>0)
                {
                    OWLClass className = namesOfClasses.get(index);
                    System.out.println("Number of instances in class "+className+ " with value for property "+ prop + " is "+NumberOfInstancesWithValueForPropForEachClass[index]);
                    double ratio = (double)NumberOfInstancesWithValueForPropForEachClass[index]/numberofInstancesTotal;
                    double classDomainCoverage = ratio*100;
                    System.out.println("Class domain coverage of class "+className + " for Property " + prop + " is: "+classDomainCoverage);
                }
            }
            
            System.out.println("Number of instances with value for property "+prop+" is : "+instancesWithValueForProp);
             double ratio =  (double)instancesWithValueForProp/numberofInstancesTotal;
             //System.out.println("Ratio "+ratio);
             double rangeCoverageForProp = (double)ratio*100;
            System.out.println("Domain Coverage for property "+ prop + " is "+rangeCoverageForProp);
         }
         return success;
     }
     
     public static int classCoverage(OWLOntology ontology, OWLOntologyManager manager, String base, OWLReasoner reasoner)
     {
         int success = 1;
         OWLDataFactory dataFactory = manager.getOWLDataFactory();
         /*OWLClass  topClass = dataFactory.getOWLClass(IRI.create(base+"OWL:Thing"));
        Set<OWLClass> subClasses = reasoner.getSubClasses(topClass, false).getFlattened();*/
         Stream<OWLClass> stream = ontology.classesInSignature();
        Set<OWLClass> setClasses = ontology.classesInSignature().collect(Collectors.toSet());
        int numOfClasses = setClasses.size();
        System.out.println("Number of classes "+numOfClasses);
        List<OWLNamedIndividual> instancesOfAllClasses = new ArrayList<>();
        List<Set<OWLNamedIndividual>> setOfInstancesOfEachClass = new ArrayList<>();
        List<Set<OWLClass>> subClassesOfEachClass = new ArrayList<>();
        List<OWLClass> namesOfClasses = new ArrayList<>();
         for(OWLClass eachClass : setClasses)
         {
             //System.out.println("Class Name: "+eachClass);
             Set<OWLNamedIndividual> instancesOfEachClass = reasoner.getInstances(eachClass).getFlattened();
             int numberOfInstancesOfThisParentClass = instancesOfEachClass.size();
             setOfInstancesOfEachClass.add(instancesOfEachClass);
             
             boolean addAll = instancesOfAllClasses.addAll(instancesOfEachClass);
             namesOfClasses.add(eachClass);
             
             Set<OWLClass> subClassesOfThisClass = reasoner.getSubClasses(eachClass).getFlattened();
             for(OWLClass eachSubClass : subClassesOfThisClass)
             {
                 int numberOfInstancesOfThisSubClass = reasoner.getInstances(eachSubClass).getFlattened().size();
                 double ratio = (double)numberOfInstancesOfThisSubClass/numberOfInstancesOfThisParentClass;
                 double hierarchicalClassCoverage = (double)ratio * 100;
                 System.out.println("Hierarchical Class Coverage of class "+eachSubClass.getIRI().getShortForm().toString()+" is "+hierarchicalClassCoverage);
             }
             subClassesOfEachClass.add(subClassesOfThisClass);
         }
         int numberofInstancesTotal = instancesOfAllClasses.size();
         System.out.println("Total number of instances "+numberofInstancesTotal);
         int index = 0;
         for(Set<OWLNamedIndividual> eachSetOfInstances : setOfInstancesOfEachClass)
         {
             int numberOfInstancesOfThisClass = eachSetOfInstances.size();
             double ratio = (double)numberOfInstancesOfThisClass/numberofInstancesTotal;
             double classCoverage = (double)ratio*100;
             OWLClass thisClass = namesOfClasses.get(index);
             if(!thisClass.isOWLThing())
             System.out.println("Class Coverage of class "+thisClass + " is "+classCoverage);
             index++;
         }
         // Compute Hierarchical Class Coverage
         /*for(Set<OWLClass> eachSubClassOfClass : subClassesOfEachClass)
         {
             for(OWLClass eachSubClass: eachSubClassOfClass)
             {
                 int numberOfInstancesPerSubClass = reasoner.getInstances(eachSubClass).getFlattened().size();
                 
             }
         }*/
         
         return success;
     }
    
}
