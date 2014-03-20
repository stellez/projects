/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.jcr.Repository;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
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
 *
 * @author xumakgt1
 */
@WebServlet(urlPatterns = {"/HelloForm"})
@MultipartConfig
public class HelloForm extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private final static Logger LOGGER = 
            Logger.getLogger(HelloForm.class.getCanonicalName());
      
    String url;
    Repository repository;
    Session jcrSession;
    private Node appNode;
    private Node imagesNode;
    private Node musicNode;
    private Node documentsNode;
    private Node othersNode;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        final Part filePart = request.getPart("file1");
        final String fileName = getFileName(filePart);
        
        InputStream filecontent = null;
        FileOutputStream fos = null;
        final PrintWriter writer = response.getWriter();
        
        try {
            /* TODO output your page here. You may use following sample code. */
            filecontent = filePart.getInputStream();
            InputStream getImage = filePart.getInputStream();
            insert(filecontent, fileName);
            
            
            String path = "http://localhost:8084/DownImg/copy" + fileName; 
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Image</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> Insert Image </h1>");
            //out.println(impresion(appNode));
            out.println(impresion(imagesNode));
            out.println("</body>");
            out.println("</html>");
            
            
        }catch(FileNotFoundException fne){
            writer.println("You either did not specify a file to upload or are "
                + "trying to upload a file to a protected or nonexistent "
                + "location.");
            writer.println("<br/> ERROR: " + fne.getMessage());
            LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}", 
                new Object[]{fne.getMessage()});
        
        } finally {
            out.close();
            if (filecontent != null) {
                filecontent.close();
            }
            /*if (writer != null) {
                writer.close();
            }*/
            
            /*File fichero = new File("/home/xumakgt1/apache-tomcat-7.0.41/webapps/ROOT/DownImg/copy" + fileName );
            if(fichero.delete()){
                System.out.println("El fichero ha sido borrado exitosamente");
            }else{
                System.out.println("El fichero no puede ser borado");
            }*/
        }
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
        } catch (Exception ex) {
            Logger.getLogger(HelloForm.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(HelloForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insert(InputStream is, String fn) throws Exception{
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
 
        //Remove
        //root.getNode("storefiles").remove();
        
        jcrSession.save();
        //jcrSession.logout();
    }
    
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
    
public String impresion(Node n) throws RepositoryException, IOException{
    String auxiliar = "";
    System.out.println("This is my print:" + n.getName());
    String output = n.getName() + ": <br><br>";
    Property aux;
    PropertyIterator auxiliar1 = n.getProperties();
    if(auxiliar1.hasNext()){
        while(auxiliar1.getPosition() < auxiliar1.getSize()){
            aux = auxiliar1.nextProperty();
            //aux1.next();
            auxiliar = aux.getName();
            InputStream re = n.getProperty(auxiliar).getStream();
            if(re.available() != 0){
                int j = re.available();
                byte[] imagen;
                imagen = new byte[j];
                //int read = re.read(imagen);
                FileOutputStream sta;
                if(!auxiliar.equals("jcr:primaryType")){
                     sta = new FileOutputStream("/home/xumakgt1/apache-tomcat-7.0.41/webapps/ROOT/DownImg/copy" + auxiliar);
                     sta.write(imagen);
                     output = output + "<img src=\"http://localhost:8084/DownImg/copy" + auxiliar + "\" WIDTH=140 HEIGHT=210 >" ;
                }
            }else 
                System.out.println("Archivo no disponible");
        }
    }
    jcrSession.save();
    jcrSession.logout();
     return output;

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