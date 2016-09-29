package se.perfmon.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MovieStore
{
    private final Connection connection;

    public MovieStore(Connection connection)
    {
	this.connection = connection;
    }

    public Collection<Movie> list() throws SQLException
    {

	Collection<Movie> movies = new ArrayList<Movie>();
	try (PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM Movies"))
	{
	    try (ResultSet result = stmnt.executeQuery())
	    {
		while (result.next())
		{
		    long id = result.getLong("id");
		    String title = result.getString("title");
		    String desc = result.getString("description");

		    movies.add(new Movie(id, title, desc));
		}
	    }
	}
	return Collections.unmodifiableCollection(movies);
    }

    public Movie create(Movie movie) throws SQLException
    {
	try (PreparedStatement stmnt = connection.prepareStatement("INSERT INTO Movies (title, description) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS))
	{
	    stmnt.setString(1, movie.getTitle());
	    stmnt.setString(2, movie.getDescription());
	    stmnt.executeUpdate();
	    
	    try(ResultSet generatedKeys = stmnt.getGeneratedKeys()) {
		if(generatedKeys.next()) {
		    long id = generatedKeys.getLong(1);
		    movie = new Movie(id, movie.getTitle(), movie.getDescription());
		} else {
		    
		}
	    }
	}
	
	return movie;
    }

    public void remove(long id) throws SQLException
    {
	try (PreparedStatement stmnt = connection.prepareStatement("DELETE FROM Movies WHERE id=?"))
	{
	    stmnt.setLong(1, id);
	    stmnt.executeUpdate();
	}
    }
}
