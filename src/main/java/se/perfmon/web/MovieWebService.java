package se.perfmon.web;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import se.perfmon.exception.ApplicationException;
import se.perfmon.store.Movie;
import se.perfmon.store.MovieService;
import se.perfmon.store.MovieServiceImpl;

@Path("movies")
public class MovieWebService
{
    private static final Logger LOG = Logger.getLogger(MovieWebService.class.getName());
    
    private static final Summary requestLatency = Summary.build()
            .name("http_request_duration_seconds")
            .labelNames("type")
            .help("Request latency in seconds.")
            .register();
    
    private static final Counter requestFailures = Counter.build()
            .name("http_request_failures_total")
            .labelNames("type")
            .help("Request failures.")
            .register();

    @Inject
    private MovieService movieService;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMovies()
    {
	Response build = null;
	Summary.Timer requestTimer = requestLatency.labels("get").startTimer();
	try
	{
	    Collection<Movie> movies = movieService.list();
	    build = Response.ok(movies).build();
	}
	catch (ApplicationException e)
	{
	    LOG.log(Level.SEVERE, e.getMessage(), e);
	    build = Response.serverError().build();
	    requestFailures.labels("get").inc();
	}
	finally
	{
	    requestTimer.observeDuration();
	}

	return build;
    }

    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie movie)
    {
	Response build = null;
	Summary.Timer requestTimer = requestLatency.labels("create").startTimer();
	try
	{
	    movie = movieService.create(movie);
	    build = Response.ok(movie).build();
	}
	catch (ApplicationException e)
	{
	    LOG.log(Level.SEVERE, e.getMessage(), e);
	    build = Response.serverError().build();
	    requestFailures.labels("create").inc();
	}
	finally
	{
	    requestTimer.observeDuration();
	}

	return build;
    }
    
    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteMovie(long id)
    {
	Response build = null;
	Summary.Timer requestTimer = requestLatency.labels("delete").startTimer();
	try
	{
	    movieService.remove(id);
	    build = Response.ok().build();
	}
	catch (ApplicationException e)
	{
	    LOG.log(Level.SEVERE, e.getMessage(), e);
	    build = Response.serverError().build();
	    requestFailures.labels("delete").inc();
	}
	finally
	{
	    requestTimer.observeDuration();
	}

	return build;
    }
}
