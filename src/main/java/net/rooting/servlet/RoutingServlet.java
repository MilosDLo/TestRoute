package net.rooting.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RoutingServlet
 */
@WebServlet("/routingServlet")
public class RoutingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ACTION_GET_ROUTES = "getRoutes";

    /**
     * Default constructor. 
     */
    public RoutingServlet() {
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processRequest(request,response);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processRequest(request,response);
	}


	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String json = "";
		String action = request.getParameter("action");
		
		switch (action) {
		case ACTION_GET_ROUTES:
			
			
			
			break;

		default:
			break;
		}
		
		response.getWriter().write(json);
		response.getWriter().flush();
		
		
	}

}
