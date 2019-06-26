package com.experiment;


import com.experiment.model.BasicPojo;
import com.experiment.model.InvalidPojoMapping;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


class ConfigurationLoaderTest {

    @Test
    void shouldBeAbleToReturnASingleProperty() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader(Collections.singletonList("application.yml"));
        assertEquals("basic_property_value", configurationLoader.getPropertyFor("basic_property"));
        assertEquals("nested_property_value", configurationLoader.getPropertyFor("nested.property"));
        assertEquals("nested_property_layered_value", configurationLoader.getPropertyFor("nested.layered.property"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPropertyIsNotFound() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader(Collections.singletonList("application.yml"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> configurationLoader.getPropertyFor("non.existent.property"));
        assertEquals("Property (non.existent.property) does not exist!", exception.getMessage());
    }

    @Test
    void shouldBeAbleToMapALogicalBlockIntoPojo() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader(Collections.singletonList("application.yml"));
        assertEquals(new BasicPojo("name", "description"), configurationLoader.getPropertyFor("basic_pojo", BasicPojo.class));
        assertEquals(Arrays.asList(new BasicPojo("name1", "description1"), new BasicPojo("name2", "description2")),
                Arrays.asList(configurationLoader.getPropertyFor("nested.list", BasicPojo[].class)));
    }

    @Test
    void shouldBeAbleToThrowIllegalArgumentExceptionWhenMappingToPojoFails() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader(Collections.singletonList("application.yml"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> configurationLoader.getPropertyFor("basic_pojo", InvalidPojoMapping.class));
        assertTrue(exception.getMessage().contains("Unable to map to com.experiment.model.InvalidPojoMapping: Unrecognized field \"name\""));
    }

    @Test
    void shouldBeAbleLoadPropertiesFromMultipleYamlFiles() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader(Arrays.asList("yaml-default-utils/default.yml", "yaml-default-utils/child.yml"));
        assertEquals("default_name", configurationLoader.getPropertyFor("child.configuration.name"));
        assertEquals("overridden_default_description", configurationLoader.getPropertyFor("child.configuration.description"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionIfConfigurationFileIsNotPresent() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ConfigurationLoader(Collections.singletonList("nonexistent.yml")));
        assertEquals("Unable to fine file nonexistent.yml!", exception.getMessage());
    }
}