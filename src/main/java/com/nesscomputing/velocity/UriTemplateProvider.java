package com.nesscomputing.velocity;

import java.net.URI;
import java.nio.charset.Charset;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class UriTemplateProvider implements Provider<Template> {
    private final URI templateUri;
    private VelocityEngine engine;
    private Charset encoding;

    public UriTemplateProvider(URI templateUri) {
        this.templateUri = templateUri;
    }

    @Inject
    public void setVelocity(VelocityEngine engine, @Encoding Charset encoding) {
        this.engine = engine;
        this.encoding = encoding;
    }

    @Override
    public Template get() {
        return engine.getTemplate(templateUri.toString(), encoding.name());
    }
}
