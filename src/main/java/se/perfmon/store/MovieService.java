package se.perfmon.store;

import java.util.Collection;

import se.perfmon.exception.ApplicationException;

public interface MovieService
{
    public Collection<Movie> list() throws ApplicationException;
    
    public Movie create(Movie movie) throws ApplicationException;
    
    public void remove(long id) throws ApplicationException;
}
