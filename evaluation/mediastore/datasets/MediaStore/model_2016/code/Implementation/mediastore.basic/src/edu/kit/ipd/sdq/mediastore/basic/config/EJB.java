package edu.kit.ipd.sdq.mediastore.basic.config;

import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EJB")
public class EJB {
	private String name;
	private String host;
	private String port;
	private String appName;
	private String moduleName;
	private String beanName;
	private Map<String, ProvidedInterface> providedInterfaces;
	private Map<String, RequiredInterface> requiredInterfaces;

	public EJB(String name, String host, String port, String appName, String moduleName, String beanName) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.appName = appName;
		this.moduleName = moduleName;
		this.beanName = beanName;
		providedInterfaces = new HashMap<String, ProvidedInterface>();
		requiredInterfaces = new HashMap<String, RequiredInterface>();
	}

	public void setProvidedInterfaces(Map<String, ProvidedInterface> pi) {
		this.providedInterfaces = pi;
	}

	public void setRequiredInterfaces(Map<String, RequiredInterface> ri) {
		this.requiredInterfaces = ri;
	}

	public RequiredInterface getRequiredInterface(String interfaceName) {
		return requiredInterfaces.get(interfaceName);
	}

	public ProvidedInterface getProvidedInterface(String interfaceName) {
		return providedInterfaces.get(interfaceName);
	}

	public void addRequiredInterface(RequiredInterface ri) {
		requiredInterfaces.put(ri.getName(), ri);
	}
	
	public void addProvidedInterface(ProvidedInterface pi) {
		providedInterfaces.put(pi.getName(), pi);
	}
	
	public String getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public String getName() {
		return name;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public String getBeanName() {
		return beanName;
	}

	@Override 
	public boolean equals(Object obj) {
		if (!(obj instanceof EJB))
			return false;
		
		if (obj == this)
			return true;
		
		EJB ejb = (EJB) obj;
		return (ejb.name.equals(name) &&
				ejb.host.equals(host) &&
				ejb.port.equals(port) &&
				ejb.appName.equals(appName) &&
				ejb.moduleName.equals(moduleName) &&
				ejb.beanName.equals(beanName));
	}
	
	@Override
	public String toString() {
		String result = "\n";
		result += "EJB : " + name + "\n";
		result += "Host : " + host + "\n";
		result += "Port : " + port + "\n";
		result += "AppName : " + appName + "\n";
		result += "RequiredInterfaces : \n";
		for (String s : requiredInterfaces.keySet()) {
			result += requiredInterfaces.get(s) + "\n";
		}
		result += "ProvidedInterfaces : \n";
		for (String s : providedInterfaces.keySet()) {
			result += providedInterfaces.get(s) + "\n";
		}
		result += "\n";
		return result;
	}

}
