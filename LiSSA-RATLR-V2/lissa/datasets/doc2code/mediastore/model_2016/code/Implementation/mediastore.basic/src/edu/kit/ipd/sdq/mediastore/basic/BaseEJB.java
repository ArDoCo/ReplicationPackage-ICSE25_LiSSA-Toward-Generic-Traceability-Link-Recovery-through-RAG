package edu.kit.ipd.sdq.mediastore.basic;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import edu.kit.ipd.sdq.mediastore.basic.config.Config;
import edu.kit.ipd.sdq.mediastore.basic.config.EJB;
import edu.kit.ipd.sdq.mediastore.basic.config.InterfaceDetails;
import edu.kit.ipd.sdq.mediastore.basic.config.ProvidedInterface;
import edu.kit.ipd.sdq.mediastore.basic.config.RequiredInterface;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IBusinessInterface;
import edu.kit.ipd.sdq.mediastore.basic.utils.JNDIUtil;

public abstract class BaseEJB {
	protected String ejbName = "";
	protected Map<String, InterfaceDetails> loadedInterfaces;
	protected EJB ejb;
	public BaseEJB() {
		loadedInterfaces = new HashMap<String, InterfaceDetails>();
	}

	protected <T extends IBusinessInterface> T initRequiredInterface(String requiredInterface, Class<T> type) {
		if (!Config.isReconfigurable() &&  Config.getEJBs() != null) { // if reconfiguration is not allowed, and ejbs was already initialized
			InterfaceDetails details = loadedInterfaces.get(requiredInterface);
			return type.cast(details.getBusinessInterface());
		}
		System.out.println(ejbName + " trying to load config");
		Config.loadConfig();
		ejb = Config.getEJBs().get(ejbName);
		RequiredInterface ri = ejb.getRequiredInterface(requiredInterface); //e.g IDownload
		ProvidedInterface pi = ri.getProvidedInterface(); //interface to load, e.g IDownloadMediaAccess
		
		
		if (loadedInterfaces.containsKey(requiredInterface)) { //If the required interface is already loaded
			InterfaceDetails details = loadedInterfaces.get(requiredInterface);
			ProvidedInterface loadedInterface = details.getProvidedInterface();
			EJB loadedEJB = details.getEJB();
			
			if(ejb.equals(loadedEJB)) { // if both ejbs are the same (same name, host, port, jndiName)
				if (loadedInterface.getName().equals(pi.getName())) { //If there's a match between the config and the loaded interface
		        	System.out.println("JNDI lookup unnecessary, returning loaded interface " + loadedInterface.getName());
					return type.cast(details.getBusinessInterface());
					
				}
			}
		}

		//Otherwise make a lookup
		EJB callee = pi.getProvidingEJB();
		boolean localCall = ejb.getAppName().equals(callee.getAppName()) && ejb.getHost().equals(callee.getHost());
		Object response = new Object();
		try {
			System.out.println("JNDI Lookup for " + pi.getName());
			response = JNDIUtil.find(pi, localCall);
		} catch (NamingException e) {
			e.printStackTrace();
		}

		T result = type.cast(response);

		loadedInterfaces.put(requiredInterface, new InterfaceDetails(result, pi, ejb, localCall));

		return result;

	}
	
	protected boolean isLocal(String interfaceName) {
		return loadedInterfaces.get(interfaceName).isLocal();
	}
}
