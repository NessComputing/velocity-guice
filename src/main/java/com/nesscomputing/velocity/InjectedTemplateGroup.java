package com.nesscomputing.velocity;

import org.apache.velocity.Template;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

class InjectedTemplateGroup extends AbstractTemplateGroup {
    private final String prefix;
    private final Injector injector;

    public InjectedTemplateGroup(String prefix, Injector injector) {
        this.prefix = prefix;
        this.injector = injector;
    }

    @Override
    public Template find(String name) {
        // TODO: not terribly happy with looking up things in the injector at runtime
        Binding<Template> binding = injector.getExistingBinding(Key.get(Template.class, Names.named(prefix + "." + name)));
        if (binding == null) {
            return null;
        }
        return binding.getProvider().get();
    }
}
