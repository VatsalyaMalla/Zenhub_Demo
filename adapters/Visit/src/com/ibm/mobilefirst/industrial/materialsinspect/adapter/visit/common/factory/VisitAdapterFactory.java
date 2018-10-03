package com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit.common.factory;


import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.InvalidNameException;

import com.ibm.mobilefirst.industrial.materialsinspect.adapter.visit.VisitAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.visit.constants.ServiceConstants;

import java.util.HashMap;
import java.util.Map;

public class VisitAdapterFactory {
	
	static Logger logger = Logger.getLogger(VisitAdapterFactory.class.getName());
	static Properties properties = null;
	static Map adapterMap = new HashMap<String, VisitAdapter>();

	
	//threadsafe inner class to hold factory instance
	private static class FactoryHolder {
		static final VisitAdapterFactory instance = new VisitAdapterFactory();
	}
	


	protected VisitAdapterFactory(){
	}

	/**
	 * Get an instance of the Factory
	 * @return
	 */
	public static VisitAdapterFactory getInstance() {		
		logger.info("VisitAdapterFactory getInstance() ...");

		return FactoryHolder.instance;
	}


	/**
	 * Return instance of adapter class, if its called first time, it will create adapter or else return the
	 * existing adapter instance.
	 * @param adapterName
	 * @return
	 */
	public static VisitAdapter getVisitAdapter(String adapterName) {

		logger.info(">>--getVisitAdapter--start--");
		
		if (properties!=null && adapterMap.get(adapterName) != null)
		{	
			logger.info(">>--getVisitAdapter--end--return adapter map");
			return (VisitAdapter)adapterMap.get(adapterName);

		}
		else if (properties==null)
		{
			readPropertiesFileAndCreateAdapter(ServiceConstants.ADAPTER_PROPERTY_FILE,adapterName);
			logger.info(">>--getVisitAdapter--end");
			return (VisitAdapter)adapterMap.get(adapterName);
		}
		else
		{			
			createAdapter(adapterName);	
			logger.info(">>--getVisitAdapter--end-- created adapter, without read of properties");
			return (VisitAdapter)adapterMap.get(adapterName);
		}
		
		
	}

	/**
	 * Read properties file to get specific adapter class name
	 * @param fileName
	 */
	private synchronized static void readPropertiesFileAndCreateAdapter(String fileName, String adapterName) {
		{
			if(properties == null) {
				properties = new Properties();	
				try{

					File adapterProperties = new File(fileName);
					FileReader reader = new FileReader(adapterProperties);
					properties.load(reader);
					logger.info("--Reading  properties file complete--");

				}
				catch (Exception e) {
					logger.info("VisitAdapterFactory: Error Reading properties file: " + e.getMessage());
					printException(e,"readPropertiesFileAndCreateAdapter VisitAdapter");
				}
			}
			createAdapter(adapterName);
		}
	}

	/** *
	 * Create Instance of Location adapter, based on parameter passed
	 * @param adapterName
	 */
	private synchronized static void createAdapter(String adapterName) {
		logger.info("--inside createAdapter--");
		VisitAdapter adapter=null;
		if (adapterMap.get(adapterName) == null)
		{
			try {
				String adapterClassName = properties.getProperty(adapterName);
			
				Class<?> clazz = Class.forName(adapterClassName);
				Object object = clazz.newInstance();
				if (object instanceof VisitAdapter) {
					adapter = (VisitAdapter)object;
					
					adapterMap.put(adapterName, adapter);
					logger.info("--created New Adapter and added in adapterMap--");
				}
				else {
					throw new InvalidNameException("VisitAdapterFactory: Invalid class name for property: " + adapterName);
				}


			}
			catch (Exception e) {
				logger.info("VisitAdapterFactory: Error creating Adapter Class: " + e.getMessage());
				printException(e,"createAdapter VisitAdapter");
			}
		}

	}
static void printException(Exception e,String methodName) {
		
		logger.severe("Exception in"+methodName);
		StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    logger.severe(sb.toString());
	}
}