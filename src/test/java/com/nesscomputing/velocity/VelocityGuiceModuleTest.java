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

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.net.URI;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import com.nesscomputing.testing.lessio.AllowLocalFileAccess;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

@AllowLocalFileAccess(paths= {"*"})
public class VelocityGuiceModuleTest {

    @Inject
    @Named("template-group.test1")
    Template test1;

    @Inject(optional=true)
    @Named("template-group.test2")
    Template test2;

    @Test
    public void testDirectBind() {
        Guice.createInjector(
                new VelocityGuiceModule()
                .bindTemplate(Names.named("template-group.test1"), URI.create("res:template-folder/test1.vm")))
            .injectMembers(this);

        StringWriter writer = new StringWriter();
        test1.merge(new VelocityContext(), writer);
        assertEquals("Hello, world!", writer.toString());
    }

    @Test
    public void testDirectoryBind() {
        Guice.createInjector(
                new VelocityGuiceModule()
                .bindTemplateDirectory("template-group", URI.create("res:template-folder/")))
            .injectMembers(this);

        StringWriter writer = new StringWriter();
        test1.merge(new VelocityContext(), writer);
        assertEquals("Hello, world!", writer.toString());

        writer = new StringWriter();
        test2.merge(new VelocityContext(ImmutableMap.of("name", "test case")), writer);
        assertEquals("Hello, test case!", writer.toString());
    }
}
