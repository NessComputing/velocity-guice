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

import org.apache.velocity.Template;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

class InjectedTemplateGroup extends AbstractTemplateGroup {
    private final String prefix;
    private final Injector injector;

    public InjectedTemplateGroup(String prefix, Injector injector) {
        this.prefix = prefix;
        this.injector = injector;
    }

    @Override
    public Template find(String name) {
        // TODO: not terribly happy with looking up things in the injector at runtime
        Binding<Template> binding = injector.getExistingBinding(Key.get(Template.class, Names.named(prefix + "." + name)));
        if (binding == null) {
            return null;
        }
        return binding.getProvider().get();
    }
}
