package net.sourceforge.guacamole.net.ausyncguac;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * The user credentials as they were set via the credentials servlet.
 * Provides a method to verify the credentials.
 */
public class AusyncCredentials implements java.io.Serializable {
    public String userID       = "";
    public String username     = "";
    public String password     = "";
    public String clientIP     = "";
    public String vblSessionID = "";


    AusyncCredentials() {
    }


    public Boolean isValid(HttpServletRequest request) {

        //Check that the client IP is the same
        if (!clientIP.equals(request.getRemoteAddr())) return false;

        //Get the user credentials from the VBL using the sessionID as identifier
        //TODO: Implementation

        //Check that the current username and password match the values in the
        //instance meta-data
        //TODO: Implementation

        return true;
    }
}