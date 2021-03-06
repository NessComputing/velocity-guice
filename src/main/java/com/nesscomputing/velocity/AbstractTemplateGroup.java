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

public abstract class AbstractTemplateGroup implements TemplateGroup {
    @Override
    public Template get(String name) {
        Template result = find(name);
        if (result == null) {
            throw new IllegalArgumentException("No such template " + name);
        }
        return result;
    }
}
