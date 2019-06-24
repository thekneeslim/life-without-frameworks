package com.experiment.model;

public class BasicPojo {

    private String name;
    private String description;

    public BasicPojo() {
        // For serialization
    }

    public BasicPojo(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicPojo basicPojo = (BasicPojo) o;

        if (name != null ? !name.equals(basicPojo.name) : basicPojo.name != null) return false;
        return description != null ? description.equals(basicPojo.description) : basicPojo.description == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
