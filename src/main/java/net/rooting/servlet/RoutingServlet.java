package net.rooting.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.maps.errors.ApiException;

import net.rooting.beans.RouteBean;

/**
 * Servlet implementation class RoutingServlet
 */
@WebServlet("/routingServlet")
public class RoutingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ACTION_GET_ROUTES = "getRoutes";
	private static final String ACTION_TEST_WITHOUT_OPTIM = "getWthOptim";

	
	@Inject
	RouteBean routeBean;
	
	
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
			
			String companyID = request.getParameter("companyID");
			String shipment_route = request.getParameter("shipment_route");
			String date = request.getParameter("date");
			
			json = routeBean.getRouteJson(companyID,shipment_route,date);						
			break;			
		case ACTION_TEST_WITHOUT_OPTIM:
			String companyIDT = request.getParameter("companyID");
			String shipment_routeT = request.getParameter("shipment_route");
			String dateT = request.getParameter("date");
			
			json = routeBean.getTestWithoutOptim(companyIDT,shipment_routeT,dateT);
			break;
		default:
			break;
		}
		
		response.getWriter().write(json);
		response.getWriter().flush();
		
		
	}

}
