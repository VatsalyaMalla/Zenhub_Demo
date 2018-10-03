package com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.common.factory;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.InvalidNameException;

import com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.NotesAdapter;
import com.ibm.mobilefirst.industrial.materialsinspect.adapter.notes.common.factory.NotesAdapterFactory;
import com.ibm.mobilefirst.industrial.materialsinspect.api.notes.constants.ServiceConstants;

public class NotesAdapterFactory {

	
	static Logger logger = Logger.getLogger(NotesAdapterFactory.class.getName());
	static Properties properties = null;
	static Map adapterMap = new HashMap<String, NotesAdapter>();

	
	//threadsafe inner class to hold factory instance
	private static class FactoryHolder {
		static final NotesAdapterFactory instance = new NotesAdapterFactory();
	}
	


	protected NotesAdapterFactory(){
	}

	/**
	 * Get an instance of the Factory
	 * @return
	 */
	public static NotesAdapterFactory getInstance() {		
		logger.info("NotesAdapterFactory getInstance() ...");

		return FactoryHolder.instance;
	}


	/**
	 * Return instance of adapter class, if its called first time, it will create adapter or else return the
	 * existing adapter instance.
	 * @param adapterName
	 * @return
	 */
	public static NotesAdapter getNotesAdapter(String adapterName) {

		logger.info(">>--getMediaAdapter--start--");
		
		if (properties!=null && adapterMap.get(adapterName) != null)
		{	
			logger.info(">>--getNotesAdapter--end--return adapter map");
			return (NotesAdapter)adapterMap.get(adapterName);

		}
		else if (properties==null)
		{
			readPropertiesFileAndCreateAdapter(ServiceConstants.ADAPTER_PROPERTY_FILE,adapterName);
			logger.info(">>--getNotesAdapter--end");
			return (NotesAdapter)adapterMap.get(adapterName);
		}
		else
		{			
			createAdapter(adapterName);	
			logger.info(">>--getNotesAdapter--end-- created adapter, without read of properties");
			return (NotesAdapter)adapterMap.get(adapterName);
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
					logger.info("NotesAdapterFactory: Error Reading properties file: " + e.getMessage());
					printException(e,"readPropertiesFileAndCreateAdapter MediaAdapter");
				}
			}
			createAdapter(adapterName);
		}
	}

	/** *
	 * Create Instance of Notes adapter, based on parameter passed
	 * @param adapterName
	 */
	private synchronized static void createAdapter(String adapterName) {
		logger.info("--inside createAdapter--");
		NotesAdapter adapter=null;
		if (adapterMap.get(adapterName) == null)
		{
			try {
				String adapterClassName = properties.getProperty(adapterName);
			
				Class<?> clazz = Class.forName(adapterClassName);
				Object object = clazz.newInstance();
				if (object instanceof NotesAdapter) {
					adapter = (NotesAdapter)object;
					
					adapterMap.put(adapterName, adapter);
					logger.info("--created New Adapter and added in adapterMap--");
				}
				else {
					throw new InvalidNameException("NotesAdapterFactory: Invalid class name for property: " + adapterName);
				}


			}
			catch (Exception e) {
				logger.info("NotesAdapterFactory: Error creating Adapter Class: " + e.getMessage());
				printException(e,"createAdapter NotesAdapter");
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
