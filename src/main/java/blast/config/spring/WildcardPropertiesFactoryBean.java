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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;

/**
 * Load property files using wildcards.  Note that the order in which files are loaded are determined by Spring's {@link org.springframework.core.io.support.PathMatchingResourcePatternResolver PathMatchingResourcePatternResolver}.
 * We do not order the files further in this class, so BE CAREFUL when dealing with properties that might conflict
 * between files!  
 */
public class WildcardPropertiesFactoryBean extends PropertiesFactoryBean {
    private static final Logger log = LoggerFactory.getLogger(WildcardPropertiesFactoryBean.class);

    ClassLoader classLoader = null;

    public void setLocation(String location) {
        PathMatchingResourcePatternResolver resolver;
        if (classLoader == null) {
            resolver = new PathMatchingResourcePatternResolver();
        } else {
            resolver = new PathMatchingResourcePatternResolver(classLoader);
        }
        try {
            Resource[] resources = resolver.getResources(location);
            if (resources != null) {
                super.setLocations(resources);
            }
        } catch (IOException e) {
            log.error("Caught an IOException while trying to read resources from " + location + ". These property files WILL NOT BE LOADED!  \nException was: ", e);
        }
    }

    @Override
    public void setLocations(Resource[] locations) {
        log.debug("Trying to load " + Arrays.asList(locations));
        super.setLocations(locations);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}