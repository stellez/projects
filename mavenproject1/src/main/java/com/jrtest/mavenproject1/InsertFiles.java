/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jrtest.mavenproject1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.core.TransientRepository;

/**
 *
 * @author xumakgt1
 */
public class InsertFiles {

/**
 * Second hop example. Stores, retrieves, and removes example content.
 */
  
   
   
   public static void preorder(Node n) throws RepositoryException{
       NodeIterator avaNodes;
       avaNodes = n.getNodes(); 
       if(!n.hasNodes()){ 
           return;
        }else{
            while(avaNodes.getPosition() != avaNodes.getSize()){
                n = avaNodes.nextNode();
                System.out.println(n.getPath());
                System.out.println(n.getIdentifier());
                preorder(n);
            }
        }     
    }
    
    public static void main(String[] args) throws Exception {
        Repository repository = new TransientRepository();
        Session session = repository.login(new SimpleCredentials("username",
                "password".toCharArray()));
        try {
            Node root = session.getRootNode();

            // Store content
            // Node hello = root.addNode("hello");
            //Node world = hello.addNode("world");
            //world.setProperty("message", "Hello World!");
            //Node next = world.addNode("Next");
            //session.save();
            
            Binary archBin;
            InputStream is = new FileInputStream("/home/xumakgt1/Desktop/Youtopia.mp3");
            //InputStream ls = new FileInputStream("/home/xumakgt1/Desktop/Youtopia.mp3");
            /*int j;
            while((j=ls.read()) != -1){
                System.out.print(j + " ");
            }
            System.out.println();*/
            
            //Binary binary = session.getValueFactory().createBinary(is);
            //next.setProperty("jcr:data", binary);
            //next.setProperty("jcr:data", is);
                     
            //InputStream getTree;
            //getTree = next.getProperty("jcr:data").getStream();
            //FileOutputStream fos = new FileOutputStream("/home/xumakgt1/Desktop/copyYoutopia.mp3");
            /*
            int i,cont, size;
            cont = 0;
            size = getTree.available();
            while((i=getTree.read()) != -1){
                //System.out.print(i + " ");
                cont = ++cont;
                fos.write(i);
            }*/
            //fos.close();
            //System.out.println("\ntamaño: " + cont + " bytes");
            //System.out.println("tamaño: " + size + " bytes");
            //System.out.println("tamaño: " + binary.getSize() + " bytes");
            
            // Retrieve content
            //Node node = root.getNode("Hello/world");
            //System.out.println(node.getPath());
            //System.out.println(node.getProperty("message").getString());
            
            //Recorrer arbol
            preorder(root);
            //session.save();
            
            // Remove content
            //root.getNode("hello/world/Next").remove();
            session.save();

        } finally {
            session.logout();
        }
    }
}