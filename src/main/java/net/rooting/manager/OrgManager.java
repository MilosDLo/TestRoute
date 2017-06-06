package net.rooting.manager;

import java.util.List;
import java.util.Map;

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
	
	
//	public List<Org>getAllOrgs(){		
//		return entityManager.createQuery("select o from Org AS o where o.extCode ='2206277'",Org.class).getResultList();
//	}
	

	
	@SuppressWarnings("unchecked")
	public List<Org> getOrgsInRoute(String companyID,String shipment_route,String date){
		
		List<Long> l = getOrgIdsFromOrdersInRoute(companyID,shipment_route,date);
	
		String query = "select o1 from Org AS o1 where o1.id in (:ids)";
		Query q = entityManager.createQuery(query);
		q.setParameter("ids", l);
		
		return (List<Org>)q.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Long> getOrgIdsFromOrdersInRoute(String companyID,String shipment_route,String date){
		String query = "select o2.orgId from Order AS o2 "
				+"where o2.companyId=:cID and o2.statusId in (2,3) and o2.orderTypeId=15 and o2.orderDay=:oDate and o2.shipmentRoute=:spr";
		
		Query q = entityManager.createQuery(query);
		q.setParameter("cID", Long.parseLong(companyID));
		q.setParameter("oDate", Long.parseLong(date));
		q.setParameter("spr", shipment_route);
					
		return (List<Long>)q.getResultList();		
	}


	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getInfoForEachRoute(String companyID,String route,String date){
		String query = "select org.name, org.address1, o2.shipmentRoute, org.longitude, org.latitude, o2.weightNet "
				+ "FROM Order o2,Org org "
				+ "where o2.orgId=org.id and o2.companyId=:cID and o2.statusId in (2,3) and o2.orderTypeId=15 and o2.orderDay=:oDate and o2.shipmentRoute=:sRoute";
		Query q = entityManager.createQuery(query);
		q.setParameter("cID", Long.parseLong(companyID));
		q.setParameter("oDate", Long.parseLong(date));
		q.setParameter("sRoute", route);
		return (List<Map<String,Object>>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getRoutesName(String companyID,String date){
		String query = "select DISTINCT o2.shipmentRoute from Order AS o2 where o2.companyId=:cID and o2.statusId in (2,3) and o2.orderTypeId=15 and o2.orderDay=:oDate";
		Query q = entityManager.createQuery(query);
		q.setParameter("cID", Long.parseLong(companyID));
		q.setParameter("oDate", Long.parseLong(date));
		return (List<String>)q.getResultList();
	}



	public long getTotalWeightInKg(String route,String companyID,String date) {
		String query = "select sum(o2.weightNet) from Order AS o2 where o2.shipmentRoute=:sRoute and o2.companyId=:cID and o2.statusId in (2,3) and o2.orderTypeId=15 and o2.orderDay=:oDate";
		Query q = entityManager.createQuery(query);
		q.setParameter("cID", Long.parseLong(companyID));
		q.setParameter("oDate", Long.parseLong(date));
		q.setParameter("sRoute", route);
		return ((long)q.getSingleResult())/100000; //kg
	}


	public Org getStorageOrgInRoute(String companyID,String route) {
		String query = "select sr.storageId FROM ShipmentRoute sr where sr.companyId=:cID and sr.extCode=:route";
		Query q = entityManager.createQuery(query);
		q.setParameter("cID", Long.parseLong(companyID));
		q.setParameter("route", route);
		String storage_id = (String)q.getSingleResult();
		
		Org storage = getStorageOrg(storage_id);  //-------------menjaj,ovo je hardkodovano!!!! 
		return storage;
	}


	private Org getStorageOrg(String storage_id) {
		if (storage_id == null || storage_id.equals("null") || storage_id.isEmpty()) {
			return null;
		}
		
		switch (storage_id) {
		case "944":
			
			return getNewOrg(45329929100L,19768242600L); //DC Novi sad    nasao sam nesto,valjda je ok
			
		case "932":
			return getNewOrg(44928379500L,20439570000L); //Zemun
			
		case "926":
			return getNewOrg(44928379500L,20439570000L); //Padinska skela

		case "929":
			return getNewOrg(43719219400L,20693052400L);   //DC Kraljevo   nasao sam DC neki u kraljevu,vrv nije to ali ce posluziti
			
		case "1476":
			return getNewOrg(44928379500L,20437381300L); //Niska mlekara (deki rece isto kao nis) 
			
		case "935":
			return getNewOrg(44928379500L,20437381300L); //DC Nis
			
		case "941":
			return getNewOrg(43895321700L,22289596800L); //Zajecar
			
		case "938":
			return getNewOrg(46065751700L,19684089900L); //DC Subotica    (opet nije dobra tacka,nasao samo imlek radnju)
			
			
		default:
			return null;
		}
		
	}


	private Org getNewOrg(Long lat, Long lng) {
		Org org = new Org();
		org.setLatitude(lat);
		org.setLongitude(lng);
		return org;		
	}
	
	
//	@SuppressWarnings("unchecked")
//	public List<Object[]> getRouteNameAndCount(String companyID, String date) {
//		String query = "select count(o2), o2.shipmentRoute from Order AS o2 where o2.companyId=:cID and o2.statusId in (2,3) and o2.orderTypeId=15 and o2.orderDay=:oDate group by o2.shipmentRoute";
//		Query q = entityManager.createQuery(query);
//		q.setParameter("cID", Long.parseLong(companyID));
//		q.setParameter("oDate", Long.parseLong(date));
//		return (List<Object[]>)q.getResultList();		
//	}	
	
	
	
	
	
	
}
