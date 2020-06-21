package com.flexicore.test;

import java.util.Set;

public class EntityProviderClasses {

    private final Set<Class<?>> seedClasses;

    public EntityProviderClasses(Set<Class<?>> seedClasses) {
        this.seedClasses = seedClasses;
    }

    public Set<Class<?>> getSeedClasses() {
        return seedClasses;
    }
}
