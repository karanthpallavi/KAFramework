/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.loadaboxfromjson;

/**
 *
 * @author Pallavi Karanth
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


public class JsonNodeDemo {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    JsonNode rootNode;
    ObjectMapper objectMapper;
    public JsonNodeDemo(File file)throws IOException{
        objectMapper = new ObjectMapper();
        rootNode = objectMapper.readTree(file);
    }
        public JsonNode readJsonWithJsonNode() throws JsonProcessingException {
            //public void readJsonWithJsonNode() throws JsonProcessingException {
            //for(JsonNode root : rootNode){
        String prettyPrintPublication = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        logger.info(prettyPrintPublication+"\n");
        return rootNode;
            //}
    }
        public String readTitle()
        {
            JsonNode titleNode=rootNode.path("title");
            String title=titleNode.asText();
            logger.info("\n----------------------------\nPublication Title: "+title+"\n");
            return title;
        }
        public int readNumCitations()
        {
            JsonNode numCitationNode=rootNode.path("n_citation");
            int numCitation=numCitationNode.asInt();
            logger.info("\n----------------------------\nNumber of Citations: "+numCitation+"\n");
            return numCitation;
        }
      /*public Map<String,String> readPersonalInformation() throws JsonProcessingException
       {
           JsonNode personalInformationNode = rootNode.get("personalInformation");
           Map<String, String> personalInformationMap = objectMapper.convertValue(personalInformationNode, Map.class);
           for (Map.Entry<String, String> entry : personalInformationMap.entrySet())
           {
               logger.info("\n----------------------------\n"+entry.getKey() + "=" + entry.getValue()+"\n");
           }
             return personalInformationMap;
       }*/
       public Iterator<JsonNode> readAuthors(){
           JsonNode authorsNode = rootNode.path("authors");
           Iterator<JsonNode> elements = authorsNode.elements();
           while(elements.hasNext()){
               JsonNode authorNode = elements.next();
               logger.info("\n----------------------------\nAuthors = "+authorNode.asText());
           }
           return elements;
       }
}