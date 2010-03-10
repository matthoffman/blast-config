package blast.config.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class WildcardPropertiesFactoryBeanTest {

    @Resource
    Properties testBean1;

    @Resource
    Properties testBean2;

    @Resource
    Properties testBean3;

    @Resource
    Properties testBean4;

    @Resource
    Properties testBean5;

    @Resource
    Properties testBean6;

    @Resource
    Properties testBean1a;

    @Resource
    Properties testBean2a;

    @Resource
    Properties testBean3a;

    @Resource
    Properties testBean4a;

    
    @Test
    public void testWildcard1() throws Exception {

//        <bean id="testBean1" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="test*.properties"/>
//        </bean>
        expectFiles1and2(testBean1);
    }

    @Test
    public void testWildcard2() throws Exception {
//        <bean id="testBean2" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath:test*.properties"/>
//        </bean>
        expectFiles1and2(testBean2);
    }

    @Test
    public void testWildcard3() throws Exception {
//        <bean id="testBean3" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath*:test.properties"/>
//        </bean>
        expectFiles3(testBean3);
    }


    @Test
    public void testWildcard4() throws Exception {
//        <bean id="testBean4" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath*:test*.properties"/>
//        </bean>
        expectFiles123(testBean4);
    }

    @Test
    public void testWildcard5() throws Exception {
//        <bean id="testBean5" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath*:test2.properties"/>
//        </bean>
        expectFile2(testBean5);
    }

    @Test
    public void testWildcard6() {
//        <bean id="testBean6" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath:test1.properties"/>
//        </bean>
        expectFile1(testBean6);
    }

    private void expectFile1(Properties testProperties) {
        assertEquals("hello", testProperties.get("test.property")); // from test1
        assertEquals("tunnels", testProperties.get("neighborhood"));// from test1
        assertEquals("1,3,5,7", testProperties.get("test.whatever")); // from test1
    }


    private void expectFiles1and2(Properties testProperties) {
        assertEquals("guten morgen", testProperties.get("test.property")); // from test2
        assertEquals("lights out", testProperties.get("neighborhood"));// from test2
        assertEquals("hymnal", testProperties.get("white.winter"));   // from test2
        assertEquals("1,3,5,7", testProperties.get("test.whatever")); // from test1
    }


    private void expectFile2(Properties testProperties) {
        assertEquals("guten morgen", testProperties.get("test.property")); // from test2
        assertEquals("lights out", testProperties.get("neighborhood"));// from test2
        assertEquals("hymnal", testProperties.get("white.winter"));   // from test2
        assertNull(testProperties.get("test.whatever")); // from test1
    }

    private void expectFiles3(Properties testProperties) {
        assertEquals("dizzyspells", testProperties.get("fitz")); // from test
        assertEquals("hola", testProperties.get("test.property")); // from test
    }



    @Test
    public void testWildcard1SetLocations() throws Exception {

//        <bean id="testBean1" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="test*.properties"/>
//        </bean>
        expectFiles1and2(testBean1);
    }

    @Test
    public void testWildcard2SetLocations() throws Exception {
//        <bean id="testBean2" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath:test*.properties"/>
//        </bean>
        expectFiles1and2(testBean2);
    }

    @Test
    public void testWildcard3SetLocations() throws Exception {
//        <bean id="testBean3" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath*:test.properties"/>
//        </bean>
        expectFiles3(testBean3);
    }


    @Test
    public void testWildcard4SetLocations() throws Exception {
//        <bean id="testBean4" class="blast.config.spring.WildcardPropertiesFactoryBean">
//            <property name="location" value="classpath*:test*.properties"/>
//        </bean>
        expectFiles123(testBean4);
    }
    

    private void expectFiles123(Properties testProperties) {
        assertEquals("guten morgen", testProperties.get("test.property")); // from test2
        assertEquals("lights out", testProperties.get("neighborhood"));// from test2
        assertEquals("hymnal", testProperties.get("white.winter"));   // from test2
        assertEquals("1,3,5,7", testProperties.get("test.whatever")); // from test1
        assertEquals("dizzyspells", testProperties.get("fitz")); // from test
    }

    public void setTestBean1(Properties testBean1) {
        this.testBean1 = testBean1;
    }

    public void setTestBean2(Properties testBean2) {
        this.testBean2 = testBean2;
    }

    public void setTestBean3(Properties testBean3) {
        this.testBean3 = testBean3;
    }

    public void setTestBean4(Properties testBean4) {
        this.testBean4 = testBean4;
    }

    public void setTestBean5(Properties testBean5) {
        this.testBean5 = testBean5;
    }

    public void setTestBean6(Properties testBean6) {
        this.testBean6 = testBean6;
    }

    public void setTestBean1a(Properties testBean1a) {
        this.testBean1a = testBean1a;
    }

    public void setTestBean2a(Properties testBean2a) {
        this.testBean2a = testBean2a;
    }

    public void setTestBean3a(Properties testBean3a) {
        this.testBean3a = testBean3a;
    }

    public void setTestBean4a(Properties testBean4a) {
        this.testBean4a = testBean4a;
    }
}
