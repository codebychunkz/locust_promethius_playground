package se.perfmon;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import se.perfmon.web.MovieWebService;

public class Application extends javax.ws.rs.core.Application
{
    @Override
    public Set<Class<?>> getClasses()
    {
	Set<Class<?>> ret = new HashSet<Class<?>>();
	ret.add(MovieWebService.class);
	
	return Collections.unmodifiableSet(ret);
    }
}
