package ru.coffeeplanter.newsfeed.data;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
//			configuration.addAnnotatedClass(News.class);
//			configuration.addAnnotatedClass(NewsCategory.class);
			return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build());
//			return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory(); // This line is simple, but non-recommended way of a SessionFactory object building
		}
		catch (Exception e) {
			System.err.println("Failed to create the SessionFactory object");
			e.printStackTrace();
			throw new RuntimeException("There was an error building the SessionFactory object");
		}	
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
}
