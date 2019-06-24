package com.experiment;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfigurationLoader {


    public static final int STARTING_INDEX = 0;
    private final Map<String, Object> properties;

    public ConfigurationLoader() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("application.yml");
        this.properties = yaml.load(inputStream);
    }

    public String getPropertyFor(String propertyName) {
        List<String> propertyChain = Arrays.asList(propertyName.split("\\."));
        return getProperty(propertyChain, STARTING_INDEX, this.properties);
    }

    private String getProperty(List<String> propertyChain, int index, Map<String, Object> map) {
        Object value = Optional.ofNullable(map.get(propertyChain.get(index)))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Property (%s) does not exist!", String.join(".", propertyChain))));
        if(value instanceof Map) {
            int INCREMENTED_INDEX = index + 1;
            return getProperty(propertyChain, INCREMENTED_INDEX, (Map) value);
        }
        return (String) value;
    }
}
