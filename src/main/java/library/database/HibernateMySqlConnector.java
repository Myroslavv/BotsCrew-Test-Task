package library.database;

/**
 * This class represents connection to MySQL Data Management System.
 * @author Myroslav
 *
 */
public class HibernateMySqlConnector extends HibernateAbstractConnector 
{
	public HibernateMySqlConnector()
	{
		this.configurationFileName = "hibernate-mysql.cfg.xml";
	}
}
