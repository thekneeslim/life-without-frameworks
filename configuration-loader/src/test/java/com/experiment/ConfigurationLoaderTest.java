package com.experiment;


import com.experiment.model.BasicPojo;
import com.experiment.model.InvalidPojoMapping;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class ConfigurationLoaderTest {

    @Test
    void shouldBeAbleToReturnASingleProperty() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        assertEquals("basic_property_value", configurationLoader.getPropertyFor("basic_property"));
        assertEquals("nested_property_value", configurationLoader.getPropertyFor("nested.property"));
        assertEquals("nested_property_layered_value", configurationLoader.getPropertyFor("nested.layered.property"));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenPropertyIsNotFound() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> configurationLoader.getPropertyFor("non.existent.property"));
        assertEquals("Property (non.existent.property) does not exist!", exception.getMessage());
    }

    @Test
    void shouldBeAbleToMapALogicalBlockIntoPojo() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        assertEquals(new BasicPojo("name", "description"), configurationLoader.getPropertyFor("basic_pojo", BasicPojo.class));
        assertEquals(Arrays.asList(new BasicPojo("name1", "description1"), new BasicPojo("name2", "description2")),
                Arrays.asList(configurationLoader.getPropertyFor("nested.list", BasicPojo[].class)));
    }

    @Test
    void shouldBeAbleToThrowIllegalArgumentExceptionWhenMappingToPojoFails() {
        ConfigurationLoader configurationLoader = new ConfigurationLoader();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> configurationLoader.getPropertyFor("basic_pojo", InvalidPojoMapping.class));
        assertTrue(exception.getMessage().contains("Unable to map to com.experiment.model.InvalidPojoMapping: Unrecognized field \"name\""));
    }
}