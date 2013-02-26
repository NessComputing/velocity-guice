package com.nesscomputing.velocity;

import java.io.StringWriter;
import java.net.URI;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.junit.Test;

import com.nesscomputing.testing.lessio.AllowLocalFileAccess;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@AllowLocalFileAccess(paths= {"*"})
public class VelocitySetupTest {

    @Inject
    @Named("template-group.test2")
    Template test2;

    @Test(expected=MethodInvocationException.class)
    public void testSubstitutionFail() {
        Guice.createInjector(
                new VelocityGuiceModule()
                .bindTemplateDirectory("template-group", URI.create("res:template-folder/")))
            .injectMembers(this);

        StringWriter writer = new StringWriter();
        test2.merge(new VelocityContext(), writer);
    }
}
