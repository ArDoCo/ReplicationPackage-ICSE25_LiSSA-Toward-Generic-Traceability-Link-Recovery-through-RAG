package edu.kit.ipd.sdq.mediastore.basic.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

public class Main {

	public static void main(String[] args) throws IOException {
		Map<String, EJB> ejbs = new HashMap<String, EJB>();

		EJB audiowatermarking = new EJB("audiowatermarking", "localhost", "3700", "mediastore.ear.audiowatermarking", "mediastore.ejb.audiowatermarking", "AudioWatermarkingImpl");
		EJB cache = new EJB("cache", "localhost", "3700", "mediastore.ear.cache", "mediastore.ejb.cache", "CacheImpl");
		EJB facade = new EJB("facade", "localhost", "3700", "mediastore.ear.facade", "mediastore.ejb.facade", "FacadeImpl");
		EJB mediaaccess = new EJB("mediaaccess", "localhost", "3700", "mediastore.ear.mediaaccess", "mediastore.ejb.mediaaccess", "MediaAccessImpl");
		EJB mediamanagement = new EJB("mediamanagement", "localhost", "3700", "mediastore.ear.mediamanagement", "mediastore.ejb.mediamanagement", "MediaManagementImpl");
		EJB packaging = new EJB("packaging", "localhost", "3700", "mediastore.ear.packaging", "mediastore.ejb.packaging", "PackagingImpl");
		EJB reencoder = new EJB("reencoder", "localhost", "3700", "mediastore.ear.reencoder", "mediastore.ejb.reencoder", "ReEncoderImpl");
		EJB tagwatermarking = new EJB("tagwatermarking", "localhost", "3700", "mediastore.ear.tagwatermarking", "mediastore.ejb.tagwatermarking", "TagWatermarkingImpl");
		EJB userdbadapter = new EJB("userdbadapter", "localhost", "3700", "mediastore.ear.userdbadapter", "mediastore.ejb.userdbadapter", "UserDBAdapterImpl");
		EJB usermanagement = new EJB("usermanagement", "localhost", "3700", "mediastore.ear.usermanagement", "mediastore.ejb.usermanagement", "UserManagementImpl");
		EJB webbean = new EJB("webbean", "localhost", "3700", "", "", "");
		
		//IDownloadAudioWatermarking
		ProvidedInterface idownloadaudiowatermarking = new ProvidedInterface("audiowatermarking", "IDownloadAudioWatermarking");
		audiowatermarking.addProvidedInterface(idownloadaudiowatermarking);
		
		//IDownloadCache
		ProvidedInterface idownloadcache = new ProvidedInterface("cache", "IDownloadCache");
		cache.addProvidedInterface(idownloadcache);
		
		//ICacheMaintenance
		ProvidedInterface icachemaintenance = new ProvidedInterface("cache", "ICacheMaintenance");
		cache.addProvidedInterface(icachemaintenance);
		
		//IFacade
		ProvidedInterface ifacade = new ProvidedInterface("facade", "IFacade");
		facade.addProvidedInterface(ifacade);
		
		
		//IMediaAccess
		ProvidedInterface imediaaccess = new ProvidedInterface("mediaaccess", "IMediaAccess");
		mediaaccess.addProvidedInterface(imediaaccess);
		
		//IDownloadMediaAccess
		ProvidedInterface idownloadmediaaccess = new ProvidedInterface("mediaaccess", "IDownloadMediaAccess");
		mediaaccess.addProvidedInterface(idownloadmediaaccess);
		
		
		//IMediaAccessMaintenance
		ProvidedInterface imediaaccessmaintenance = new ProvidedInterface("mediaaccess", "IMediaAccessMaintenance");
		mediaaccess.addProvidedInterface(imediaaccessmaintenance);
		

		//IMediaManagement
		ProvidedInterface imediamanagement = new ProvidedInterface("mediamanagement", "IMediaManagement");
		mediamanagement.addProvidedInterface(imediamanagement);
		
		//IPackaging
		ProvidedInterface ipackaging = new ProvidedInterface("packaging", "IPackaging");
		packaging.addProvidedInterface(ipackaging);
		
		//IDownloadReencoder
		ProvidedInterface idownloadreencoder = new ProvidedInterface("reencoder", "IDownloadReencoder");
		reencoder.addProvidedInterface(idownloadreencoder);
		
		
		//IDownloadTagWatermarking
		ProvidedInterface idownloadtagwatermarking = new ProvidedInterface("tagwatermarking", "IDownloadTagWatermarking");
		tagwatermarking.addProvidedInterface(idownloadtagwatermarking);
		
		//IUserDBAdapter
		ProvidedInterface iuserdbadapter = new ProvidedInterface("userdbadapter", "IUserDBAdapter");
		userdbadapter.addProvidedInterface(iuserdbadapter);
		
		//IUserManagement
		ProvidedInterface iusermanagement = new ProvidedInterface("usermanagement", "IUserManagement");
		usermanagement.addProvidedInterface(iusermanagement);
		
		
		
		//IDownload
		RequiredInterface RIdownload = new RequiredInterface("IDownload", idownloadmediaaccess);
		RequiredInterface RIusermanagement = new RequiredInterface("IUserManagement", iusermanagement);
		RequiredInterface RImediamanagement = new RequiredInterface("IMediaManagement", imediamanagement);
		RequiredInterface RImediaaccess = new RequiredInterface("IMediaAccess", imediaaccess);
		RequiredInterface RIpackaging = new RequiredInterface("IPackaging", ipackaging);
		RequiredInterface RIuserdbadapter = new RequiredInterface("IUserDBAdapter", iuserdbadapter);
		RequiredInterface RIfacade = new RequiredInterface("IFacade", ifacade);
		
		audiowatermarking.addRequiredInterface(RIdownload);
		
		cache.addRequiredInterface(RIdownload);
		
		facade.addRequiredInterface(RIusermanagement);
		facade.addRequiredInterface(RImediamanagement);

		
		mediamanagement.addRequiredInterface(RImediaaccess);
		mediamanagement.addRequiredInterface(RIpackaging);
		mediamanagement.addRequiredInterface(RIdownload);
		
		reencoder.addRequiredInterface(RIdownload);
		
		tagwatermarking.addRequiredInterface(RIdownload);
		
		usermanagement.addRequiredInterface(RIuserdbadapter);
		
		webbean.addRequiredInterface(RIfacade);
		
		ejbs.put(audiowatermarking.getName(), audiowatermarking);
		ejbs.put(cache.getName(), cache);
		ejbs.put(facade.getName(), facade);
		ejbs.put(mediaaccess.getName(), mediaaccess);
		ejbs.put(mediamanagement.getName(), mediamanagement);
		ejbs.put(packaging.getName(), packaging);
		ejbs.put(reencoder.getName(), reencoder);
		ejbs.put(tagwatermarking.getName(), tagwatermarking);
		ejbs.put(userdbadapter.getName(),userdbadapter);
		ejbs.put(usermanagement.getName(), usermanagement);
		ejbs.put(webbean.getName(), webbean);
		
		File xml = new File("ejbconfig.xml");
		xml.createNewFile();
		FileOutputStream fos = new FileOutputStream(xml);

		XStream xstream = new XStream();
		Class[] types = { EJB.class, ProvidedInterface.class,
				RequiredInterface.class };
		xstream.processAnnotations(types);
		xstream.useAttributeFor(EJB.class, "name");
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.toXML(ejbs, fos);

		Config.loadConfig();
		ejbs.clear();
		ejbs = Config.getEJBs();
		for (String s : ejbs.keySet()) {
			System.out.println(ejbs.get(s));
			
		}
	}
}
