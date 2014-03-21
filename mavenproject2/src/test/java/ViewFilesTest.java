/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author xumakgt1
 */
public class ViewFilesTest {
    
    public ViewFilesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of processRequest method, of class ViewFiles.
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        ViewFiles instance = new ViewFiles();
        instance.processRequest(request, response);
    }

    /**
     * Test of listFiles method, of class ViewFiles.
     */
    @Test
    public void testListFiles() throws Exception {
        System.out.println("listFiles");
        Node n = null;
        ViewFiles instance = new ViewFiles();
        String expResult = "<form method=\"POST\" action=\"ViewFiles\">"
                    + "<select name=\"dropdown\" onchange=this.form.submit()> "
                    + "<option value=\"null\"> </option>"
                    + "</select>"
                    + "</form>";
        String result = instance.listFiles(n);
        assertEquals(expResult, result);
    }

    /**
     * Test of doGet method, of class ViewFiles.
     */
    @Test
    public void testDoGet() throws Exception {
        System.out.println("doGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        ViewFiles instance = new ViewFiles();
        instance.doGet(request, response);
    }

    /**
     * Test of doPost method, of class ViewFiles.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        ViewFiles instance = new ViewFiles();
        instance.doPost(request, response);
    }

    /**
     * Test of getServletInfo method, of class ViewFiles.
     */
    @Test
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        ViewFiles instance = new ViewFiles();
        String expResult = "Short description";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }
    
}
