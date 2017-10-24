package library.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

/**
 * Representation of <code>book</code> relation in database.
 * The owner of unidirectional many-to-many relationship 
 * between <code>book</code> and <code>author</code>.
 * @author Myroslav
 *
 */
@Entity
@Table(name = "book")
public class Book 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "title", length = 50, nullable = false)
	private String title;
	
	@ManyToMany(fetch = FetchType.EAGER,
			cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "book_author",
			joinColumns = { @JoinColumn(name = "book_id") },
			inverseJoinColumns = { @JoinColumn(name = "author_id") })
	private Set<Author> authors = new HashSet<>();

	public Book()
	{
	}
	
	public Book(String title)
	{
		this.title = title;
	}
	
	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public Set<Author> getAuthors() 
	{
		return new HashSet<Author>(authors);
	}

	public void addAuthor(Author author) 
	{
		authors.add(author);
	}

	public void removeAuthor(Author author)
	{
		authors.remove(author);
	}
	
	public long getId() 
	{
		return id;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null
				|| !(obj instanceof Book))
			return false;
		
		if (obj == this)
			return true;
		
		return equalsTo((Book) obj);
	}
	
	private boolean equalsTo(Book book)
	{
		boolean titlesAreEqual = this.title.equals(book.getTitle());
		boolean authorsAreTheSame = this.authors.equals(book.getAuthors());
		
		return titlesAreEqual && authorsAreTheSame;
	}
	
	@Override
	public int hashCode()
	{
		int primeCoefficient = 19;
		int bookHashValue = 11;
		
		return getHashCode(bookHashValue, primeCoefficient);
	}
	
	private int getHashCode(int bookValue, int coefficient)
	{
		int result = bookValue;
		int titleHashValue = this.title.hashCode();
		int authorsHashValue = this.authors.hashCode();
		
		result = result * coefficient + titleHashValue;
		result = result * coefficient + authorsHashValue;
		
		return result;
	}
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("\"%s\" by ", this.title));
		builder.append(getAuthorsString());
		
		return builder.toString();
	}
	
	private String getAuthorsString()
	{
		return this.authors.stream()
				.map(author -> author.getName())
				.collect(Collectors.joining(", "));
	}
}
