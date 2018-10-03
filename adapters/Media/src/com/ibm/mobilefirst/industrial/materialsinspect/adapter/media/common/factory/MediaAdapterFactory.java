package com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.common.factory;


import java.io.File;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.InvalidNameException;

import com.ibm.mobilefirst.industrial.materialsinspect.adapter.media.MediaAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.api.media.constants.ServiceConstants;

import java.util.HashMap;
import java.util.Map;

public class MediaAdapterFactory {
	
	static Logger logger = Logger.getLogger(MediaAdapterFactory.class.getName());
	static Properties properties = null;
	static Map adapterMap = new HashMap<String, MediaAdapter>();

	
	//threadsafe inner class to hold factory instance
	private static class FactoryHolder {
		static final MediaAdapterFactory instance = new MediaAdapterFactory();
	}
	


	protected MediaAdapterFactory(){
	}

	/**
	 * Get an instance of the Factory
	 * @return
	 */
	public static MediaAdapterFactory getInstance() {		
		logger.info("MediaAdapterFactory getInstance() ...");

		return FactoryHolder.instance;
	}


	/**
	 * Return instance of adapter class, if its called first time, it will create adapter or else return the
	 * existing adapter instance.
	 * @param adapterName
	 * @return
	 */
	public static MediaAdapter getMediaAdapter(String adapterName) {

		logger.info(">>--getMediaAdapter--start--");
		
		if (properties!=null && adapterMap.get(adapterName) != null)
		{	
			logger.info(">>--getMediaAdapter--end--return adapter map");
			return (MediaAdapter)adapterMap.get(adapterName);

		}
		else if (properties==null)
		{
			readPropertiesFileAndCreateAdapter(ServiceConstants.ADAPTER_PROPERTY_FILE,adapterName);
			logger.info(">>--getMediaAdapter--end");
			return (MediaAdapter)adapterMap.get(adapterName);
		}
		else
		{			
			createAdapter(adapterName);	
			logger.info(">>--getMediaAdapter--end-- created adapter, without read of properties");
			return (MediaAdapter)adapterMap.get(adapterName);
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
					logger.info("MediaAdapterFactory: Error Reading properties file: " + e.getMessage());
					printException(e,"readPropertiesFileAndCreateAdapter MediaAdapter");
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
		MediaAdapter adapter=null;
		if (adapterMap.get(adapterName) == null)
		{
			try {
				String adapterClassName = properties.getProperty(adapterName);
			
				Class<?> clazz = Class.forName(adapterClassName);
				Object object = clazz.newInstance();
				if (object instanceof MediaAdapter) {
					adapter = (MediaAdapter)object;
					
					adapterMap.put(adapterName, adapter);
					logger.info("--created New Adapter and added in adapterMap--");
				}
				else {
					throw new InvalidNameException("MediaAdapterFactory: Invalid class name for property: " + adapterName);
				}


			}
			catch (Exception e) {
				logger.info("MediaAdapterFactory: Error creating Adapter Class: " + e.getMessage());
				printException(e,"createAdapter MediaAdapter");
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