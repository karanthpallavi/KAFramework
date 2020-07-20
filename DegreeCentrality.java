/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.semanticdegreecentrality;

import com.clarkparsia.pellet.utils.CollectionUtils;
import com.google.common.base.Optional;
import com.mycompany.preprocessontology.LoadOWLFile;
//import com.mycompany.semanticdegreecentrality.MultiSelectionDemo;
import com.mycompany.semanticdegreecentrality.SelectProperties;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JFrame;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectInverseOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.util.AutoIRIMapper;
/*import openllet.owlapi.OWL;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;*/
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 *
 * @author Pallavi Karanth
 */
public class DegreeCentrality {
    // Need to clear all these for each indiv or class - to be done
    public static Set<OWLObjectPropertyDomainAxiom> subGraphDomainAxioms = new HashSet<>();
        public static Set<OWLObjectPropertyRangeAxiom> subGraphRangeAxioms = new HashSet<>();
        public static Set<OWLAxiom> referencingAxioms = new HashSet<>();
        public static Set<OWLAxiom> referencingAxiomsIndiv = new HashSet<>();
        public static Set<OWLAxiom> referencingAxioms1 = new HashSet<>();
        public static Set<OWLAxiom> referencingAxioms2 = new HashSet<>();
        public static Set<OWLAxiom> referencingAxiomsFiltered = new HashSet<>();
        public static Set<OWLAxiom> newIndivAxioms = new HashSet<>();
        public static HashMap<Integer,Integer> propsInHierarchy = new HashMap<>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws OWLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, ClassCastException  {
        
        String OntologyFile = new String(args[0]);
        System.out.println("The Ontology file which is preprocessed and used for computing Semantic Degree Centrality is "+OntologyFile);
        String FolderName = new String(args[1]);
        System.out.println("The folder containing ontology file is "+FolderName);
        String inferredfileName = args[2];
        System.out.println("Inferred Ontology is saved at path "+inferredfileName);
        
        String inferredfileTriples = args[3];
        System.out.println("Inferred Ontology in triples format is saved at path "+inferredfileTriples);
        String considerTaxonomicRelations = args[4];
        
        //LoadOWLFile loadObject = new LoadOWLFile();
        LoadOWLFile.inferOntology(OntologyFile,FolderName,inferredfileName,inferredfileTriples);
        // inferredfileTriples contains the inferred ontology after preprocessing
        // Perform Semantic Filtering for all individual class nodes in the graph
        // Get all class nodes in the Ontology
        //LoadOWLFile.loadOntology(inferredfileName,FolderName);
        File owlFile = new File(inferredfileName);
        File folder = new File(FolderName);
        
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
        manager.addIRIMapper(mapper);
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(owlFile);
        
        // Report information about the ontology
        System.out.println("Ontology Loaded...");
        
        // Get all classes in inferred ontology
        Set<OWLClass> classesInOntology = ontology.getClassesInSignature();
         // For each class, compute DegreeCentrality 
         
         // For individuals, compute degree centrality
         Iterator ClassIter = classesInOntology.iterator();
         Configuration configuration=new Configuration();
            configuration.throwInconsistentOntologyException=false;
            ReasonerFactory reasonerfactory = new ReasonerFactory();
            OWLReasoner reasoner=reasonerfactory.createReasoner(ontology, configuration);
            
         // Find Degree Centrality of all class nodes - commented for now
         /*while(ClassIter.hasNext())
         {
             OWLClass className = (OWLClass) ClassIter.next();
             //System.out.println(className.getIRI().getShortForm());
             int success = SemanticDegreeCentrality(className,ontology,manager);
         }*/
         //IRI owlClassIRI = new IRI("http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#FoodOrDrink");
         // Degree Centrality of just one class node for testing
         int success = 0;
         for(OWLClass className: classesInOntology)
         {
             // Get individuals of this class to find degree centrality of these individuals
             Set<OWLNamedIndividual> indivOfClass = reasoner.getInstances(className, false).getFlattened();
             
             //if  (className.getIRI().getFragment().equals("CitrusFruits"))
             //if  (className.getIRI().getFragment().equals("Kuru_Dynasty"))
             //if  (className.getIRI().getFragment().equals("Person"))
             if  (className.getIRI().getFragment().equals("Author"))
             {
                 if(!considerTaxonomicRelations.contains("1"))
                {
                    success = SemanticDegreeCentrality(className,ontology,manager,0); 
                } 
                 else 
                 {
                     success = SemanticDegreeCentrality(className,ontology,manager,1); 
                }
                 // Compute degree centrality for individuals
                 for(OWLNamedIndividual indiv: indivOfClass)
                 {
                     if(!considerTaxonomicRelations.contains("1"))
                    {
                        success = SemanticDegreeCentrality(indiv,ontology,manager,0);
                    }
                     else
                     {
                         success = SemanticDegreeCentrality(indiv,ontology,manager,1);
                     }
                 }
                 
             }
         }
         // Degree Centrality of just one individual for testing
         
         //OWLClass className = (OWLClass) ontology.containsClassInSignature(owlClassIRI);
         /*while(ClassIter.hasNext())
         {
             OWLClass className = (OWLClass) ClassIter.next();
             int success = SemanticDegreeCentrality(className,ontology,manager);  
         }*/
         
    }
    
    public static int SemanticDegreeCentrality(OWLNamedIndividual individual, OWLOntology ontology,OWLOntologyManager manager, int considerTaxonomicRelations) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException
    {
        //int succFilter = SemanticFiltering(individual,ontology,manager);
        List<String> propertiesForFiltering = SemanticFiltering(individual,ontology,manager); 
        System.out.println("Call to Semantic Degree Centrality for individual "+individual.getIRI().getShortForm().toString());
        // Count indegree and outdegree
        int semanticIndegreeCentrality = 0;
        int semanticOutdegreeCentrality = 0;
        // Get Object Property Assertions
        if(considerTaxonomicRelations!=1)
        {
            // Dont consider Taxonomic Relations
            
        } else {
        
        // Get all ancestor classes for individual and count for type relations outdegree
        Configuration configuration=new Configuration();
            configuration.throwInconsistentOntologyException=false;
            ReasonerFactory reasonerfactory = new ReasonerFactory();
            OWLReasoner reasoner=reasonerfactory.createReasoner(ontology, configuration);
            int numberOfClassesAsAncestorsForIndiv = reasoner.getTypes(individual, false).getFlattened().size();
            System.out.println(numberOfClassesAsAncestorsForIndiv + "number of ancestors");
                    semanticOutdegreeCentrality = semanticOutdegreeCentrality + numberOfClassesAsAncestorsForIndiv;
                    // Indegree is just for link between parent and individual
                semanticIndegreeCentrality+=1;    

        } //  End of Consider taxonomic relations
        
        //Iterator axiomsIter = newIndivAxioms.iterator();
         Iterator PropStringIter = propertiesForFiltering.iterator();
        OWLDataFactory datafactory = manager.getOWLDataFactory();
        //Optional<IRI> ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        //String ontologyIRI = "http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#";
        //String ontologyIRI = "http://www.semanticweb.org/vinu/ontologies/2014/3/untitled-ontology-64#";
        String ontologyIRI = "http://www.semanticweb.org/drkm/ontologies/2018/1/untitled-ontology-104#";;
        int outdegreeForIndivPropAssertions = 0;
        int indegreeFromIndivPropAssertions = 0;
        System.out.println("Axiom of individual "+individual);
        List<OWLObjectPropertyExpression> equivalentObjProps = null;
        Node<OWLObjectPropertyExpression> equivalentObjectProperties;
        ReasonerFactory factory = new ReasonerFactory();
        Configuration configuration=new Configuration();
        configuration.throwInconsistentOntologyException=false;
        OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
        
        for(OWLAxiom axiomInQues: newIndivAxioms)
        {
            if(axiomInQues.getAxiomType().toString().contains("ObjectPropertyAssertion"))
            {
                OWLObjectProperty property = (OWLObjectProperty)axiomInQues.getObjectPropertiesInSignature().iterator().next();
                //OWLObjectProperty namedProperty = expr.getNamedProperty();
                String toString = property.toString();
                int length = toString.length();
                int indexOf = toString.indexOf("#");
                String propName = toString.substring(indexOf+1, length-1);
                //System.out.println("Need to count for property "+propName+" ??");
                OWLObjectPropertyAssertionAxiom axiom = (OWLObjectPropertyAssertionAxiom) axiomInQues;
                OWLIndividual subject = axiom.getSubject();
                //System.out.println("Subject is "+subject);
                
                if(individual.equals(subject))
                {
                    outdegreeForIndivPropAssertions++;
                }
                OWLIndividual object = axiom.getObject();
                //System.out.println("Object is "+object);
                if(individual.equals(object))
                {
                    indegreeFromIndivPropAssertions++;
                }
                
            }
        }
        
        /*List<OWLObjectPropertyExpression> objPropsForFiltering = new ArrayList<>();
        NodeSet<OWLObjectPropertyExpression> subObjectPropertiesSet = null;
        Set<OWLObjectPropertyExpression> subObjectProperties = null;
        Set<OWLObjectPropertyExpression> superObjectProperties = null;
        NodeSet<OWLObjectPropertyExpression> superObjectPropertiesSet = null;
        for(String OWLObjPropInString:propertiesForFiltering )
        {   
                    OWLObjectProperty objProp = datafactory.getOWLObjectProperty(IRI.create(ontologyIRI + OWLObjPropInString));
                    //equivalentObjectProperties = reasoner.getEquivalentObjectProperties(objProp);
                    //objPropsForFiltering.add(objProp);
                    subObjectPropertiesSet = reasoner.getSubObjectProperties(objProp, true);
                    subObjectProperties = subObjectPropertiesSet.getFlattened();
                    superObjectPropertiesSet = reasoner.getSuperObjectProperties(objProp, true);
                    superObjectProperties = superObjectPropertiesSet.getFlattened();
                    //equivalentObjProps.addAll((Collection<? extends OWLObjectPropertyExpression>) equivalentObjectProperties);
                    //objPropsForFiltering.addAll(subObjectProperties);  
        }
        Iterator iterator = subObjectProperties.iterator();
        while(iterator.hasNext())
        {
            System.out.println("Sub Properties Added");
            OWLObjectPropertyExpression next = (OWLObjectPropertyExpression) iterator.next();
            System.out.println(next);
            
            
            if(next.isOWLBottomObjectProperty()||next.isOWLTopObjectProperty())
            {
                continue;
            }
            else
            {
                OWLObjectPropertyExpression inverseProperty = next.getInverseProperty();
                //OWLObjectInverseOf invOf = (OWLObjectInverseOf) inverseProperty;
                //OWLObjectPropertyExpression inverse = invOf.getInverse();
                //System.out.println("Inverse "+inverse);
                OWLObjectPropertyExpression actualProp = inverseProperty.getNamedProperty();
                OWLObjectPropertyExpression actualInverse = actualProp.getInverseProperty();
                System.out.println("Actual property: "+ actualInverse);
                System.out.println("Inverse:" +inverseProperty);
                if(objPropsForFiltering.contains(inverseProperty))
                {
                    continue;
                }
                else
                {
                    objPropsForFiltering.add((OWLObjectPropertyExpression) next);
                }
            }
        }
        iterator = superObjectProperties.iterator();
        while(iterator.hasNext())
        {
            System.out.println("Super Properties Added");
            OWLObjectPropertyExpression next = (OWLObjectPropertyExpression) iterator.next();
            System.out.println(next);
            if(next.isOWLBottomObjectProperty()||next.isOWLTopObjectProperty())
            {
                continue;
            }
            else
            {
                OWLObjectPropertyExpression inverseProperty = next.getInverseProperty();
                //OWLObjectInverseOf invOf = (OWLObjectInverseOf) inverseProperty;
                //OWLObjectPropertyExpression inverse = invOf.getInverse();
                //System.out.println("Inverse "+inverse);
                OWLObjectPropertyExpression actualProp = inverseProperty.getNamedProperty();
                OWLObjectPropertyExpression actualInverse = actualProp.getInverseProperty();
                System.out.println("Actual property: "+ actualInverse);
                System.out.println("Inverse:" +inverseProperty);
                if(objPropsForFiltering.contains(inverseProperty))
                {
                    continue;
                }
                else
                {
                    objPropsForFiltering.add((OWLObjectPropertyExpression) next);
                }
            }
        }
        System.out.println("Sub and Super properties ");
        System.out.println(objPropsForFiltering);
        while(axiomsIter.hasNext())
        {
            OWLAxiom axiom = (OWLAxiom) axiomsIter.next();
            HashMap<OWLIndividual,Boolean>  propValues = new HashMap<OWLIndividual,Boolean>();
            if(axiom.getAxiomType().getName().contains("ObjectPropertyAssertion"))
            {
                System.out.println(axiom);
                
                //Node<OWLObjectPropertyExpression> equivalentObjectProperties;
                for(OWLObjectPropertyExpression OWLObjProp:objPropsForFiltering )
                {
                    
                    //OWLObjectProperty objProp = datafactory.getOWLObjectProperty(IRI.create(ontologyIRI + OWLObjPropInString));
                    /*equivalentObjectProperties = reasoner.getEquivalentObjectProperties(objProp);
                    NodeSet<OWLObjectPropertyExpression> subObjectProperties = reasoner.getSubObjectProperties(objProp, true);
                    NodeSet<OWLObjectPropertyExpression> superObjectProperties = reasoner.getSuperObjectProperties(objProp, true);
                    equivalentObjProps.addAll((Collection<? extends OWLObjectPropertyExpression>) equivalentObjectProperties);*/
                    /*Set<OWLObjectProperty> objectPropertiesInSignature = axiom.getObjectPropertiesInSignature();
                    if(axiom.getObjectPropertiesInSignature().contains(OWLObjProp))
                    {
                        System.out.println("----------------------------------------------------------------");
                        System.out.println("Property assertion for "+ individual+"contains "+OWLObjProp);
                        OWLObjectPropertyAssertionAxiom axiomAssertion = (OWLObjectPropertyAssertionAxiom) axiom;
                        OWLIndividual object = axiomAssertion.getObject();
                        // Check for sub or super object property assertions with same value as object
                        
                        System.out.println("Object value for property is"+object);
                        if(!propValues.containsKey(object))
                        {
                            propValues.put(object, Boolean.TRUE);
                            System.out.println("Adding degree because of property assertion");
                            outdegreeForIndivPropAssertions++;
                        }
                        else
                        {
                            System.out.println("Not adding degree again");
                        }
                        
                        // Get object value in this axiom
                        // Store object value and boolean value - key value in a map?
                        // Check if this value exists in map
                        // if so, is it true, If true, dont add
                        // If not true, add to degree and make it true
                        // if value not in map, add to map and add boolean true
                        //outdegreeForIndivPropAssertions++;
                    }
                    else
                    {
                        System.out.println("Not in filtered properties list");
                        // Dont count for degree as its not in filtered properties
                        //outdegreeForIndivPropAssertions++;
                    }
                }
                //if(axiom.getObjectPropertiesInSignature().)
            }
        }
        /*while(PropStringIter.hasNext())
        {
            String property = PropStringIter.next().toString();
            //System.out.println(property);
            
            //System.out.println(ontologyIRI);
            OWLObjectProperty objProp = datafactory.getOWLObjectProperty(IRI.create(ontologyIRI + property));
            while(axiomsIter.hasNext())
            {
                OWLAxiom axiom = (OWLAxiom) axiomsIter.next();
                //Object typeAxiom = new Object("ObjectPropertyRange");
                if(axiom.getAxiomType().getName().contains("ObjectPropertyAssertion"))
                {
                    if(axiom.getObjectPropertiesInSignature().contains(objProp))
                    {
                        System.out.println("Property Assertion of individual");
                        System.out.println("Property is "+axiom);
                        outdegreeForIndivPropAssertions++;
                    }
                }
            }
        }*/
        System.out.println("Outdegree from prop assertions " +outdegreeForIndivPropAssertions);
        System.out.println("Outdegree from ancestors "+semanticOutdegreeCentrality);
        semanticOutdegreeCentrality = semanticOutdegreeCentrality + outdegreeForIndivPropAssertions;
        System.out.println("Indegree from individual property assertions "+indegreeFromIndivPropAssertions);
        semanticIndegreeCentrality = semanticIndegreeCentrality + indegreeFromIndivPropAssertions;
        System.out.println("Semantic Indegree Centrality for "+individual.getIRI().getShortForm().toString()+" is "+semanticIndegreeCentrality);
        System.out.println("Semantic Outdegree Centrality for "+individual.getIRI().getShortForm().toString()+" is "+semanticOutdegreeCentrality);
        return 1;
    }
    
    public static int SemanticDegreeCentrality(OWLClass className, OWLOntology ontology,OWLOntologyManager manager, int considerTaxonomicRelations) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException
    {
        System.out.println("Call to SemanticDegreeCentrality for class "+className.getIRI().toString());
        // Perform Semantic Filtering - How to take PropertiesToConsider input?
        int successFiltering = SemanticFiltering(className, ontology,manager);
        OWLDataFactory factory = manager.getOWLDataFactory();
        int semanticIndegreeCentrality = 0;
        int semanticOutdegreeCentrality = 0;
        // Graph is filtered, axioms contain filtered graph
        // Calculate degree centrality
        if(considerTaxonomicRelations!=1)
        {
            // Dont consider Taxonomic Relations
            
        } else {
            // Consider taxonomic relations
            // Consider rdfs:subClassOf links
            // For indegree centrality, count rdfs:subclassof axioms from other subclasses to this class
            Set<OWLSubClassOfAxiom> subClassOfAxiomsOfClass =  ontology.getSubClassAxiomsForSuperClass(className);
            System.out.println("Subclasses for indegree "+subClassOfAxiomsOfClass.size());
            semanticIndegreeCentrality = semanticIndegreeCentrality + subClassOfAxiomsOfClass.size();
            Configuration configuration=new Configuration();
            configuration.throwInconsistentOntologyException=false;
            ReasonerFactory reasonerfactory = new ReasonerFactory();
            OWLReasoner reasoner=reasonerfactory.createReasoner(ontology, configuration);
            // Add the individuals of this class
            int numberOfIndividuals = reasoner.getInstances(className, false).getFlattened().size();
            System.out.println("Individuals count "+numberOfIndividuals);
            semanticIndegreeCentrality = semanticIndegreeCentrality + numberOfIndividuals;
            System.out.println(semanticIndegreeCentrality);
            //Set<OWLSubClassOfAxiom> superClassAxiomsOfClass = ontology.getSubClassAxiomsForSubClass(className);
            
            NodeSet<OWLClass> supClses = reasoner.getSuperClasses(className,false);
            int numberOfAncestors = supClses.getFlattened().size();
            System.out.println("Superclasses for outdegree "+numberOfAncestors);
            semanticOutdegreeCentrality = semanticOutdegreeCentrality + numberOfAncestors;
            System.out.println(semanticOutdegreeCentrality);
        }
        
        // Find outdegree if class is present in the subGraphDomainAxioms
        // Find indegree if class is present in the subGraphRangeAxioms
        if(subGraphDomainAxioms.contains(className))
        {
            System.out.println("Contains class in domain axioms");
        }
        if(subGraphRangeAxioms.contains(className))
        {
            System.out.println("Contains class in range axioms");
        }
        
        // For chosen properties, based on the class is in range or domain, add to the in/out degree
        // In subGraphDomainAxioms, axioms where class is domain of a property are present, hence the size of subGraphDomainAxioms gives the outdegree to be added for the properties
        // subGraphRangeAxioms - for indegree
        //System.out.println("Size of range axioms "+subGraphRangeAxioms.size());
        /*System.out.println("Size of domain axioms "+subGraphDomainAxioms.size());
        //semanticIndegreeCentrality = semanticIndegreeCentrality + subGraphRangeAxioms.size();*/
        
        // For indegree, we need to get those properties where the class is in range and then calculate
        Iterator axiomsIter = referencingAxioms1.iterator();
        int indegreeForClassByRangeOfProp = 0;
        int outdegreeForClassByDomainOfProp = 0;
        while(axiomsIter.hasNext())
        {
            OWLAxiom axiom = (OWLAxiom) axiomsIter.next();
            //Object typeAxiom = new Object("ObjectPropertyRange");
            if(axiom.getAxiomType().getName().contains("ObjectPropertyRange"))
            {
                if(axiom.getClassesInSignature().contains(className))
                indegreeForClassByRangeOfProp++;
            }
            else if(axiom.getAxiomType().getName().contains("ObjectPropertyDomain"))
            {
                if(axiom.getClassesInSignature().contains(className))
                    outdegreeForClassByDomainOfProp++;
            }
        }
        /*System.out.println("For debugging - print domain axioms");
        System.out.println(subGraphDomainAxioms);*/
        semanticOutdegreeCentrality = semanticOutdegreeCentrality + outdegreeForClassByDomainOfProp;
        semanticIndegreeCentrality = semanticIndegreeCentrality + indegreeForClassByRangeOfProp;
        System.out.println("Indegree Centrality for class " + className + " is " + semanticIndegreeCentrality);
        System.out.println("Outdegree Centrality for class " + className + " is " + semanticOutdegreeCentrality);
        return 1;
        
    }
    
    public static List<String> SemanticFiltering(OWLNamedIndividual indiv, OWLOntology ontology, OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException
    {
        System.out.println("Call to Semantic Filtering for individual "+indiv.getIRI().toString());
        Vector propNames = new Vector();
        
        Set<OWLObjectPropertyAssertionAxiom> objPropAxioms = ontology.getObjectPropertyAssertionAxioms(indiv);
        //Set<OWLObjectProperty> objPropsOfIndiv = indiv.getObjectPropertiesInSignature();
        //System.out.println("Number of properties for "+indiv.toString()+" are "+objPropAxioms.size());
        for(OWLObjectPropertyAssertionAxiom prop : objPropAxioms)
        {
            Set<OWLObjectProperty> objPropsOfIndiv = prop.getObjectPropertiesInSignature();
            //System.out.println("Number of properties in axiom is "+objPropsOfIndiv.size());
            String propName = objPropsOfIndiv.iterator().next().getIRI().getShortForm();
            //System.out.println("Property considered for " + indiv.toString() + " is " +propName);
            propNames.add(propName);
        }
        //System.out.println("Number of properties for this individual"+indiv+"is "+propNames.size());
        String[] propForSelection = new String[propNames.size()];
        Iterator propNamesIter = propNames.iterator();
        int i = 0;
        while(propNamesIter.hasNext())
        {
            String prop = propNamesIter.next().toString();
            //System.out.println(prop);
            propForSelection[i] = prop;
            i++;
        }
        List<String> propertiesForFiltering = new ArrayList<>();
        //propertiesForFiltering.add("hasGuru");
        //propertiesForFiltering.add("hasDisciple");
        /*propertiesForFiltering.add("hasParent");
        propertiesForFiltering.add("hasWife");
        propertiesForFiltering.add("hasHusband");
        propertiesForFiltering.add("hasSibling");
        propertiesForFiltering.add("isParentOf");
        propertiesForFiltering.add("hasDaughter");*/
        propertiesForFiltering.add("isAuthorOf");
        Iterator PropStringIter = propertiesForFiltering.iterator();
        OWLDataFactory datafactory = manager.getOWLDataFactory();
        List<OWLObjectProperty> listOfFilteredProps = new ArrayList<>();
        ReasonerFactory factory = new ReasonerFactory();
        Configuration configuration=new Configuration();
        configuration.throwInconsistentOntologyException=false;
        OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
        NodeSet<OWLObjectPropertyExpression> subObjectProperties = null;
        NodeSet<OWLObjectPropertyExpression> superObjectProperties = null;
        HashMap<OWLObjectProperty,Set<OWLObjectPropertyExpression>> listSubProperties = new HashMap<>();
        HashMap<OWLObjectProperty,Set<OWLObjectPropertyExpression>> listSuperProperties = new HashMap<>();
        //List<Set<OWLObjectPropertyExpression>> listSubProperties = new ArrayList<>();
        //List<Set<OWLObjectPropertyExpression>> listSuperProperties = new ArrayList<>();
        while(PropStringIter.hasNext())
        {
            
            //String ontologyIRI = "http://www.semanticweb.org/vinu/ontologies/2014/3/untitled-ontology-64#";
            String ontologyIRI = "http://www.semanticweb.org/drkm/ontologies/2018/1/untitled-ontology-104#";
            String property = PropStringIter.next().toString();
            OWLObjectProperty objProp = datafactory.getOWLObjectProperty(IRI.create(ontologyIRI + property));
            listOfFilteredProps.add(objProp);
            subObjectProperties = reasoner.getSubObjectProperties(objProp, true);
            Set<OWLObjectPropertyExpression> subProp = subObjectProperties.getFlattened();
            superObjectProperties = reasoner.getSuperObjectProperties(objProp, true);
            Set<OWLObjectPropertyExpression> superProp = superObjectProperties.getFlattened();
            listSubProperties.put(objProp, subProp);
            listSuperProperties.put(objProp, superProp);
        }
        //System.out.println("Sub and Super Properties");
        //System.out.println(listSubProperties);
        //System.out.println(listSuperProperties);
        
        //CreateGUIShowProperties(propForSelection);
        // No need to ancestral properties as only properties asserted for this individual are considered, 
        // Get axioms of individual
        referencingAxioms2 = ontology.getReferencingAxioms(indiv);
        Iterator iter = referencingAxioms2.iterator();
        int index = 0;
        List<Integer> indexOfAxiomsTOBeDeleted = new ArrayList<Integer>();
        while(iter.hasNext())
        {
            OWLAxiom axiom = (OWLAxiom) iter.next();
            //System.out.println(axiom.toString());
            //System.out.println(axiom.getAxiomType());
            if(axiom.getAxiomType().toString().contains("ObjectPropertyAssertion"))
            {
                //System.out.println("ObjectPropertyAssertion found");
                OWLObjectPropertyAssertionAxiom axiomAssertion = (OWLObjectPropertyAssertionAxiom) axiom;
                OWLObjectPropertyExpression property = axiomAssertion.getProperty();
                //Set<OWLObjectPropertyExpression> get = listSubProperties.get(property);
                //System.out.println("Size is "+get.size());
                //Set<OWLObjectPropertyExpression> superProp = listSuperProperties.get(property);
                //System.out.println("Property is "+property);
                if(listOfFilteredProps.contains((OWLObjectProperty)property))
                {
                    //System.out.println("Axiom " + axiom + " Contains "+property);
                    // Need to check if other axioms have its sub or super property assertions for same value
                    OWLIndividual object = axiomAssertion.getObject();
                    int indexOfRef = 0;
                    for(OWLAxiom axiomInQues:referencingAxioms2)
                    {
                        if(axiomInQues.getAxiomType().toString().contains("ObjectPropertyAssertion"))
                        {
                             OWLObjectPropertyAssertionAxiom propAxiom = (OWLObjectPropertyAssertionAxiom)axiomInQues;
                             //System.out.println(propAxiom.toString());
                            OWLObjectPropertyExpression property1 = propAxiom.getProperty();
                            if(property1.equals(property))
                            {
                                OWLIndividual object1 = propAxiom.getObject();
                                //OWLObjectPropertyExpression prop = propAxiom.getProperty();
                                if(object.equals(object1))
                                {
                                    if(index!=indexOfRef)
                                    {
                                        if(axiom.equalsIgnoreAnnotations(propAxiom))
                                        {
                                         //System.out.println("Same axioms, ignore");
                                        }else
                                        {
                                            /*System.out.println("Object is same, axiom different");
                                        System.out.println("Axioms are ");
                                        System.out.println(propAxiom);
                                        System.out.println(axiom);*/
                                        }
                                        // Prop and Obj are same, but different axioms
                                    }
                                    else
                                    {
                                        //System.out.println("Same axiom, ignore");
                                    }
                                    
                                }
                                
                            }
                            else
                            {
                                OWLIndividual object1 = propAxiom.getObject();
                                 OWLIndividual subject1 = propAxiom.getSubject();
                                 OWLIndividual subject = axiomAssertion.getSubject();
                                Set<OWLObjectPropertyExpression> subProps = listSubProperties.get((OWLObjectProperty)property);
                                    if(subProps.contains(property1))
                                    {
                                        //System.out.println("In sub property list: Check for object values");
                                        //System.out.println("Objects are "+object + "and "+object1);
                                        if(object.equals(object1)&&subject.equals(subject1)){
                                         if(!indexOfAxiomsTOBeDeleted.contains(indexOfRef))
                                         {
                                             indexOfAxiomsTOBeDeleted.add(indexOfRef); 
                                             //System.out.println("Axiom to be deleted "+propAxiom+" at index "+indexOfRef);
                                         }
                                             
                                        }
                                    }
                                    else
                                    {
                                        Set<OWLObjectPropertyExpression> superProps = listSuperProperties.get((OWLObjectProperty)property);
                                        if(superProps.contains(property1))
                                        {
                                            //System.out.println("In super property list: Check for object values");
                                            //System.out.println("Objects are "+object + "and "+object1);
                                        if(object.equals(object1)&&subject.equals(subject1)){
                                         if(!indexOfAxiomsTOBeDeleted.contains(indexOfRef))
                                         {
                                             indexOfAxiomsTOBeDeleted.add(indexOfRef);   
                                             //System.out.println("Axiom to be deleted "+propAxiom+" at index "+indexOfRef);
                                         }
                                           
                                        }
                                        }
                                    }
                            }
                        }
                        indexOfRef++;
                    }
                    
                }
                else
                {
                    // This is ObjectPropertyAssertion axiom, but does not contain filtered properties
                    // Need to check for its sub or super properties
                    /*Set<OWLObjectPropertyExpression> get = listSubProperties.get(property);
                    System.out.println("Sub Properties of "+property+" ");
                    try{
                    System.out.println(get);
                    if(get.isEmpty())
                    {*/
                        // Retrieve sub and super properties of this property and 
                        // find if its sub or super property of any of filtered properties
                        NodeSet<OWLObjectPropertyExpression> subObjectProperties1 = reasoner.getSubObjectProperties(property, true);
                        NodeSet<OWLObjectPropertyExpression> superObjectProperties1 = reasoner.getSuperObjectProperties(property, true);
                        Set<OWLObjectPropertyExpression> flattened = subObjectProperties1.getFlattened();
                        Set<OWLObjectPropertyExpression> flattened1 = superObjectProperties1.getFlattened();
                        //System.out.println("Sub Object Properties " +flattened);
                        //System.out.println("Super Object Properties " +flattened1);
                        int indexOfFilteredMainProp = 0;
                        boolean Found = false;
                        for(OWLObjectProperty objProp: listOfFilteredProps)
                        {
                            if(flattened.contains(objProp))
                            {
                                
                                //System.out.println("Filtered Property "+objProp + " contains "+property + " in sub properties list");
                                // Add property to propertiesForFiltering
                                String propString = property.toString();
                                //System.out.println("Property to be added " +propString);
                                int length = propString.length();
                                int indexOf = propString.indexOf("#");
                                String subStrPropName = propString.substring(indexOf+1,length-1);
                                //System.out.println("SubString Prop Name is "+subStrPropName);
                                if(!propertiesForFiltering.contains(subStrPropName))
                                {
                                    propertiesForFiltering.add(subStrPropName);
                                    int indexOfProp = propertiesForFiltering.indexOf(subStrPropName);
                                    propsInHierarchy.put(indexOfFilteredMainProp, indexOfProp);
                                }
                                Found = true;
                                break;
                            }
                            else if(flattened1.contains(objProp))
                            {
                                //System.out.println("Filtered Property "+objProp + " contains "+property + " in super properties list");
                                String propString = property.toString();
                                //System.out.println("Property to be added " +propString);
                                int length = propString.length();
                                int indexOf = propString.indexOf("#");
                                String subStrPropName = propString.substring(indexOf+1,length-1);
                                //System.out.println("SubString Prop Name is "+subStrPropName);
                                if(!propertiesForFiltering.contains(subStrPropName))
                                {
                                    propertiesForFiltering.add(subStrPropName);
                                    int indexOfProp = propertiesForFiltering.indexOf(subStrPropName);
                                    propsInHierarchy.put(indexOfFilteredMainProp, indexOfProp);
                                }
                                Found = true;
                                break;
                            }
                            /*else
                            {
                                // Property in the axiom is not in filtered properties list
                                // and in sub properties or super properties 
                                // We can store the axiom index to be deleted
                                if(!indexOfAxiomsTOBeDeleted.contains(index))
                                indexOfAxiomsTOBeDeleted.add(index);
                                System.out.println("Axiom to be deleted at index "+index);
                                System.out.println(axiom);
                                break;
                            }*/
                            indexOfFilteredMainProp++;
                        }
                        if(Found!=true)
                        {
                            if(!indexOfAxiomsTOBeDeleted.contains(index))
                                indexOfAxiomsTOBeDeleted.add(index);
                                //System.out.println("Axiom to be deleted at index "+index);
                                //System.out.println(axiom);
                        }
                        
                    
                }
                
            }
            index++;
            /*else if(index == 0)
            {
                // Check all the remaining axioms
                while(iter.hasNext())
                {
                    OWLAxiom axiomNext = (OWLAxiom) iter.next();
                    
                }
            }*/
        }
        //System.out.println("Axioms to be deleted \n"+indexOfAxiomsTOBeDeleted);
        if(!newIndivAxioms.isEmpty())
        {
            newIndivAxioms.clear();
        }
        Iterator iterAxiom = referencingAxioms2.iterator();
        index = 0;
        while(iterAxiom.hasNext())
        {
            OWLAxiom axiomToCopy = (OWLAxiom) iterAxiom.next();
            if(!indexOfAxiomsTOBeDeleted.contains((Integer)index))
            {
                //System.out.println("Adding axiom "+axiomToCopy+" at index " +index);
                newIndivAxioms.add(axiomToCopy);
            }
            index++;
        }
        System.out.println("-------------------------------------------------------------------");
        System.out.println("Axioms to be added for individual "+indiv);
        System.out.println(newIndivAxioms);
        System.out.println("\n----------------------------------------------------------------\n");
        System.out.println("Properties for filtering");
        System.out.println(propertiesForFiltering);
        //referencingAxiomsIndiv.addAll(referencingAxioms2);
        referencingAxiomsIndiv.addAll(newIndivAxioms);
        //System.out.println("Indiv Axioms ");
        //System.out.println(referencingAxioms2.toString());
        OWLOntology outputOntology = manager.createOntology();
        manager.addAxioms(outputOntology, newIndivAxioms);
        //manager.addAxioms(outputOntology, referencingAxioms2);
        //File filteredOntologyFile=new File("E:/Pallavi/Ontology/FilteredOntologyFileIndiv.owl");
        //File filteredOntologyFile=new File("E:/Pallavi/Ontology/FilteredMahabharataOntologyIndiv_DegreeCentralityFile.owl");
        File filteredOntologyFile=new File("E:/Pallavi/Ontology/FilteredTopCitedAuthorsCitationNetwork.owl");
        if (!filteredOntologyFile.exists())
            filteredOntologyFile.createNewFile();
        filteredOntologyFile=filteredOntologyFile.getAbsoluteFile();
        OutputStream outputStream=new FileOutputStream(filteredOntologyFile);
        manager.saveOntology(outputOntology, manager.getOntologyFormat(ontology), outputStream);
        
        // Confirm axioms for chosen properties are written
        //System.out.println("Number of referencing axioms are "+referencingAxioms2.size());
        return propertiesForFiltering;
        //return 1;
    }
    
    public static int SemanticFiltering(OWLClass className, OWLOntology ontology, OWLOntologyManager manager) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException
    {
        System.out.println("Call to Semantic Filtering for class "+className.getIRI().toString());
        // Add properties of all subclasses to the list of properties to be considered for filtering
        ReasonerFactory factory = new ReasonerFactory();
        Configuration configuration=new Configuration();
        configuration.throwInconsistentOntologyException=false;
        OWLReasoner reasoner=factory.createReasoner(ontology, configuration);
        //OWLReasoner reasoner = new Reasoner.ReasonerFactory().createReasoner(ontology);
        Set<OWLClass> subclassesOfClass = reasoner.getSubClasses(className, false).getFlattened();
        //System.out.println("Number of subclasses for class "+className+ " is "+subclassesOfClass.size());
        // Get subclasses using OWLOntologyManager
        //manager.
        //Iterator subclassIter = subclassesOfClass.iterator();
        //System.out.println("Subclasses of Class "+className+" are: ");
        //Set<OWLClass> subClasses = new HashSet<>();
        Vector propNames = new Vector();
        
        //subClasses contains class with name className and its subclasses
        //System.out.println("Before adding any property, Set size is  "+propNames.size());
        /*while(subclassIter.hasNext())
        {
            OWLEntity entity = (OWLEntity) subclassIter.next();
            System.out.println("Entity is "+entity.toString());
            if(!entity.isBuiltIn())
            {
                // if subclass is owl:Nothing, its not added to the set of subclasses of this class
                subClasses.add((OWLClass) entity);
            }
            String subclassName = entity.getIRI().getFragment();
            //System.out.println(subclassName+"\n");
        }
        
        subClasses.add(className);*/
        subclassesOfClass.add(className);
        // Get owl object properties with their expression
        Set<OWLObjectProperty> owlObjProp = ontology.getObjectPropertiesInSignature();
        //System.out.println("Number of properties "+owlObjProp.size());
        Iterator propIter = owlObjProp.iterator();
        Set<OWLObjectPropertyExpression> objPropExpr;
        NodeSet<OWLClass> classesAsDomainOfProp;
        while(propIter.hasNext())
        {
            OWLObjectProperty propInQues = (OWLObjectProperty) propIter.next();
            //System.out.println("Property is "+propInQues.getIRI().toString());
            /*if(propInQues.isOWLTopObjectProperty())
                break;*/
            //System.out.println("Property considered is "+propInQues.getIRI().toString());
            classesAsDomainOfProp = reasoner.getObjectPropertyDomains((OWLObjectPropertyExpression) propInQues, true);
            //System.out.println("Number of classes "+classesAsDomainOfProp.getNodes().size());
            //System.out.println("Class Name which is domain "+ classesAsDomainOfProp.getNodes().toString());
            //System.out.println(classesAsDomainOfProp.getFlattened().toString());
            //System.out.println(className.getIRI().toString());
            // Add this property to Properties to choose from list if className and/or its subclasses is in classesAsDomainOfProp list
            //String classAsDomainName = classesAsDomainOfProp.getFlattened().toString();
            //boolean classAsDomainContainsClassName = classAsDomainName.contains((CharSequence) className);
            //if(classAsDomainContainsClassName)
            if(propInQues.isOWLTopObjectProperty())
                continue;
            if(classesAsDomainOfProp.containsEntity(className))
            {
                //System.out.println("if satisfied");
                String PropertyName = propInQues.getIRI().getShortForm();
                //System.out.println(PropertyName+" is added \n");
                propNames.add(PropertyName);
                //break;
            }
            else
            {
                //System.out.println("Else satisfied");
                // Check if this property has any of the class's ancestor classes as domain
                NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(className, false);
                //System.out.println("Size of superclasses "+superClasses.getFlattened().size());
                Set<OWLClass> superClassSet = superClasses.getFlattened();
                //System.out.println(superClassSet.toString());
                
                for(OWLClass superclass: superClasses.getFlattened())
                {
                    //OWLClass superClassInQues = superclass;
                    if(classesAsDomainOfProp.containsEntity(superclass))
                        {
                            //System.out.println("else if satisfied");
                           String PropertyName = propInQues.getIRI().getShortForm();
                           //System.out.println(PropertyName+" is added \n");
                           propNames.add(PropertyName);
                           break; 
                        }
                }
                
                // Logic to check if property is applicable for subclasses of class className 
                // Dont use this - A property can exist for a class and its superclasses and not for subclasses 
                /*Iterator subclsIter = subclassesOfClass.iterator();
                while(subclsIter.hasNext())
                {
                        OWLClass subClassInQues = (OWLClass) subclsIter.next();
                        //boolean classAsDomainContainsSubClassName = classAsDomainName.contains((CharSequence)subClassInQues);
                        //if(classAsDomainContainsSubClassName)
                        if(classesAsDomainOfProp.containsEntity(subClassInQues))
                        {
                            System.out.println("else if satisfied");
                           String PropertyName = propInQues.getIRI().getShortForm();
                           System.out.println(PropertyName+" is added \n");
                           propNames.add(PropertyName);
                           break; 
                        }
                }*/
                
            }
            //break;
            //System.out.println("Number of properties added to propNames "+propNames.size());
            
            /* Logic to get only those properties for which class or its subclasses is in the domain of the Property in question
            Iterator clsIter = classesAsDomainOfProp.iterator();
            while(clsIter.hasNext())
            {
                OWLClass classAsDomain = (OWLClass)clsIter.next();
                System.out.println("Class as domain is: "+classAsDomain.getClass().getSimpleName() + " Property is "+propInQues.getIRI().getShortForm()+"\n");
                if(className.equals(classAsDomain))
                {
                    String PropertyName = propInQues.getIRI().getShortForm();
                    propNames.add(PropertyName);
                    break;
                }
                else // Check if  any sub classes match classesAsDomain
                {
                    Iterator subclsIter = subClasses.iterator();
                    while(subclsIter.hasNext())
                    {
                        OWLClass subClassInQues = (OWLClass) subclsIter.next();
                        if(subClassInQues.equals(classAsDomain))
                        {
                            String PropertyName = propInQues.getIRI().getShortForm();
                            propNames.add(PropertyName);
                            break;
                        }
                    }
                }
            }*/
        } // End of while propIter
        
        // Get top object properties for properties of class and subclasses
        Set<OWLObjectProperty> topObjPropsOfProps = new HashSet<>();
        Iterator propertyIter = owlObjProp.iterator();
        while(propertyIter.hasNext())
        {
            OWLObjectProperty Property = (OWLObjectProperty) propertyIter.next();
            NodeSet<OWLObjectPropertyExpression> topObjProps = reasoner.getSuperObjectProperties(Property, false);
            // Add these to propNames
            /*Iterator topPropIter = topObjProps.iterator();
            while(topPropIter.hasNext())
            {
            OWLObjectProperty topProp = (OWLObjectProperty) topPropIter.next();
            String topPropName = topProp.toString();
            System.out.println("Top Property which got added is "+topPropName);
            propNames.add(topPropName);
            }*/
            for (Iterator<Node<OWLObjectPropertyExpression>> it = topObjProps.iterator(); it.hasNext();) {
                Node<OWLObjectPropertyExpression> superpropertyExp = it.next();
                if(!superpropertyExp.isTopNode())
                {
                    String topPropName = superpropertyExp.getEntities().toString();
                    //System.out.println("Top Property which got added is "+topPropName);
                    propNames.add(topPropName);
                } // Only if top property is not topObjectProperty, add to propNames
            }
            // Check if there are property chains
            
        }
        // Check for property chain axioms in ontology
        Set<OWLSubPropertyChainOfAxiom> axioms = ontology.getAxioms(AxiomType.SUB_PROPERTY_CHAIN_OF);
        if(!axioms.isEmpty())
        {
            // If there are property chains, remove them from ontology
            manager.removeAxioms(ontology, axioms);
        }
        //System.out.println("Properties chosen "+propNames.size());
        String[] propNamesForSelection = new String[propNames.size()];
        /*System.out.println("Chosen Properties are");
        Iterator propNamesIter = propNames.iterator();
        int i = 0;
        while(propNamesIter.hasNext())
        {
            String prop = propNamesIter.next().toString();
            System.out.println(prop);
            propNamesForSelection[i] = prop;
            i++;
        }*/
        
        // Show properties and let user select properties for Semantic Filtering
        
        //CreateGUIShowProperties(propNamesForSelection);
           
        // Create sub graph with class, properties chosen
        // For all properties, get object property domain and range axioms
        // Get referencing axioms of the class
        // Construct sub graph/write to an ontology file, these axioms
        // Get property expression from property string name for all properties chosen for filtering
        List<String> propertiesForFiltering = new ArrayList<>();
        //propertiesForFiltering.add("hasGuru");
        //.add("hasDisciple");
        /*propertiesForFiltering.add("hasParent");
        propertiesForFiltering.add("hasWife");
        propertiesForFiltering.add("hasHusband");
        propertiesForFiltering.add("hasSibling");
        propertiesForFiltering.add("hasSon");
        propertiesForFiltering.add("hasDaughter");*/
        propertiesForFiltering.add("isAuthorOf");
        //Iterator PropStringIter = propNames.iterator(); // propNames as of now, get selection from JList and replace propNames later
        Iterator PropStringIter = propertiesForFiltering.iterator();
        OWLDataFactory datafactory = manager.getOWLDataFactory();
        //Optional<IRI> ontologyIRI = ontology.getOntologyID().getOntologyIRI();
        //String ontologyIRI = "http://www.semanticweb.org/drkm/ontologies/2017/4/untitled-ontology-82#";
        //String ontologyIRI = "http://www.semanticweb.org/vinu/ontologies/2014/3/untitled-ontology-64#";
        String ontologyIRI = "http://www.semanticweb.org/drkm/ontologies/2018/1/untitled-ontology-104#";
        while(PropStringIter.hasNext())
        {
            String property = PropStringIter.next().toString();
            //System.out.println(property);
            
            //System.out.println(ontologyIRI);
            OWLObjectProperty objProp = datafactory.getOWLObjectProperty(IRI.create(ontologyIRI + property));
            //System.out.println(owlObjProp.iterator().next());
            //System.out.println(objProp);
            // Construct propertyExpression
            if(owlObjProp.contains(objProp))
            //if(owlObjProp.contains(property))
            {
                //System.out.println("Inside if"+" for Object Property "+objProp);
               OWLObjectPropertyExpression propExpression = objProp.getSimplified();
               subGraphDomainAxioms.addAll(ontology.getObjectPropertyDomainAxioms(propExpression));
               //System.out.println("Domain Axioms ");
               System.out.println(subGraphDomainAxioms.toString());
               //System.out.println("Range Axioms ");
                Set<OWLObjectPropertyRangeAxiom> objectPropertyRangeAxioms = ontology.getObjectPropertyRangeAxioms(propExpression);
               subGraphRangeAxioms.addAll(objectPropertyRangeAxioms);
               //System.out.println(subGraphRangeAxioms.toString());
                referencingAxioms.addAll(ontology.getReferencingAxioms(objProp));
                //System.out.println("Referencing Axioms");
                //System.out.println(referencingAxioms);
            }
        }
        // For classes involved, add all referencing axioms
        referencingAxioms1 = ontology.getReferencingAxioms(className);
        referencingAxioms.addAll(referencingAxioms1);
        //System.out.println("Class Axioms ");
        //System.out.println(referencingAxioms1.toString());
        
        // Add all axioms of class and chosen properties to a new ontology
        //String ontologyPath = "E:/Pallavi/Ontology/FilteredFoodOntology.owl";
        //OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File(ontologyPath));
        //IRI outputDocumentIRI = IRI.create("E:/Pallavi/Ontology/FilteredFoodOntology.owl");
        OWLOntology outputOntology = manager.createOntology();
        manager.addAxioms(outputOntology, subGraphDomainAxioms);
        manager.addAxioms(outputOntology, subGraphRangeAxioms);
        manager.addAxioms(outputOntology, referencingAxioms);
        //File filteredOntologyFile=new File("E:/Pallavi/Ontology/FilteredOntologyFile.owl");
        //File filteredOntologyFile=new File("E:/Pallavi/Ontology/FilteredMahabharataOntology_DegreeCentralityFile.owl");
        File filteredOntologyFile=new File("E:/Pallavi/Ontology/FilteredTopCitedAuthorsCitationNetwork_Authorclass.owl");
        if (!filteredOntologyFile.exists())
            filteredOntologyFile.createNewFile();
        filteredOntologyFile=filteredOntologyFile.getAbsoluteFile();
        OutputStream outputStream=new FileOutputStream(filteredOntologyFile);
        manager.saveOntology(outputOntology, manager.getOntologyFormat(ontology), outputStream);
        
        // Confirm axioms for chosen properties are written
        System.out.println("Number of referencing axioms are "+referencingAxioms.size());
        
        
    return 1;
    } // End of function SemanticFiltering
    
    public static void CreateGUIShowProperties(String[] propNamesForSelection)
    {
        SelectProperties objProp = new SelectProperties(propNamesForSelection);
        List<String> selProps = objProp.getChosenValues();
        //System.out.println("The number of chosen properties is"+selProps.size());
        /*JFrame frame = new MultiSelectionDemo(propNames);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
        //MultiSelectionDemo mst = new MultiSelectionDemo(propNames);
        //JFrame frame = mst;
        //Object[] selectedValues = mst.selection;
        /*System.out.println("Selected values in DegreeCentrality App ");
        for(int i=0;i<selectedValues.length;i++)
        {
            System.out.println(selectedValues[i]);
        }*/
        
    }
}

