velocity-guice
==============

Provide simple Guice bindings for Apache Velocity.  Currently very bare-bones.

Features
--------

* Uses commons-vfs to find templates, so any filesystem supported by commons-vfs works
* All templates are found eagerly, so any missing templates will fail fast and explosively
* Can bind "template directories" - any .vm files will be exposed as template bindings

Limitations
-----------

* Since all templates are bound at Guice module time, there is no dynamic discovery (yet)
* Uses a different VelocityEngine per template -- not sure of the consequences of this
* Not terribly configurable (yet)

License
-------

Released under Apache License 2.0 by Ness Computing
http://www.apache.org/licenses/LICENSE-2.0.html
