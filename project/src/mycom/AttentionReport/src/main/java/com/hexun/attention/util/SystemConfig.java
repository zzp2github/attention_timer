package com.hexun.attention.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
/**
 * @ClassName: SystemConfig 
 * @Description: TODO(系统配置文件) 
 * @author: zhangzhipeng
 * @date: 2016年9月5日 下午4:30:28
 */
public class SystemConfig {
	//老师的粉丝url
	public static String TEACHER_RELATION_URL;  
	
	public static Properties properties = new Properties();
	static {
		try {
			properties.load(SystemConfig.class.getClassLoader().getResourceAsStream("config/properties/xxl-job-executor.properties"));
			Field[] list = SystemConfig.class.getFields();
			for (int i = 0; i < list.length; i++) {
				Field f = list[i];
				String key = f.getName();
				String value = properties.getProperty(key);
				if (value != null){
				}else {
					continue;
				}
				try {
					Object p = new Object();
					// 数据类型转换
					if (p == null || String.class.equals(f.getType()))
						p = value;
					else if (Double.class.equals(f.getType()))
						p = Double.parseDouble(value);
					else if (Long.class.equals(f.getType()))
						p = Long.parseLong(value);
					else if (Integer.class.equals(f.getType()))
						p = Integer.parseInt(value);
					else if (Short.class.equals(f.getType()))
						p = Short.parseShort(value);
					else if (Boolean.class.equals(f.getType()))
						p = Boolean.parseBoolean(value);
					// 给对象里的属性写入值
					f.set(new SystemConfig(), p);
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
