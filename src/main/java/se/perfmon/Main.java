package se.perfmon;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.weld.environment.servlet.Listener;

import io.prometheus.client.exporter.MetricsServlet;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

public class Main
{

    public static void main(String[] args) throws Exception
    {
	Server dbServer = setupDatabase();
	dbServer.start();
	
	Builder builder = Undertow.builder().addHttpListener(8080, "localhost");

	ResteasyDeployment deployment = new ResteasyDeployment();
	deployment.setApplicationClass(Application.class.getName());
	deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");

	UndertowJaxrsServer server = new UndertowJaxrsServer();
	DeploymentInfo di = server.undertowDeployment(deployment, "/");

	di.setClassLoader(Main.class.getClassLoader())
		.setContextPath("/")
	        .setDeploymentName("Metrics test app")
	        .addServlets(Servlets.servlet(MetricsServlet.class)
	        	.addMapping("/metrics"))
	        .addListener(Servlets.listener(Listener.class));

	server.start(builder);
	server.deploy(di);
    }

    private static Server setupDatabase() throws SQLException
    {
	Server server = Server.createTcpServer("-tcpPort", "9123", "-tcpAllowOthers");
	return server;
    }

}