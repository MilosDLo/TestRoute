package net.rooting.manager;

import java.util.List;


import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.rooting.domain.Order;
import net.rooting.domain.Org;

//import net.esteh.mobile.domain.Org;

@Dependent
public class OrgManager {

	@PersistenceContext
	EntityManager entityManager;
	
	
	
	
	public List<Org>getAllOrgs(){		
		return entityManager.createQuery("select o from Org AS o where o.extCode ='2206277'",Org.class).getResultList();
	}
	

	
	@SuppressWarnings("unchecked")
	public List<Org> getOrgsInRoute(String companyID,String shipment_route,String date){
		
		List<Long> l = getOrgIdsFromOrders(companyID,shipment_route,date);
	
		String query = "select o1 from Org AS o1 where o1.id in (:ids)";
		Query q = entityManager.createQuery(query);
		q.setParameter("ids", l);
		q.toString();
		
		return (List<Org>)q.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Long> getOrgIdsFromOrders(String companyID,String shipment_route,String date){
		String query = "select o2.orgId from Order AS o2 "
				+"where o2.companyId=:cID and o2.statusId in (2,3) and o2.orderTypeId=15 and o2.orderDay=:oDate and o2.shipmentRoute=:spr";
		
		Query q = entityManager.createQuery(query);
		q.setParameter("cID", Long.parseLong(companyID));
		q.setParameter("oDate", Long.parseLong(date));
		q.setParameter("spr", shipment_route);
					
		return (List<Long>)q.getResultList();
		
	}	
	
}
