package bluecrystal.service.service;

public class ServiceFactory {
	private static ServiceFacade serviceFacade;

	public static ServiceFacade getServiceFacade() {
		if(serviceFacade == null){
			serviceFacade = new ServiceFacade();
		}
		return serviceFacade;
	}
	
	

}
