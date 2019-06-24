package com.experiment;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
}