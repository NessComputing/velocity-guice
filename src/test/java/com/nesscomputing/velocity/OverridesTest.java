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
