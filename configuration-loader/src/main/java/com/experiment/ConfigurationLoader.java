package com.experiment;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        return getPropertyFor(propertyName, String.class);
    }

    public <T> T getPropertyFor(String propertyName, Class<T> clazzToMap) {
        List<String> propertyChain = Arrays.asList(propertyName.split("\\."));
        return getProperty(propertyChain, STARTING_INDEX, this.properties, clazzToMap);
    }

    private <T> T getProperty(List<String> propertyChain, int index, Map map, Class<T> clazzToMap) {
        Object value = Optional.ofNullable(map.get(propertyChain.get(index)))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Property (%s) does not exist!", String.join(".", propertyChain))));
        int INCREMENTED_INDEX = index + 1;
        if(value instanceof Map && isNotLastValueOf(propertyChain, INCREMENTED_INDEX)) {
            return getProperty(propertyChain, INCREMENTED_INDEX, (Map) value, clazzToMap);
        }
        return mapToObject(clazzToMap, value);
    }

    private <T> T mapToObject(Class<T> clazzToMap, Object value) {
        try {
            return new ObjectMapper().convertValue(value, clazzToMap);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Unable to map to %s: %s", clazzToMap.getCanonicalName(), e.getMessage()));
        }
    }

    private boolean isNotLastValueOf(List<String> propertyChain, int INCREMENTED_INDEX) {
        return INCREMENTED_INDEX != propertyChain.size();
    }
}
