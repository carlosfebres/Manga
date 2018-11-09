package utils;

import java.io.*;
import java.util.Properties;

public class Props {
    Properties props;

    Props() {
        props = new Properties();
        InputStream is = null;

        try {
            File f = new File("../../webapp/config.properties");
            is = new FileInputStream(f);
        } catch (Exception e) {
            System.out.println("Another Try");
            is = null;
        }

        try {
            if (is == null) {
                is = getClass().getResourceAsStream("config.properties");
            }
            props.load(is);
        } catch (Exception e) {
        }
    }

    String getProperty(String s) {
        return props.getProperty(s);
    }
}
