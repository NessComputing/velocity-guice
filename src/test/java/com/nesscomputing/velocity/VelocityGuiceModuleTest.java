package com.nesscomputing.velocity;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.net.URI;
import java.net.URL;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class VelocityGuiceModuleTest {

    @Inject
    @Named("template-group.test1")
    Template test1;

    @Inject(optional=true)
    @Named("template-group.test2")
    Template test2;

    @Test
    public void testResourceThere() {
        System.out.println(getClass().getClassLoader());
        String name = "template-folder/test1.vm";
        URL resource = getClass().getClassLoader().getResource(name);
        System.out.println(resource);
        System.out.println(name.hashCode());
        assertNotNull(resource);
    }

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
