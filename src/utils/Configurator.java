package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configurator {
    private Properties properties;

    public Configurator(String archivo) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(archivo)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

}