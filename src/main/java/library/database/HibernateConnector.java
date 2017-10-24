package library.database;

import org.hibernate.SessionFactory;

/**
 * Hibernate-centered extension of basic database entry point.
 * @author Myroslav
 *
 */
public interface HibernateConnector extends DatabaseConnector<SessionFactory>
{
}
