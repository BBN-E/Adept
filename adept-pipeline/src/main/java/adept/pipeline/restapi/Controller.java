package adept.pipeline.restapi;

import java.util.*;

import org.eclipse.jetty.plus.servlet.ServletHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.Connector;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.client.Address;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.UrlEncoded;

import org.eclipse.jetty.server.handler.HandlerCollection;


public class Controller implements Runnable
{
	
	private static List<String> servletnames = new ArrayList<String>();
	private static int port;
	
	private static Server server;
	private static HttpClient client;
	
	private static Thread serverThread;
	
	public Controller(String[] args)
	{
		// take command line arguments that specify what algorithm servers to run. This comes from the client
		port = Integer.parseInt(args[0]);
		for(int i=1; i<args.length; i++)
		{
			servletnames.add(args[i]);
		}
				
		serverThread = new Thread(this,"Server side thread");
		serverThread.start();
				
		/** TODO: Do things also to start up the client side (Note that in the actual implementation,
			**	this will need to be done first, since the information about what servlets to activate actually comes from
			**	the server side) */
	}
	
	public static void main(String[] args)
	{
		Controller controller = new Controller(args);		
	}	
		
		
		// bring the servers up in a separate thread. Associate multiple servlets with this server
		@Override
		public void run()
		{
			System.out.println("In the run method");
			
			try
			{
				List<ServletHolder> servletholders = new ArrayList<ServletHolder>();
				for(String s : servletnames)
				{
					//HttpServlet instance = Class.forName(s).getConstructor().newInstance();
					System.out.println("Class name: " + s);
					ServletHolder sh = new ServletHolder((HttpServlet)Class.forName(s).getConstructor().newInstance());
					servletholders.add(sh);
				}
						
				runServer(port,servletholders);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
		/**
		 * Start server.
		 *
		 * @param port the port
		 * @throws Exception the exception
		 */
		public void runServer(int port, List<ServletHolder> servletholders) throws Exception
		{
			System.out.println("In the runServer() method");
		    server = new Server(port);
		    	
		    HandlerCollection handlerCollection = new HandlerCollection();
		    ServletHandler[] handlers = new ServletHandler[2];
		    int i=0;
		    for(ServletHolder sh : servletholders)
		    {
		    	System.out.println("in for loop");
		    	ServletHandler handler = new ServletHandler();
		    	handler.addServletWithMapping(sh, "/*");
		    	handlers[i] = handler;
		    	i++;
		    	//handlerCollection.addHandler(handler);
		    }
		    
		    handlerCollection.setHandlers(handlers);
		    System.out.println("Number of handlers in the handler collection: " + handlerCollection.getHandlers().length);
		    System.out.println("just before server.setHandler()");
		    server.setHandler(handlerCollection);
		    server.start();
		    server.join();
		    try { System.in.read(); } catch( Throwable t ) {};
		}
		
		// inter thread communication(from child thread to main thread) to pass results obtained from various servlets to the client side

	
	
	
}