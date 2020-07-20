/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.loadaboxfromjson;

import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;
import java.util.Iterator;
import java.util.Map;
import static org.junit.Assert.*;
import org.codehaus.jackson.JsonEncoding; 
import org.codehaus.jackson.JsonFactory; 
import org.codehaus.jackson.JsonGenerationException; 
import org.codehaus.jackson.JsonGenerator; 
//import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser; 
import org.codehaus.jackson.JsonToken; 
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
/**
 *
 * @author Pallavi Karanth
 */
public class loadABoxFromJSON {
    public static String IndivDataStr[][] = new String[3079007][];
    public static void main(String args[]) throws OWLOntologyCreationException, FileNotFoundException, IOException, OWLOntologyStorageException
    {
       
        try{
            String datafilePath = args[0];
            //FileReader fr = new FileReader("E:/Pallavi/data/dblp.v9/sample.txt");
            System.out.println("Data File Path is : "+datafilePath);
            //FileReader fr = new FileReader(datafilePath);
            File source  = new File(datafilePath);
            
            String fileName = args[1];
        System.out.println("Ontology being processed is " + fileName); 
        
        String folderName = args[2];
        System.out.println("Folder containing other imported ontologies is "+folderName);
          OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        // We load an ontology from the URI specified
        // on the command line
        File owlFile = new File(fileName);
        File folder = new File(folderName);
        AutoIRIMapper mapper=new AutoIRIMapper(folder, true);
        //manager.addIRIMapper(mapper);
        manager.getIRIMappers().add(mapper);
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
        System.out.println("Number of axioms in Ontology before adding individuals "+ontology.getAxiomCount());
        String base = "http://www.semanticweb.org/drkm/ontologies/2018/1/untitled-ontology-104";
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        
        /*JsonFactory factory = new JsonFactory();
        
        JsonParser parser = factory.createJsonParser(source);
        parser.nextToken();                                     //start reading the file
        while(!parser.isClosed())
        {
        while (parser.nextToken() != JsonToken.END_OBJECT) {    //loop until "}"
                
               String fieldName = parser.getCurrentName();
               if (fieldName.equals("title")) {
                   parser.nextToken();
                   System.out.println("title : " + parser.getText());
               }else if (fieldName.equals("id")) {
                   parser.nextToken();
                   System.out.println("id : " + parser.getText());
               }  
               else if (fieldName.equals("n_citation")) {
                   parser.nextToken();
                   System.out.println("NumCitations : " + parser.getIntValue());
               } else if (fieldName.equals("venue")) {
                   parser.nextToken();
                   System.out.println("venue : " + parser.getText());
               }else if (fieldName.equals("year")) {
                   parser.nextToken();
                   System.out.println("year : " + parser.getIntValue());
               }  
               else if (fieldName.equals("authors")) {
                   parser.nextToken();
                   while (parser.nextToken() != JsonToken.END_ARRAY){   //loop until "]"
                       System.out.println("Author: " +parser.getText());
                   }
               } 
               else if (fieldName.equals("references")) {
                   parser.nextToken();
                   while (parser.nextToken() != JsonToken.END_ARRAY){   //loop until "]"
                       System.out.println("References: " +parser.getText());
                   }
               } 
               else if(fieldName.equals("abstract")) {
                   continue;
               }
               else { // unexpected token, generate error
                   throw new IOException("Unrecognized field '"+fieldName+"'");
               }
        }
        } // End of while(!parser.isClosed)
              parser.close(); */
            /*ObjectMapper mapper1 = new ObjectMapper();
            org.codehaus.jackson.JsonNode rootNodes = mapper1.readTree(source);
            List<org.codehaus.jackson.JsonNode> listOfNodes = rootNodes.findParents("authors");
            System.out.println(listOfNodes.size());*/
            //JsonNodeDemo jsonNodeDemo=new JsonNodeDemo(source);
            //JsonNode rootNode=jsonNodeDemo.readJsonWithJsonNode();
            //jsonNodeDemo.readJsonWithJsonNode();
            /*jsonNodeDemo.readAuthors();
            //assertNotNull(rootNode);
            String title=jsonNodeDemo.readTitle();
            System.out.println(title);*/
            //assertEquals("Preliminary Design of a Network Protocol Learning Tool Based on the Comprehension of High School Students: Design by an Empirical Study Using a Simple Mind Map", title);
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(source);
            // starting parsing of JSON String 
            //while (parser.nextToken() != JsonToken.END_OBJECT)
            List<String> coAuthors = new ArrayList<>();
            List<String> referencesPaper = new ArrayList<>();
            String title = null;
            int numCitation = 0;
            String venue = null;
            int year = 0;
            int numOfEntries = 0;
            while(!parser.isClosed())
            {
                
            JsonToken jsonToken = parser.nextToken();
            String token = parser.getCurrentName();
            if ("authors".equals(token))
            {
            System.out.println("Authors :"); parser.nextToken();
            int numAuthors = 0;
            
            boolean done = false;
            // next token will be '[' which means JSON array
            // parse tokens until you find ']'
            while (parser.nextToken() != JsonToken.END_ARRAY)
            {
            numAuthors++;
            String authorName = parser.getText();
            if(done == false)
            {
            done = true;
            }
            coAuthors.add(authorName);
            String authorNameWithHash = "#"+authorName;
            System.out.println(authorNameWithHash);
            OWLIndividual author = dataFactory.getOWLNamedIndividual(IRI.create(base + authorNameWithHash));
            OWLClass authorClass = dataFactory.getOWLClass(IRI.create(base + "#Author"));
            OWLClassAssertionAxiom ax = dataFactory.getOWLClassAssertionAxiom(authorClass, author);
            manager.addAxiom(ontology, ax);
            System.out.println("Added "+author+ " to class "+authorClass);
            }
            if(coAuthors.size()>1)
            {
            // Add coAuthor property assertions
            OWLObjectProperty CoAuthorWith = dataFactory.getOWLObjectProperty(IRI.create(base+"CoAuthorWith"));
            for(int i = 0;i<coAuthors.size();i++)
            {
            for(int j = 0; j<coAuthors.size();j++)
            {
            if(i==j)
            continue;
            String authorName1WithHash = "#"+coAuthors.get(i);
            String authorName2WithHash = "#"+coAuthors.get(j);
            OWLIndividual author1 = dataFactory.getOWLNamedIndividual(IRI.create(base + authorName1WithHash));
            OWLIndividual author2 = dataFactory.getOWLNamedIndividual(IRI.create(base + authorName2WithHash));
            OWLObjectPropertyAssertionAxiom coAuthorAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(CoAuthorWith, author1, author2);
            manager.addAxiom(ontology, coAuthorAssertion);
            System.out.println("Added co authors "+author1+" and"+author2);
            }
            //coAuthors.clear();
            }
            }
            }
            if ("title".equals(token))
            {
            parser.nextToken(); //next token contains value
            title = parser.getText(); //getting text field
            System.out.println("title : " + title);
            }
            if ("references".equals(token))
            {
            System.out.println("References :"); parser.nextToken();
            
            // next token will be '[' which means JSON array
            // parse tokens until you find ']'
            while (parser.nextToken() != JsonToken.END_ARRAY)
            {
                String ref = parser.getText();
            System.out.println(ref);
            referencesPaper.add(ref);
            }
            }
            if ("n_citation".equals(token))
            {
            parser.nextToken(); //next token contains value
            numCitation = parser.getIntValue(); //getting text field
            System.out.println("number of citations : " + numCitation);
            }
            if ("id".equals(token))
            {
            parser.nextToken(); //next token contains value
            String id = parser.getText(); //getting text field
            System.out.println("id : " + id);
            String idWithhash = "#"+id;
            //String titleWithHash = "#"+title;
            OWLClass publicationClass = dataFactory.getOWLClass(IRI.create(base + "#ScientificPublication"));
            OWLIndividual publication = dataFactory.getOWLNamedIndividual(IRI.create(base + idWithhash));
            OWLClassAssertionAxiom ax = dataFactory.getOWLClassAssertionAxiom(publicationClass, publication);
            manager.addAxiom(ontology, ax);
            System.out.println("Added "+publication+ " to class "+publicationClass);
            OWLDataProperty hasTitle = dataFactory.getOWLDataProperty(IRI.create(base+"hasTitle"));
            OWLDataPropertyAssertionAxiom hasTitleAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasTitle, publication, title);
            manager.addAxiom(ontology, hasTitleAxiom);
            System.out.println("Added title " + title + " to "+publication);
            OWLDataProperty NumCitations = dataFactory.getOWLDataProperty(IRI.create(base+"hasNumberOfCitations"));
            OWLDataPropertyAssertionAxiom hasNumCitationsAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(NumCitations, publication, numCitation);
            manager.addAxiom(ontology, hasNumCitationsAxiom);
            System.out.println("Added number of citations " + numCitation + " to "+publication);
            OWLDataProperty publishedInVenue = dataFactory.getOWLDataProperty(IRI.create(base+"publishedInVenue"));
            OWLDataPropertyAssertionAxiom venueAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(publishedInVenue, publication, venue);
            manager.addAxiom(ontology, venueAxiom);
            System.out.println("Added venue " + venue + " to "+publication);
            OWLDataProperty publishedInYear = dataFactory.getOWLDataProperty(IRI.create(base+"publishedInYear"));
            OWLDataPropertyAssertionAxiom yearAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(publishedInYear, publication, year);
            manager.addAxiom(ontology, yearAxiom);
            System.out.println("Added published year " + year + " to "+publication);
            numOfEntries++;
            //Reset title
            title=null;
            numCitation = 0;
            venue = null;
            year = 0;
            if(referencesPaper.size()>0)
            {
                // Add paper cites relation
                for(int i=0;i<referencesPaper.size();i++)
                {
                    String reference = referencesPaper.get(i);
                    String referenceWithHash = "#"+reference;
                    OWLIndividual referencePaper = dataFactory.getOWLNamedIndividual(IRI.create(base +referenceWithHash ));
                    OWLObjectProperty citesProperty = dataFactory.getOWLObjectProperty(IRI.create(base +"#cites"));
                    OWLObjectPropertyAssertionAxiom citesAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(citesProperty, publication, referencePaper);
                    manager.addAxiom(ontology, citesAxiom);
                    System.out.println("Added reference" + referencePaper+ " to "+publication);
                }
                referencesPaper.clear();
            }
            // authorOf relation also here
            int numOfAuthors = coAuthors.size();
            for(int i = 0;i<numOfAuthors;i++)
            {
               String authorNameWithHash = "#"+coAuthors.get(i); 
               OWLIndividual author = dataFactory.getOWLNamedIndividual(IRI.create(base +authorNameWithHash));
               OWLObjectProperty isAuthorOf = dataFactory.getOWLObjectProperty(IRI.create(base +"isAuthorOf"));
               OWLObjectPropertyAssertionAxiom authorOfAxiom = dataFactory.getOWLObjectPropertyAssertionAxiom(isAuthorOf, author,publication);
               manager.addAxiom(ontology, authorOfAxiom);
               System.out.println("Added author" + author+ " to "+publication);
            }
            coAuthors.clear();
            }
            if ("venue".equals(token))
            {
            parser.nextToken(); //next token contains value
            venue = parser.getText(); //getting text field
            System.out.println("venue : " + venue);
            }
            if ("year".equals(token))
            {
            parser.nextToken(); //next token contains value
            year = parser.getIntValue(); //getting text field
            System.out.println("year : " + year);
            }
            if(numOfEntries==200)
            {
                System.out.println("Number of axioms in ontology after adding "+ontology.getAxiomCount());
                File file = new File("E:/Pallavi/Ontology/CitationNetworkOntologyWithABox.owl");
                manager.createOntology(IRI.create((base + "ontology")));
                manager.saveOntology(ontology, IRI.create(file));
                System.out.println("Adding individuals to new file was successful!");
                break;
            }
            }
            parser.close(); 
            /*JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fr);*/
            //String title = (String) jsonObject.get("title");
            //System.out.println("Title of Paper: "+title);
            //System.out.println("Title "+jsonObject.get("title"));
            //BufferedReader br = new BufferedReader(fr);
            //getObjectFromTree(source);

        } 
        catch (JsonGenerationException jge) 
        { 
            jge.printStackTrace(); 
        } 
        catch (JsonMappingException jme) 
        { 
            jme.printStackTrace(); 
        } 
        catch (IOException e) {

	  e.printStackTrace();

     }
     }
       
/*public static void getObjectFromTree(File source) throws IOException
{
    JsonNode publicationNode = new ObjectMapper().readTree(source);
    String title = publicationNode.get("title").getTextValue();
    System.out.println("Title is "+title);
    
}*/
  }


            
            