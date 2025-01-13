package edu.kit.ipd.sdq.mediastore.basic.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("RequiredInterface")
public class RequiredInterface {
	private String name; // e.g. IDownload
	private ProvidedInterface providedInterface; // e.g. IDownloadMediaAccess (name)

	
	public RequiredInterface(String name, ProvidedInterface providedInterface) {
		this.name = name;
		this.providedInterface = providedInterface;
	}
	
	public ProvidedInterface getProvidedInterface() {
		return providedInterface;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "\t name : " + name + "\n";
		result += "\t Provided Interface :\n\n" + providedInterface + "\n";
		return result;
	}

}
