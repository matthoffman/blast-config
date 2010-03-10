/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package blast.config.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.util.CollectionUtils;

/**
 * This is a PropertiesFactoryBean that takes multiple sets of properties -- say, some from files, some from the
 * database, some from JNDI or a coordination service like ZooKeeper -- and merges them together in a predictable way.
 * This way, you can pass this PropertiesFactoryBean into a PropertiesPlaceholderConfigurer instance, and then annotate
 * your spring beans (using ${propertyName} syntax as normal) and those properties can be pulled from any of a number of
 * sources behind the scenes.
 *
 * The key here is that property lists passed into the constructor of this bean are passed in IN PRIORITY ORDER -- that
 * is, properties passed in FIRST will take precendence over those in later properties sets.
 * So, for example, if you have three sets of properties passed in in this order:
 * <pre>
 * file.properties
 * db.properties
 * build.properties
 * </pre>
 * and the same property -- let's say "database-version" -- was defined in each, then the one passed in in file.properties
 * would be kept.
 *
 * This is UNLIKE the "properties" property of this bean, which is the reverse. Note that setting local overrides (and
 * the rest of the configurations for the standard Spring PropertyPlaceholderConfigurer) still work.
 *
 * Example configuration:
 * <pre>
  &lt;bean id=&quot;mergedPropertyFactory&quot; class=&quot;blast.config.spring.MergedPropertiesFactoryBean&quot;&gt;
        &lt;!--  these are in order from HIGHEST PRIORITY (overrides properties from other sources) to LOWEST PRIORITY --&gt;
        &lt;constructor-arg&gt;
            &lt;list&gt;
                &lt;bean class=&quot;org.springframework.beans.factory.config.PropertiesFactoryBean&quot;&gt;
                    &lt;property name=&quot;locations&quot;&gt;
                        &lt;list&gt;
                            &lt;value&gt;classpath:test1.properties&lt;/value&gt;
                            &lt;value&gt;classpath:build.properties&lt;/value&gt;
                        &lt;/list&gt;
                    &lt;/property&gt;
                &lt;/bean&gt;
                &lt;bean class=&quot;blast.config.DBPropertiesFactoryBean&quot;&gt;
                    &lt;property name=&quot;datasource&quot; ref=&quot;dataSource&quot;/&gt;
                    &lt;property name=&quot;query&quot; value=&quot;select namespace, name, value from table_name &quot;/&gt;
                &lt;/bean&gt;
                &lt;bean class=&quot;blast.config.spring.WildcardPropertiesFactoryBean&quot;&gt;
                    &lt;property name=&quot;locations&quot; value=&quot;META-INF/spring/*.properties&quot;/&gt;
                &lt;/bean&gt;
            &lt;/list&gt;
        &lt;/constructor-arg&gt;
    &lt;/bean&gt;
 </pre>
 * In this example, properties will be loaded from four locations -- test1.properties, build.properties, a database
 * table ("table_name", in this case), and *.properties in the META-INF/spring directory. Properties defined in
 * test1.properties and build.properties will take precedence over those defined in the database or
 * META-INF/spring/*.properties, while properties defined in the database will take precendence over those defined in
 * any property file in META-INF/spring.
 *
 * To use this within a PropertyPlaceholderConfigurer, giving us the benefits of ${variable.name} string replacement
 * within Spring context files, we'd do something like the following:
 *
 <pre>
 <[CDATA[
    <bean id="propertyConfigurer" class="blast.config.spring.PlaceholderWithDefaultConfigurer">

    </bean>

 ]>
 &lt;property name=&quot;systemPropertiesModeName&quot; value=&quot;SYSTEM_PROPERTIES_MODE_OVERRIDE&quot;/&gt;

 </pre>
 */
public class MergedPropertiesFactoryBean extends PropertiesFactoryBean {

    private static final Logger log = LoggerFactory.getLogger(MergedPropertiesFactoryBean.class);

    List<Properties> propertiesToMerge;

    /**
     *
     * @param propertiesToMerge in descending priority order (highest priority to lowest).
     * That is, properties specified in the first entry in this list will override properties
     * of the same name specified in later files.
     */
    public MergedPropertiesFactoryBean(final List<Properties> propertiesToMerge){
        super();
        this.propertiesToMerge = propertiesToMerge;
    }

    @Override
    protected void loadProperties(Properties mergedProperties) throws IOException {
        if (propertiesToMerge != null) {
            // we need the properties to be in reverse order, because of the way we'll load them
            // this way, the first gets precedence
            // create a copy so we're not modifying the argument
            ArrayList<Properties> propList = new ArrayList<Properties>(propertiesToMerge);
            Collections.reverse(propList);
            for (Properties prop: propList) {
                CollectionUtils.mergePropertiesIntoMap(prop, mergedProperties);
            }
        }

        Properties locationProps = new Properties();
        super.loadProperties(locationProps);
        if (!locationProps.isEmpty()) {
            log.warn("Warning: You've set the \"locations\" property in the "+this.getClass().getName()+", but you probably meant to pass the property files in the constructor. " +
            		"See javadoc for details. " +
            		"Locations will override everything passed in via the constructor.");
            CollectionUtils.mergePropertiesIntoMap(locationProps, mergedProperties);
        }
    }
}
