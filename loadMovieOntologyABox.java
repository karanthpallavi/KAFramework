/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.loadmovieontologyabox;
//include info.movito.themoviedbapi.TmdbApi;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbMovies.MovieMethod;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.ProductionCompany;
import info.movito.themoviedbapi.model.ProductionCountry;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCast;
import info.movito.themoviedbapi.model.people.PersonCrew;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;

/**
 *
 * @author Pallavi Karanth
 */
public class loadMovieOntologyABox 
{
    public static void main(String args[]) throws OWLOntologyCreationException, OWLOntologyStorageException
    {
        String fileName = args[0];
        System.out.println("Ontology being processed is " + fileName); 
        
        String folderName = args[1];
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
        String base = "http://www.semanticweb.org/drkm/ontologies/2018/5/untitled-ontology-42#";
        OWLDataFactory dataFactory = manager.getOWLDataFactory();
        
        TmdbMovies movies = new TmdbApi("7d9655c61970d3dcf55806c789292eaa").getMovies();
        /*MovieDb movie = movies.getMovie(78, "en", MovieMethod.credits, MovieMethod.images, MovieMethod.similar);
        String originalTitle = movie.getOriginalTitle();
        System.out.println("Original Title " +originalTitle);*/
        List<MovieDb> allMovies = new ArrayList<>();
        for(int page = 1; page<10;page++)
        {
            MovieResultsPage popularMovies = movies.getPopularMovies("en", page);
            List<MovieDb> results = popularMovies.getResults();
            System.out.println(results);
            MovieResultsPage topRatedMovies = movies.getTopRatedMovies("en", page);
            List<MovieDb> results1 = topRatedMovies.getResults();
            System.out.println(results1);
            allMovies.addAll(results);
            allMovies.addAll(results1);
            System.out.println(allMovies.size());
        }
        List<Integer> movieIds;
        List<String> movieTitles = new ArrayList<>();
        movieIds = new ArrayList<>();
        
        for(MovieDb movie : allMovies)
        {
            //System.out.println(movie.getReleaseDate());
            Integer id = movie.getId();
            OWLClass movieClass = dataFactory.getOWLClass(IRI.create(base+"Movie"));
            
            movieIds.add(id);
            System.out.println(movie.getId());
            MovieDb eachMovie = movies.getMovie(id, "en", MovieMethod.credits);
            List<PersonCast> cast = eachMovie.getCast();
            String titleWithWhiteSpace = movie.getTitle();
            String titleWithoutWhiteSpace = titleWithWhiteSpace.replaceAll("\\s","");
            String titleTrimmed = titleWithoutWhiteSpace.trim();
            OWLNamedIndividual indivMovie = dataFactory.getOWLNamedIndividual(IRI.create(base+titleTrimmed));
            OWLClassAssertionAxiom ax = dataFactory.getOWLClassAssertionAxiom(movieClass, indivMovie);
            manager.addAxiom(ontology, ax);
            System.out.println("Added "+indivMovie+ " to class "+movieClass);
            OWLDataProperty hasTitle = dataFactory.getOWLDataProperty(IRI.create(base+"hasTitle"));
            OWLDataPropertyAssertionAxiom hasTitleAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasTitle, indivMovie, titleTrimmed);
            manager.addAxiom(ontology, hasTitleAxiom);
            OWLDataProperty hasId = dataFactory.getOWLDataProperty(IRI.create(base+"hasId"));
            OWLDataPropertyAssertionAxiom hasIdAxiom = dataFactory.getOWLDataPropertyAssertionAxiom(hasId, indivMovie, id);
            manager.addAxiom(ontology, hasIdAxiom);
            /*for(PersonCast eachCast: cast)
            {
                System.out.println("Movie "+ title+" Cast: "+eachCast.toString());
            }
            movieTitles.add(eachMovie.getOriginalTitle());
            List<Genre> genres = eachMovie.getGenres();
            for(Genre eachGenre: genres)
            {
                System.out.println("Movie: "+title + " Genre: "+eachGenre);
            }
            List<PersonCrew> crew = eachMovie.getCrew();
            for(PersonCrew eachCrew: crew)
            {
                System.out.println("Movie: " + title+ " Crew: "+eachCrew.toString());
            }
            List<ProductionCompany> productionCompanies = eachMovie.getProductionCompanies();
            List<ProductionCountry> productionCountries = eachMovie.getProductionCountries();
            String originalLanguage = eachMovie.getOriginalLanguage();
            String releaseDate = eachMovie.getReleaseDate();*/
            //System.out.println("Movie: "+movie.getTitle()+" Cast :"+crew);
        }
        System.out.println("Number of axioms in ontology after adding "+ontology.getAxiomCount());
        File fileformated = new File("E:/Pallavi/Ontology/Movie_ABoxLoadedNew.owl");
        //Save the ontology in a different format
                OWLDocumentFormat ontologyFormat = manager.getOntologyFormat(ontology);
                System.out.println("Format of ontology before saving is "+ontologyFormat);
                OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
                System.out.println("Ontology will be saved in "+owlxmlFormat);
                if (ontologyFormat.isPrefixOWLDocumentFormat()) { 
                    owlxmlFormat.copyPrefixesFrom(ontologyFormat.asPrefixOWLDocumentFormat()); 
                }
                manager.saveOntology(ontology, owlxmlFormat, IRI.create(fileformated.toURI()));
                
                
    /*OWLOntology o = manager.loadOntologyFromOntologyDocument(new FileDocumentSource(
        new File("E:/Pallavi/Ontology/Movie_ABoxLoadedNew.owl"), new OWLXMLDocumentFormat()));*/
    System.out.println("Check.should() " + manager.getOntologyFormat(ontology));
    ontology.getAxioms().forEach(System.out::println);
    } 
}