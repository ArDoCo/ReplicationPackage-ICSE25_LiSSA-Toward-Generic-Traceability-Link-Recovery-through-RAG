package edu.kit.ipd.sdq.mediastore.web.beans;

import edu.kit.ipd.sdq.mediastore.basic.BaseEJB;
import edu.kit.ipd.sdq.mediastore.basic.interfaces.IBusinessInterface;
public class WebBeanManager {
	private static WebBean webBean = new WebBean();

	public static <T extends IBusinessInterface> T initRequiredInterface(String requiredInterface, Class<T> type) {
		return webBean.initRequiredInterface(requiredInterface, type);
	}
	
	public static boolean isLocal(String interfaceName) {
		return webBean.isLocal(interfaceName);
	}
	
	private static class WebBean extends BaseEJB {
		public WebBean() {
			super();
			ejbName = "webbean";
		}
		
		protected <T extends IBusinessInterface> T initRequiredInterface(String requiredInterface, Class<T> type) {
			return super.initRequiredInterface(requiredInterface, type);
		}
		
		protected boolean isLocal(String interfaceName) {
			return loadedInterfaces.get(interfaceName).isLocal();
		}

	}
	
}
