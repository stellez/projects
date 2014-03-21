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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 * This class consists in get the files stores in Jackrabbit repository and show it to the user
 * the files specifically are images.
 * 
 * @author Stevens Tellez
 */
@WebServlet(urlPatterns = {"/ViewFiles"})
public class ViewFiles extends HttpServlet{

    private String url;
    private Repository repository; 
    private Session jcrSession;
    private Node root, appNode, imagesNode,
         musicNode, documentsNode, othersNode;
    
    public ViewFiles(){
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
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws javax.jcr.RepositoryException if a repository error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, RepositoryException {
        
        if(response != null){
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ViewFiles</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<center> <h1>Images</h1></center> "); 
            out.println("<center> <form action= \"http://localhost:8084/mavenproject2/index.jsp\"><input type=\"submit\" value=\"Home\"></form></center> ");           
            if(this.connectRepository()){
                out.println("<center>" + this.listFiles(imagesNode) + "</center>" );
                if(request != null){
                        String filePart = "";              
                        filePart = request.getParameter("dropdown");
                        if(filePart != null){
                            if(!filePart.isEmpty()){
                                InputStream in = searchBufferImage(imagesNode, filePart);
                                int size = in.available();
                                byte[] content = new byte[size];  
                                in.read(content);  
                                byte[] encoded = Base64.encodeBase64(content);
                                String encodedString = new String(encoded);                      
                                out.println("<center> <br><img src=\"data:image/jpeg;base64,"+ encodedString +"\" style='max-width: 300px; max-height: 300px' WIDTH=20% HEIGHT=20% /></center> ");
                            }
                        }
                }else{
                    Logger.getLogger(ViewFiles.class.getName()).log(Level.SEVERE, null, "Error: request is null");
                }
            }else{
                out.println("<center> <h1>Error couldn't connect to repository </h1></center>");
            }
               out.println("</body>");
               out.println("</html>");
               out.close();
               if(!this.disconnectRepository()){
                   System.err.println("Error: could not log");
               }
           }else{
               Logger.getLogger(ViewFiles.class.getName()).log(Level.SEVERE, null, "Error: response is null");
           }
       
    }
    
    /**
     * Make a connection to a remote repository.
     * 
     * @return true if the connection was successful, false otherwise
     */
    public boolean connectRepository(){
        try{
            url = "http://localhost:8080/rmi";
            repository = JcrUtils.getRepository(url);
            jcrSession = repository.login(new SimpleCredentials("admin", "admin".toCharArray()) , "default");
            root = jcrSession.getRootNode();
            appNode = root.getNode("storefiles");
            imagesNode = root.getNode("storefiles/images");
            musicNode = root.getNode("storefiles/music");
            documentsNode = root.getNode("storefiles/documents");
            othersNode = root.getNode("storefiles/others");
            return true;
        }catch(RepositoryException re){
            return false;
        }
    }
    
    /**
     * Save and close the current session.
     * 
     * @return true if session was close, false otherwise
     */
    public boolean disconnectRepository(){
         try {         
            jcrSession.save();
            jcrSession.logout();
            return true;    
        }catch(RepositoryException re){
            return false;
        }
    }
    
    /**
     * Walk through a node of the tree getting the properties where which one is an image.
     * 
     * @param n node what contains the images
     * @return the string of a form on HTML which is for interact with the user
     * @throws RepositoryException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public String listFiles(Node n) throws RepositoryException, IOException{
        if(n != null){
            String result, name;
            name = "";
            result = "<form method=\"POST\" action=\"ViewFiles\"><select name=\"dropdown\" onchange=this.form.submit()>";
            result = result + "<option selected=\"selected\" disabled=\"disabled\">Select a picture</option>";
            Property aux;
            PropertyIterator auxiliar1 = n.getProperties();
            if(auxiliar1.hasNext()){
                while(auxiliar1.getPosition() < auxiliar1.getSize()){
                    aux = auxiliar1.nextProperty();
                    name = aux.getName();
                    if(!name.equals("jcr:primaryType"))
                        result = result + "<option value=\"" + name + "\">" + name.substring(0,name.indexOf(".")) + "</option>"; 
                }
            }
            result = result + "</select></form>";
            return result;
        }else{
            return "<form method=\"POST\" action=\"ViewFiles\">"
                    + "<select name=\"dropdown\" onchange=this.form.submit()> "
                    + "<option value=\"null\"> </option>"
                    + "</select>"
                    + "</form>";
        }
    }
    
    /**
     * Walk through a node of the tree for get the InputStream associated with the image.
     * 
     * @param n node what contains the images
     * @param file_name name of the file for get the InputStream
     * @return InputStream of the image
     * @throws RepositoryException if a servlet-specific error occurs
     */
    private InputStream searchBufferImage(Node n, String file_name) throws RepositoryException {
            String name;
            name = "";
            InputStream is = null;
            Property aux;
            PropertyIterator auxiliar1 = n.getProperties();
            if(auxiliar1.hasNext()){
                while(auxiliar1.getPosition() < auxiliar1.getSize()){
                    aux = auxiliar1.nextProperty();
                    name = aux.getName();
                    if(!name.equals("jcr:primaryType"))
                        if(name.equals(file_name))
                            is = n.getProperty(name).getStream();
                }
            }
            return is;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (RepositoryException ex) {
            Logger.getLogger(ViewFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        } catch (RepositoryException ex) {
            Logger.getLogger(ViewFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
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