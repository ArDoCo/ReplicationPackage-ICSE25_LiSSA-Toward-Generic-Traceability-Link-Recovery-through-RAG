package edu.kit.ipd.sdq.mediastore.basic.config;

import java.io.File;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

public class Config {
	private static Map<String, EJB> ejbs;
	private static long timestamp = -1;
	private static boolean reconfigurable = true;
	
	public static void loadConfig() {
		File config = new File("C:\\ejbconfig.xml");
		//System.out.println("last timestamp : " + timestamp );
		//System.out.println("current timestamp : " + config.lastModified());
		if (config.lastModified() == timestamp) {
			System.out.println("config already up to date");
			return;
		}
		//System.out.println("loading config");
		XStream xstream = new XStream();
		Class[] types = { EJB.class, ProvidedInterface.class,
				RequiredInterface.class };
		xstream.processAnnotations(types);

		xstream.useAttributeFor(EJB.class, "name");
		ejbs = (Map<String, EJB>) xstream.fromXML(config);
		timestamp = config.lastModified();
		//System.out.println("Timestamp changed to " + timestamp);
	}

	
	public static boolean isReconfigurable() {
		return reconfigurable;
	}
	public static Map<String, EJB> getEJBs() {
		return ejbs;
	}
}
