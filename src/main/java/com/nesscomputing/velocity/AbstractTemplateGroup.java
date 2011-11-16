package com.nesscomputing.velocity;

import org.apache.velocity.Template;

public abstract class AbstractTemplateGroup implements TemplateGroup {
    @Override
    public Template get(String name) {
        Template result = find(name);
        if (result == null) {
            throw new IllegalArgumentException("No such template " + name);
        }
        return result;
    }
}
