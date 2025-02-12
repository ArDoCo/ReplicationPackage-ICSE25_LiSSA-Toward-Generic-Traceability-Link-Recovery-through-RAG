package edu.kit.ipd.sdq.mediastore.basic.config;

import edu.kit.ipd.sdq.mediastore.basic.interfaces.IBusinessInterface;

public class InterfaceDetails {
	private IBusinessInterface bi;
	private ProvidedInterface pi;
	private EJB ejb;
	private boolean local;
	public InterfaceDetails(IBusinessInterface bi, ProvidedInterface pi, EJB ejb, boolean local) {
		this.bi = bi;
		this.pi = pi;
		this.ejb = ejb;
		this.local = local;
	}
	
	public IBusinessInterface getBusinessInterface() {
		return bi;
	}
	
	public ProvidedInterface getProvidedInterface() {
		return pi;
	}
	
	public EJB getEJB() {
		return ejb;
	}
	
	public boolean isLocal() {
		return local;
	}
}
