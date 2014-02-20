package t3.core;

import org.apache.log4j.BasicConfigurator;
import org.glassfish.jersey.server.ResourceConfig;

import t3.repository.Models;

public class T3Application extends ResourceConfig{


	public T3Application() {
        packages("t3.rest");
        BasicConfigurator.configure();
        Models.initialize();
    }
	
}
