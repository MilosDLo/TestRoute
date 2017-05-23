package net.rooting.Util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.metamodel.MetadataSources;

public class HibernateUtil {

//	private static final SessionFactory sessionFactory;
//	
//	static{
//		try {
//			// Create the SessionFactory from standard (hibernate.cfg.xml)
//			
//			// config file.
//			
//			sessionFactory = new Configuration().configure().buildSessionFactory(serviceRegistry)
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
	
static SessionFactory sessionFactory;
	
	protected void setup(){
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() //config settings from hibernate.cfg.xml
				.build();
		
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			StandardServiceRegistryBuilder.destroy(registry);

		}
	}
	
	public SessionFactory getSessionFactory(){
		return sessionFactory;
	}
	
	public void exit(){
		sessionFactory.close();
	}
	
}
