/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crewman.hibernateproject;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

@WebServlet(name = "LDAPServlet", urlPatterns = {"/LDAPServlet"})
public class LDAPServlet extends HttpServlet {
	
	private static final String minSupportedVersion = "1.0.0";
	
	/**
	 * Auto-generated SerialID used for Serialization
	 */
	private static final long serialVersionUID = -1484189364812256301L;

	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		return;
	}
	
	@Override
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		String plain_netid = null, plain_pass = null;
		
		/* Get encrypted Base64 parameters */
		String netid_in = (String) request.getParameter("netid");
		String pass_in = (String) request.getParameter("password");
                String version_in = (String) request.getParameter("version");
		
		if( netid_in == null || pass_in == null || version_in == null )	
			return;
		
		/* Decode from Base64 */
		byte[] enc_netid_bytes = Base64.decodeBase64(netid_in);
		byte[] enc_pass_bytes = Base64.decodeBase64(pass_in);
		
		/* Get our PrivateKey and decrypt parameters */
		PrivateKey privateKey = getPrivKey();
	    plain_netid = decrypt(enc_netid_bytes, privateKey);
	    plain_pass = decrypt(enc_pass_bytes, privateKey);
		PrintWriter out = response.getWriter();
                
                //Remove later
		if( plain_netid.equals("test") && plain_pass.equals("123") ) {

			/* If app version is lower than min supported version then just return */
			if( !checkVersion(version_in) )
				out.print("update");
                        
                        out.print("true");
			return;
		}
        
		
		
		/* Check if credentials are valid
		 * - If not an exception is thrown and we return
		 * - Otherwise, the search was successful and we first write
		 * 		true to the output stream then return 
		 * */
		try {
			search(plain_netid,plain_pass);
		} 
                catch (LDAPException e) {
			return;
		}
                catch(GeneralSecurityException e){
                    
                }
		out.print("true"); //Successful LDAP query 
	}
	
        /**
	 * Gets the current min supported version by returning an array where:
	 * 	 [0] is Major revision number
	 * 	 [1] is Minor revision number
	 * 	 [2] is Point revision number
	 * */
	private int[] getVersionArray( String ver ) {
		int[] intVerNums = new int[3];
		String[] verNums = ver.split("\\.");
		intVerNums[0] = Integer.parseInt(verNums[0]);
		intVerNums[1] = Integer.parseInt(verNums[1]);
		intVerNums[2] = Integer.parseInt(verNums[2]);
		return intVerNums;
	}
	
	private boolean checkVersion(String appVersion) {

	    int[] appVer = getVersionArray(appVersion);
	    int[] minVer = getVersionArray(minSupportedVersion);

	    for (int i = 0; i < minVer.length; i++)
	        if (appVer[i] != minVer[i])
	            return appVer[i] > minVer[i];

	    return true;
	}
        
	private PrivateKey getPrivKey() {
		ObjectInputStream inputStream;
		PrivateKey pk = null;
		
		try {
			inputStream = new ObjectInputStream(new FileInputStream("C:\\private.key"));
			pk = (PrivateKey) inputStream.readObject();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
                catch(ClassNotFoundException e){
                    e.printStackTrace();
                }
		
		return pk;
	}
	
	/**
	 * Decrypt text using private key.
	 * 
	 * @param text
	 *          :encrypted text
	 * @param key
	 *          :The private key
	 * @return plain text
	 * @throws java.lang.Exception
	 */
	private static String decrypt(byte[] text, PrivateKey key) {
		byte[] dectyptedText = null;
		try {
			final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");	// Get an RSA cipher object and print the provider

			/* Decrypt the text using the private key */
			cipher.init(Cipher.DECRYPT_MODE, key);	
			dectyptedText = cipher.doFinal(text);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new String(dectyptedText);
	}
	
    private static boolean search(String username, String password) throws GeneralSecurityException, LDAPException {
        
        String[] attributes = new String[] { "cn", "uid", "utaEmplID" };	// the attributes to search for
        
        /* Connect to LDAP */
        SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
        SSLSocketFactory socketFactory = sslUtil.createSSLSocketFactory();
        LDAPConnection ldap = new LDAPConnection(socketFactory, 
                					"ldap.cedar.uta.edu",
                					636,
                					"uid=" + username + ",cn=accounts,dc=uta,dc=edu",
                					password
        );
        
        /* Search LDAP */
        SearchResult sr = ldap.search(
                "cn=accounts,dc=uta,dc=edu", 
                SearchScope.SUB, 
                "(uid=" + username + ")",
                attributes
        );
        
        if (sr.getEntryCount() == 0)
            return false;
        
        return true;
    }
}
