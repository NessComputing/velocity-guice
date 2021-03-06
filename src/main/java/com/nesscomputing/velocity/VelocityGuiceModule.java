/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.velocity;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.Log4JLogChute;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
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
                bind (Template.class).annotatedWith(annotation).toProvider(new UriTemplateProvider(templateUri)).in(Scopes.SINGLETON);
            }
        });
        return this;
    }

    public VelocityGuiceModule bindTemplateDirectory(final String prefix, final URI... templateDirUris) {
        bindingActions.add(new Runnable() {
            @Override
            public void run() {
                try {
                    Set<String> foundTemplates = Sets.newHashSet();
                    for (URI dir : templateDirUris) {
                        FileObject root = VFS.getManager().resolveFile(dir.toString());
                        walk(foundTemplates, prefix, root);
                    }
                    bind (TemplateGroup.class).annotatedWith(Names.named(prefix)).toProvider(new TemplateGroupProvider(prefix));
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
    @Singleton
    public VelocityEngine getVelocityEngine() {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty("runtime.log.logsystem.log4j.logger", Log4JLogChute.class.getName());
        engine.setProperty("velocimacro.arguments.strict", "true");
        engine.setProperty("runtime.references.strict", "true");
        engine.setProperty("resource.loader", "vfs");
        engine.setProperty("vfs.resource.loader.class", CommonsVfsResourceLoader.class.getName());
        engine.setProperty("velocimacro.library", "");
        engine.init();
        return engine;
    }

    protected void walk(Set<String> foundTemplates, final String prefix, FileObject root) throws FileSystemException, URISyntaxException {
        List<FileObject> foundFiles = Lists.newArrayList();
        root.findFiles(new MacroFileSelector(), true, foundFiles);

        for (FileObject file : foundFiles) {
            String templateName = StringUtils.removeEndIgnoreCase(root.getName().getRelativeName(file.getName()), ".vm");
            String bindName = prefix + "." + templateName;

            if (!foundTemplates.add(bindName)) {
                continue;
            }

            UriTemplateProvider provider = new UriTemplateProvider(file.getURL().toURI());
            bind (Template.class).annotatedWith(Names.named(bindName)).toProvider(provider).in(Scopes.SINGLETON);
        }
    }

    private static class MacroFileSelector implements FileSelector {
        @Override
        public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
            return fileInfo.getFile().getName().getBaseName().endsWith(".vm");
        }

        @Override
        public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
            return true;
        }
    }
}
