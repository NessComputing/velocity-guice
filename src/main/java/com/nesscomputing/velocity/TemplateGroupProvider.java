package com.nesscomputing.velocity;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class TemplateGroupProvider implements Provider<InjectedTemplateGroup> {

    private final String prefix;
    private Injector injector;

    public TemplateGroupProvider(String prefix) {
        this.prefix = prefix;
    }

    @Inject
    public void injectInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public InjectedTemplateGroup get() {
        return new InjectedTemplateGroup(prefix, injector);
    }
}
