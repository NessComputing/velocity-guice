package com.nesscomputing.velocity;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.Log4JLogChute;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class VelocityGuiceModule extends AbstractModule {

    private final List<Runnable> bindingActions = Lists.newArrayList();
    private final Charset charset;

    public VelocityGuiceModule() {
        this (Charsets.UTF_8);
    }

    public VelocityGuiceModule(Charset charset) {
        this.charset = charset;
    }

    public VelocityGuiceModule bindTemplate(final Annotation annotation, final URI templateUri) {
        bindingActions.add(new Runnable() {
            @Override
            public void run() {
                bind (Template.class).annotatedWith(annotation).toProvider(new UriTemplateProvider(templateUri));
            }
        });
        return this;
    }

    public VelocityGuiceModule bindTemplateDirectory(final String prefix, final URI templateDirUri) {
        bindingActions.add(new Runnable() {
            @Override
            public void run() {
                try {
                    for (FileObject file : VFS.getManager().resolveFile(templateDirUri.toString()).getChildren()) {
                        if (file.getName().getBaseName().endsWith(".vm")) {
                            String bindName = prefix + "." + StringUtils.removeEndIgnoreCase(file.getName().getBaseName(), ".vm");
                            bind (Template.class).annotatedWith(Names.named(bindName)).toProvider(new UriTemplateProvider(file.getURL().toURI()));
                        }
                    }
                } catch (FileSystemException e) {
                    throw Throwables.propagate(e);
                } catch (URISyntaxException e) {
                    throw Throwables.propagate(e);
                }
            }
        });
        return this;
    }

    @Override
    protected void configure() {
        bind(Charset.class).annotatedWith(Encoding.class).toInstance(charset);
        for (Runnable action : bindingActions) {
            action.run();
        }
    }

    @Provides
    public VelocityEngine getVelocityEngine() {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty("runtime.log.logsystem.log4j.logger", Log4JLogChute.class.getName());
        engine.setProperty("velocimacro.arguments.strict", "true");
        engine.setProperty("runtime.references.strict", "true");
        return engine;
    }
}
