package com.experiment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigurationLoader {

    public static final int STARTING_INDEX = 0;
    private Map<String, Object> properties;

    public ConfigurationLoader(List<String> configurationFiles) {
        InputStream consolidatedConfiguration = loadConfigurations(configurationFiles);
        Yaml yaml = new Yaml();
        this.properties = yaml.load(consolidatedConfiguration);
    }

    public String getPropertyFor(String propertyName) {
        return getPropertyFor(propertyName, String.class);
    }

    public <T> T getPropertyFor(String propertyName, Class<T> clazzToMap) {
        List<String> propertyChain = Arrays.asList(propertyName.split("\\."));
        return getProperty(propertyChain, STARTING_INDEX, this.properties, clazzToMap);
    }

    private InputStream loadConfigurations(List<String> configurationFiles) {
        List<InputStream> streams = configurationFiles.stream()
                .map(file -> Optional.ofNullable(this.getClass().getClassLoader().getResourceAsStream(file)).orElseThrow(() -> new IllegalArgumentException(String.format("Unable to fine file %s!", file))))
                .map(this::appendLineBreak)
                .collect(Collectors.toList());
        return new SequenceInputStream(Collections.enumeration(streams));
    }

    private InputStream appendLineBreak(InputStream stream) {
        return new SequenceInputStream(stream, new ByteArrayInputStream("\n".getBytes()));
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
