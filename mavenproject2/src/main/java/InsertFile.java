/*
* Copyright (c) 1997, 2011, Oracle and/or its affiliates. All rights reserved.
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
*
* This code is free software; you can redistribute it and/or modify it
* under the terms of the GNU General Public License version 2 only, as
* published by the Free Software Foundation.  Oracle designates this
* particular file as subject to the "Classpath" exception as provided
* by Oracle in the LICENSE file that accompanied this code.
*
* This code is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
* FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
* version 2 for more details (a copy is included in the LICENSE file that
* accompanied this code).
*
* You should have received a copy of the GNU General Public License version
* 2 along with this work; if not, write to the Free Software Foundation,
* Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
*
* Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
* or visit www.oracle.com if you need additional information or have any
* questions.
*/

import java.io.FileOutputStream;
import javax.jcr.Repository;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 * This class consists in insert a file into Jackrabbit remote repository.
 * 
 * @author Stevens Tellez
 */
@WebServlet(urlPatterns = {"/InsertFile"})
@MultipartConfig
public class InsertFile extends HttpServlet {
    
    private final static Logger LOGGER = 
            Logger.getLogger(InsertFile.class.getCanonicalName());
      
    private String url;
    private Repository repository;
    private Session jcrSession;
    private Node appNode;
    private Node imagesNode;
    private Node musicNode;
    private Node documentsNode;
    private Node othersNode;
    
    
    public InsertFile(){
        url = null;
        repository = null;
        jcrSession = null;
        appNode = null;
        imagesNode = null;
        musicNode = null;
        documentsNode = null; 
        othersNode = null;
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * 
     * @param request request send to servlet
     * @param response response send by the servlet
     * @throws ServletException if occurs any exception that interfered with normal operation of the servlet
     * @throws IOException if don't found the file, or any I/O error occurs
     * @throws RepositoryException if occurs any exception on the repository
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RepositoryException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        final Part filePart = request.getPart("file1");
        final String fileName = getFileName(filePart);
        
        InputStream filecontent = null;
        FileOutputStream fos = null;
        
        try {
            /* TODO output your page here. You may use following sample code. */
            filecontent = filePart.getInputStream();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<body>");
            if(filecontent.available() > 0){
                insert(filecontent, fileName);                         
                out.println("<h1> Succes Insert File </h1>");
                
            }else {        
                out.println("<h1> Field Image is empty </h1>");
            }
            out.println("<form action= \"http://localhost:8084/mavenproject2/index.jsp\"><input type=\"submit\" value=\"Home\"></form>");
            out.println("</body>");
            out.println("</html>");
          
        }finally {
            //out.close();
            if (filecontent != null)
                filecontent.close();
            jcrSession.save();
            jcrSession.logout();
                  
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if occurs any exception that interfered with normal operation of the servlet
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (IOException ex) {
            Logger.getLogger(InsertFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(InsertFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(InsertFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title ="Using GET Method to Read Form Data";
        String docType =
        "<!doctype html public \"-//w3c//dtd html 4.0 "+
        "transitional//en\">\n";
        out.println(docType +
        "<html>\n"+
        "<head><title>"+ title +"</title></head>\n"+
        "<body bgcolor=\"#f0f0f0\">\n"+
        "<h1 align=\"center\">"+ title +"</h1>\n"+
        "<ul>\n"+ 
        " <li><b>First Name</b>: " 
        + request.getParameter("first_name")+"\n"+
        " <li><b>Last Name</b>: "
        + request.getParameter("last_name")+"\n"+
        "</ul>\n"+
        "</body></html>");       
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
            //doGet(request, response);
        } catch (IOException ex) {
            Logger.getLogger(InsertFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(InsertFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(InsertFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Make a connection with Jackrabbit remote repository and check what exists the correct nodes for the workspace
     * and insert the file into repository.
     * 
     * @param is inputStream associated with the entry file
     * @param fn the name of the file with the extension
     * @throws ServletException If any error occurs 
     * @throws RepositoryException If couldn't connect with the repository
     * @see InputStream
     * @see String
     */
    public void insert(InputStream is, String fn) throws ServletException, RepositoryException{
        System.out.println("Connecting to repository...");
        url = "http://localhost:8080/rmi";
        repository = JcrUtils.getRepository(url);
        jcrSession = repository.login(new SimpleCredentials("admin", "admin".toCharArray()) , "default");
        Node root = jcrSession.getRootNode();
        if(!root.hasNode("storefiles")){
            appNode = root.addNode("storefiles");
            if(!appNode.hasNode("storefiles/images")){
                imagesNode = appNode.addNode("images");              
            }
            if(!appNode.hasNode("storefiles/music")){
                musicNode = appNode.addNode("music");               
            }
            if(!appNode.hasNode("storefiles/documents")){
                documentsNode = appNode.addNode("documents");                
            }
            if(!appNode.hasNode("storefiles/others")){
                othersNode = appNode.addNode("others");              
            }
        }else{
            appNode = root.getNode("storefiles");
            imagesNode = root.getNode("storefiles/images");
            musicNode = root.getNode("storefiles/music");
            documentsNode = root.getNode("storefiles/documents");
            othersNode = root.getNode("storefiles/others");
        }
        String extencion = null;
        if(!fn.isEmpty()){
            extencion = fn.substring(fn.indexOf("."),fn.length());
            System.out.println("The extension of the file is:" + extencion);
            if(extencion.equals(".png") || extencion.equals(".jpeg") || extencion.equals(".jpg")){
                imagesNode.setProperty(fn,is);
            }else if(extencion.equals(".txt") || extencion.equals(".pdf") || extencion.equals(".docx")){
                documentsNode.setProperty(fn, is);
            }else if(extencion.equals(".mp3")){
                musicNode.setProperty(fn, is);
            }
        }else {
            System.out.println("String vacio");
        }    
    }
    
    /**
     * Get the name of the file.
     * 
     * @param part multipart/form-data get from HTML request.
     * @return the name of the file with the extension.
     * @see Part
     */
    private String getFileName(final Part part){
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for(String content: part.getHeader("content-disposition").split(";")){
            if(content.trim().startsWith("filename")){
                return content.substring(
                        content.indexOf('=') + 1 ).trim().replace("\"","");
            }
        }
        return null;
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}