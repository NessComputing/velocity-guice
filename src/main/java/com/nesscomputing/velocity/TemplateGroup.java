package com.nesscomputing.velocity;

import org.apache.velocity.Template;

public interface TemplateGroup {
    Template get(String name);
    Template find(String name);
}
