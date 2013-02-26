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

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class TemplateGroupProvider implements Provider<InjectedTemplateGroup> {

    private final String prefix;
    private Injector injector;

    public TemplateGroupProvider(String prefix) {
        this.prefix = prefix;
    }

    @Inject
    public void injectInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public InjectedTemplateGroup get() {
        return new InjectedTemplateGroup(prefix, injector);
    }
}
