package se.perfmon.store;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;
import javax.sql.DataSource;

import se.perfmon.exception.ApplicationException;

public class MovieServiceImpl implements MovieService
{
    @Inject
    private DataSource dataSource;

    public Collection<Movie> list() throws ApplicationException
    {

	Collection<Movie> movies = Collections.emptyList();
	try (Connection conn = getConnection())
	{
	    try
	    {
		movies = new MovieStore(conn).list();
	    }
	    catch (SQLException e)
	    {
		conn.rollback();
		throw e;
	    }
	    conn.commit();
	}
	catch (Exception e)
	{
	    throw new ApplicationException(e);
	}

	return movies;
    }

    public Movie create(Movie movie) throws ApplicationException
    {
	try (Connection conn = getConnection())
	{
	    try
	    {
		movie = new MovieStore(conn).create(movie);
	    }
	    catch (SQLException e)
	    {
		conn.rollback();
		throw e;
	    }
	    conn.commit();
	}
	catch (Exception e)
	{
	    throw new ApplicationException(e);
	}

	return movie;
    }

    public void remove(long id) throws ApplicationException
    {
	try (Connection conn = getConnection())
	{
	    try
	    {
		new MovieStore(conn).remove(id);
	    }
	    catch (SQLException e)
	    {
		conn.rollback();
		throw e;
	    }
	    conn.commit();
	}
	catch (Exception e)
	{
	    throw new ApplicationException(e);
	}
    }

    private final Connection getConnection() throws SQLException
    {
	return dataSource.getConnection();
    }
}
