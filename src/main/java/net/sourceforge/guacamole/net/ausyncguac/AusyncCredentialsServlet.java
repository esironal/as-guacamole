package net.sourceforge.guacamole.net.ausyncguac;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.guacamole.net.ausyncguac.AusyncCredentials;

/**
 * This servlet is used to set the The download servlet that allows to retrieve data directly from the Raidar box.
 * It uses the BOSS database for authentication.
 */
public class AusyncCredentialsServlet extends HttpServlet {

    Logger logger = LoggerFactory.getLogger(AusyncCredentialsServlet.class);
    private AusyncCredentials m_credentials = new AusyncCredentials();


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer buffer = new StringBuffer();
        String line = null;
        try {
            //Read the JSON data from the POST data and fill them into the credentials class
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            m_credentials = gson.fromJson(reader.readLine(), AusyncCredentials.class);

            //Store the credentials into the HTTP session
            request.getSession(true).setAttribute("credentials", m_credentials);
        } catch (Exception e) {
            logger.error("Couldn't read the string from the post request: {}", e.toString());
        }
    }
}
