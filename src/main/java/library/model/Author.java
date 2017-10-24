package library.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Representation of <code>author</code> relation in database.
 * @author Myroslav
 *
 */
@Entity
@Table(name = "author",
		uniqueConstraints = 
		{
			@UniqueConstraint(columnNames = { "name" })
		})
public class Author 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	public Author() 
	{
	}
	
	public Author(String name)
	{
		this.name = name;
	}

	public long getId() 
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null
				|| !(obj instanceof Author))
			return false;
		
		if (obj == this)
			return true;
		
		return this.equalsTo((Author) obj);
	}
	
	private boolean equalsTo(Author author)
	{
		return this.name.equals(author.getName());
	}
	
	@Override
	public int hashCode()
	{
		int primeCoefficient = 23;
		int authorHashValue = 17;
		
		return getHashCode(authorHashValue, primeCoefficient);
	}
	
	private int getHashCode(int authorValue, int coefficient)
	{
		int result = authorValue;
		
		int nameHashValue = this.name.hashCode();
		result = result * coefficient + nameHashValue;
		
		return result;
	}
}
