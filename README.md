Blast Config
============

About
-----

This is a small collection of configuration-related classes that may be helpful for other projects.
They're primarily extensions of Spring's [PropertyPlaceholderConfigurer](http://static.springsource.org/spring/docs/2.5.x/api/org/springframework/beans/factory/config/PropertyPlaceholderConfigurer.html)
that originated from the Riot project ([http://www.riotfamily.org](www.riotfamily.org)), and were modified only very slightly to work in a broader context.  I've changed the package declaration on those to indicate that they have been modified, so that any problems in the classes when used outside the original Riot context aren't blamed on them.  Many thanks to Riot for their work and for licensing it under the Apache license.

The classes in question were pulled from Riot's SVN around February 2010, so more recent changes may not have been ported back.  If this is the case, please fork & make necessary changes, or let me know.  Thanks!

Examples
--------

* **WildcardPropertiesFactoryBean** lets you load resources (.properties files, most likely) using wildcards in place of explicit paths and/or filenames.  It relies heavily on Spring's [PathMatchingPatternResolver](http://static.springsource.org/spring/docs/2.5.6/api/org/springframework/core/io/support/PathMatchingResourcePatternResolver.html) behind the scenes, so make sure to read that javadoc to understand how it works. In particular, note the two warnings in that Javadoc:

> WARNING: Note that "classpath*:" when combined with Ant-style patterns will only work reliably with at least one root directory before the pattern starts, unless the actual target files reside in the file system. This means that a pattern like "classpath*:*.xml" will not retrieve files from the root of jar files but rather only from the root of expanded directories. This originates from a limitation in the JDK's ClassLoader.getResources() method which only returns file system locations for a passed-in empty String (indicating potential roots to search).
>
> WARNING: Ant-style patterns with "classpath:" resources are not guaranteed to find matching resources if the root package to search is available in multiple class path locations. This is because a resource such as
>
>     com/mycompany/package1/service-context.xml
>
>may be in only one location, but when a path such as
>     classpath:com/mycompany/**/service-context.xml
>
>is used to try to resolve it, the resolver will work off the (first) URL returned by getResource("com/mycompany");. If this base package node exists in multiple classloader locations, the actual end resource may not be underneath. Therefore, preferably, use "classpath*:" with the same Ant-style pattern in such a case, which will search all class path locations that contain the root package.

Using **WildcardPropertiesFactoryBean** is simple:

    <bean id="propertyFactory" class="blast.config.spring.WildcardPropertiesFactoryBean">
        <property name="location" value="classpath*:/property_dir/**/*.properties"/>
    </bean>

This will return any file ending in ".properties" that is on the classpath and inside a "property_dir" directory, nested any number of levels deep.

It will wrap them in a PropertiesFactoryBean, which allows you to access them as a [Properties](http://java.sun.com/javase/6/docs/api/java/util/Properties.html) object.  For example, if you have a bean like this:

    public class FooClass {
        Properties props;

        public void setProps(Properties newProps) {
           this.props = newProps;
        }
    }

... then you could define it in Spring this way:

    <bean id="fooClass" class="FooClass">
        <property name="props" ref="propertyFactory"/>
    </bean>

and it would inject a Properties instance with all the properties defined underneath property_dir, defined above.

It is perhaps more useful, though, to wrap the properties in a PropertyPlaceholderConfigurer; then you could define your Spring beans with placeholders, like this:

    <bean id="someOtherClass" class="SomeClass">
        <property name="name" value="${name.property}"/>
    </bean>

and let Spring inject the value of a property called "name.property".

*TODO: examples of wrapping this in the PropertyPlaceholderConfigurer instances found here.*

Coming soon:  examples, and some more explicit test cases.


License
-------

These classes are provided as-is, no warranty, and licensed under the Apache 2.0 license.


Why "Blast"?
------------
I like the sound of the word. It lends itself well to fun rocketship icons. I can't think of any other way to tie together configuration files and Space Invaders.

