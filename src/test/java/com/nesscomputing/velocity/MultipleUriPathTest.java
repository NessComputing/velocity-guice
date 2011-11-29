package com.nesscomputing.velocity;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.net.URI;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MultipleUriPathTest {
    @Inject
    @Named("template-group.test1")
    Template test1;

    @Test
    public void testDirectoryBind() {
        Guice.createInjector(
                new VelocityGuiceModule()
                .bindTemplateDirectory("template-group", URI.create("res:not-so-fast/"), URI.create("res:template-folder/")))
            .injectMembers(this);

        StringWriter writer = new StringWriter();
        test1.merge(new VelocityContext(), writer);
        assertEquals("Not so fast!", writer.toString());
    }
}
