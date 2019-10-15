/**
 * 
 */
package org.dragonli.service.dubbosupport;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.EchoService;

/**
 * @author kangzhijie
 *
 */
public class DubboBeanManager implements IHostEcho {

	private final AtomicBoolean hadSuccessInit = new AtomicBoolean(false);
	private volatile ReferenceConfig<Object> reference; 
	public static Logger logger = Logger.getLogger(DubboBeanManager.class);
	public ReferenceConfig<Object> getReference() {
		return reference;
	}

	private volatile EchoService bean;
	
	public EchoService getBean() {
		return bean;
	}

	public boolean isHadSuccessInit() {
		return hadSuccessInit.get();
	}

	public boolean init(String className,String id,ApplicationConfig application , RegistryConfig registry)
	{
		return init(className,id,application,registry,null);
	}
	
	public boolean init(Class<?> cls,String id,ApplicationConfig application , RegistryConfig registry)
	{
		return init(cls,id,application,registry,null);
	}
	
	public boolean init(String className,String id,ApplicationConfig application , RegistryConfig registry,String ip)
	{
		try
		{
			Class<?> cls = Class.forName(className);
			return init(cls,id,application,registry,ip);
		}
		catch(Exception ee)
		{
        	return false;
		}
	}
	
	public boolean init(Class<?> cls,String id,ApplicationConfig application , RegistryConfig registry,String ip)
	{
		return init(cls,id,application,registry,ip,"");
	}
	
	public boolean init(Class<?> cls,String id,ApplicationConfig application , RegistryConfig registry,String ip,String group)
	{
		if( hadSuccessInit.get() )
			return true;
		
		synchronized(hadSuccessInit)
		{
			if( hadSuccessInit.get() )
				return true;
			if( reference == null )
			{
		        reference = new ReferenceConfig<>();
		        reference.setApplication(application);
		        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		        reference.setInterface(cls);
		        reference.setId(id);
//		        reference.setActives(256);
//		        reference.setConnections(256);
		        reference.setGroup(group);
		        if(ip!=null)
		        	reference.setUrl("dubbo://"+ip+"/"+cls.getName());
		        logger.info("set reference"+reference.toString());
			}
			
	        try
	        {
	        	bean = (EchoService) reference.get();
	        }
	        catch(Exception e)
	        {
//	        	hadInit.set(true);
//	        	initFlag = false;
	        	e.printStackTrace();
	        	return false;
	        }
	        hadSuccessInit.set( bean != null && reference != null );
		}
		
        return hadSuccessInit.get();
	}

	@Override
	public boolean echo() {
		// TODO Auto-generated method stub
		return bean != null && "OK".equals(bean.$echo("OK"));
	}
}
