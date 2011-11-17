package com.nesscomputing.velocity;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonsVfsResourceLoader extends ResourceLoader {

    private static final Logger LOG = LoggerFactory.getLogger(CommonsVfsResourceLoader.class);

    @Override
    public void init(ExtendedProperties configuration) {

    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        try {
            FileSystemOptions opts = new FileSystemOptions();
            return VFS.getManager().resolveFile(source, opts).getContent().getInputStream();
        } catch (FileSystemException e) {
            LOG.debug(String.format("Could not load resource \"%s\"", source), e);
            throw new ResourceNotFoundException(source, e);
        }
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }
}
