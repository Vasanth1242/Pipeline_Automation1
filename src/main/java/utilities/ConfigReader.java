package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
	public static Properties props;

	static {
		try {
			FileInputStream fis = new FileInputStream("src/test/resources/config/Config.properties");
			props = new Properties();
			props.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		//return props.getProperty(key);
		String value = props.getProperty(key);
		
		if(value == null) {
			throw new RuntimeException("property' "+ key + "not found");
		}
		return value.trim();
	}
}
