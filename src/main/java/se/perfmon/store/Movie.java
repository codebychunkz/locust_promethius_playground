package se.perfmon.store;

public class Movie
{
    private long id;

    private String title;

    private String description;

    public Movie()
    {
    }
    
    public Movie(long id, String title, String description)
    {
	this.id = id;
	this.title = title;
	this.description = description;
    }
    
    public long getId()
    {
	return id;
    }

    public String getTitle()
    {
	return title;
    }

    public String getDescription()
    {
	return description;
    }
}
