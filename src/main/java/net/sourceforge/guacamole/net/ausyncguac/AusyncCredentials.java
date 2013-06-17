package net.sourceforge.guacamole.net.ausyncguac;

import java.io.*;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The user credentials. They are read from the servelt context.
 * The class provides a method to verify the credentials.
 */
public class AusyncCredentials implements java.io.Serializable {

    Logger logger = LoggerFactory.getLogger(AusyncCredentials.class);

    private String userID       = "";
    private String username     = "";
    private String password     = "";
    private String clientIP     = "";
    private String vblSessionID = "";


    AusyncCredentials(ServletContext context) {
        //Load the credentials from the servlet context
        userID = context.getInitParameter("userID");
        username = context.getInitParameter("username");
        password = context.getInitParameter("password");
        clientIP = context.getInitParameter("clientIP");
        vblSessionID = context.getInitParameter("vblSessionID");
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

    public String getPassword() {
        return password;
    }
}