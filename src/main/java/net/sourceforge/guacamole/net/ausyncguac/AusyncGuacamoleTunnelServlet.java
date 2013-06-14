package net.sourceforge.guacamole.net.ausyncguac;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.sourceforge.guacamole.GuacamoleException;
import net.sourceforge.guacamole.net.GuacamoleSocket;
import net.sourceforge.guacamole.net.GuacamoleTunnel;
import net.sourceforge.guacamole.net.InetGuacamoleSocket;
import net.sourceforge.guacamole.protocol.ConfiguredGuacamoleSocket;
import net.sourceforge.guacamole.protocol.GuacamoleClientInformation;
import net.sourceforge.guacamole.protocol.GuacamoleConfiguration;
import net.sourceforge.guacamole.servlet.GuacamoleHTTPTunnelServlet;
import net.sourceforge.guacamole.servlet.GuacamoleSession;
import java.lang.Process;
import java.lang.ProcessBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.guacamole.net.ausyncguac.AusyncCredentials;

/**
 * The remote visualisation session servlet. It is based on Guacamole.
 */
public class AusyncGuacamoleTunnelServlet extends GuacamoleHTTPTunnelServlet {

    Logger logger = LoggerFactory.getLogger(AusyncGuacamoleTunnelServlet.class);

    @Override
    protected GuacamoleTunnel doConnect(HttpServletRequest request) throws GuacamoleException {


        //--------------------------------
        //   Perform the authentication
        //--------------------------------
        //Get the credentials from the HTTP session
        AusyncCredentials credentials = (AusyncCredentials)request.getSession(true).getAttribute("credentials");
        if (credentials == null) {
            logger.error("Couldn't find credentials in the HTTP session!");
            return null;
        }

        //Validate the credentials
        if (!credentials.isValid(request)) {
            logger.error("The credentials are not valid!");
            return null;   
        }


        //--------------------------------
        //   Connect to the VNC server
        //--------------------------------
        GuacamoleConfiguration config = new GuacamoleConfiguration();
        config.setProtocol("vnc");
        config.setParameter("hostname", "localhost");
        config.setParameter("port", "5901");
        config.setParameter("password", credentials.password);

        // Get client information
        GuacamoleClientInformation info = new GuacamoleClientInformation();
        
        // Set width if provided
        String width  = request.getParameter("width");
        if (width != null)
            info.setOptimalScreenWidth(Integer.parseInt(width));

        // Set height if provided
        String height = request.getParameter("height");
        if (height != null)
            info.setOptimalScreenHeight(Integer.parseInt(height));
           
        // Add video mimetypes
        String[] video_mimetypes = request.getParameterValues("video");
        if (video_mimetypes != null)
            info.getVideoMimetypes().addAll(Arrays.asList(video_mimetypes));

        // Connect to guacd
        GuacamoleSocket socket = new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket("localhost", 4822),
                config, info
        );

        // Establish the tunnel using the connected socket
        GuacamoleTunnel tunnel = new GuacamoleTunnel(socket);

        // Attach tunnel to session
        GuacamoleSession session = new GuacamoleSession(request.getSession(true));
        session.attachTunnel(tunnel);

        logger.info("HTTP tunnel established");

        // Return pre-attached tunnel
        return tunnel;
    }
}
