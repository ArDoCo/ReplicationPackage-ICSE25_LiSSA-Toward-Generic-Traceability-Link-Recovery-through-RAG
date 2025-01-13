package edu.kit.ipd.sdq.mediastore.basic.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ProvidedInterface")
public class ProvidedInterface {
	private String providingEJBName; //e.g mediaaccess
	private String name; //e.g. IDownloadMediaAccess
	public ProvidedInterface(String providingEJBName, String name) {
		this.providingEJBName = providingEJBName;
		this.name = name;
	}

	public String getFullName() {
		return "edu.kit.ipd.sdq.mediastore.basic.interfaces." + name;
	}


	public String getName() {
		return name;
	}

	
	public String getProvidingEJBName() {
		return providingEJBName;
	}
	
	public EJB getProvidingEJB() {
		return Config.getEJBs().get(providingEJBName);
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "\t Providing EJB : " + providingEJBName + "\n";
		result += "\t name : " + name + "\n";
		return result;
	}
}
