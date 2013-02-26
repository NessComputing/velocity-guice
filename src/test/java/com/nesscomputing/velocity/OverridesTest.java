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

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@AllowLocalFileAccess(paths= {"*"})
public class OverridesTest {
    @Inject
    @Named("template-override-test.test")
    Template wut;

    @Inject
    @Named("template-override-test.bar/test")
    Template bar;

    @Inject
    @Named("template-override-test.foo/test")
    Template foo;

    @Test
    public void testLookup() {
        Guice.createInjector(
                new VelocityGuiceModule()
                .bindTemplateDirectory("template-override-test", URI.create("res:template-override-test/")))
            .injectMembers(this);

        assertEquals("foo", evaluate(foo));
        assertEquals("bar", evaluate(bar));
        assertEquals("wut", evaluate(wut));
    }

    private String evaluate(Template t) {
        StringWriter w = new StringWriter();
        t.merge(new VelocityContext(), w);
        return w.toString();
    }
}
