/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.InputStream;
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
public class InsertFileTest {
    
    public InsertFileTest() {
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
     * Test of processRequest method, of class InsertFile.
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        InsertFile instance = new InsertFile();
        instance.processRequest(request, response);
    }

    /**
     * Test of doGet method, of class InsertFile.
     */
    @Test
    public void testDoGet() throws Exception {
        System.out.println("doGet");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        InsertFile instance = new InsertFile();
        instance.doGet(request, response);
    }

    /**
     * Test of doPost method, of class InsertFile.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost");
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        InsertFile instance = new InsertFile();
        instance.doPost(request, response);
    }

    /**
     * Test of insert method, of class InsertFile.
     */
    @Test
    public void testInsert() throws Exception {
        System.out.println("insert");
        InputStream is = null;
        String fn = "";
        InsertFile instance = new InsertFile();
        instance.insert(is, fn);
    }

    /**
     * Test of getServletInfo method, of class InsertFile.
     */
    @Test
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        InsertFile instance = new InsertFile();
        String expResult = "Short description";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }
    
}
