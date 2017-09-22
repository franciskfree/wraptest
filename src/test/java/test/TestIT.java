/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.mobitill.warptest.IndexManagedBean;
import java.io.File;
import java.net.URL;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import static org.jboss.arquillian.warp.client.filter.http.HttpFilters.request;
import org.jboss.arquillian.warp.jsf.Phase;

/**
 *
 * @author jmukamana
 */
@WarpTest
@RunWith(Arquillian.class)
@RunAsClient
public class TestIT {

    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String WEB_INF_SRC = "src/main/webapp/WEB-INF";
    private static final String WEB_RESOURCES = "src/main/webapp/resources";
    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL deploymentUrl;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "com.mobitill")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("jboss-web.xml", "jboss-web.xml")
                .addAsWebInfResource("web.xml", "web.xml")
                .addAsWebResource(new File(WEBAPP_SRC, "index.xhtml"))
                .addAsWebResource(new File("src/main/webapp/page", "index2.xhtml"), "page/index2.xhtml")
                .addAsLibraries(files);
        System.out.println(war.toString(true));
        return war;
    }
    @FindBy(tagName = "li")                     // 2. injects a first element with given tag name
    private WebElement facesMessage;

    @Test
    @InSequence(1)
    public final void browserTest() throws Exception{
       browser.get(deploymentUrl.toExternalForm() + "page/index2.xhtml"); 
    }
    @Test
    @InSequence(2)
    public final void browserNewMerchant() throws Exception {
        Warp
                .initiate(new Activity() {
                    @Override
                    public void perform() {
                        WebElement txt = browser.findElement(By.id("form:txt"));
                        txt.sendKeys("sema index 1");
                        
                        WebElement btn = browser.findElement(By.id("form:btn"));
                        guardAjax(btn).click();
                    }
                })
                .observe(request().header().containsHeader("Faces-Request"))
                .inspect(new Inspection() {
                    private static final long serialVersionUID = 1L;
       
                    @Inject
                    IndexManagedBean hmb;

                    @BeforePhase(Phase.RENDER_RESPONSE)
                    public void beforeRenderResponse() {
                        System.out.println("beforeRenderResponse:");
                    }

                    @AfterPhase(Phase.RENDER_RESPONSE)
                    public void afterRenderResponse() {
                        System.out.println("afterRenderResponse:");
                    }

                    @AfterPhase(Phase.UPDATE_MODEL_VALUES)
                    public void afterUpdateModelValues() {
                        System.out.println("afterUpdateModelValues:");
                    }

                    @BeforePhase(Phase.UPDATE_MODEL_VALUES)
                    public void beforeUpdateModelValues() {
                        System.out.println("beforeUpdateModelValues:");
                    }

                    @BeforePhase(Phase.RESTORE_VIEW)
                    public void beforeRestoreView() {
                        System.out.println("beforeRestoreView:");
                    }

                    @AfterPhase(Phase.RESTORE_VIEW)
                    public void afterRestoreView() {
                        System.out.println("afterRestoreView:");
                    }

                    @BeforePhase(Phase.APPLY_REQUEST_VALUES)
                    public void beforeApplyRequestValues() {
                        System.out.println("beforeApplyRequestValues:");
                    }

                    @AfterPhase(Phase.APPLY_REQUEST_VALUES)
                    public void afterApplyRequestValues() {
                        System.out.println("afterApplyRequestValues:");
                    }

                    @BeforePhase(Phase.PROCESS_VALIDATIONS)
                    public void beforeProcessValidations() {
                        System.out.println("beforeProcessValidations:");
                    }

                    @AfterPhase(Phase.PROCESS_VALIDATIONS)
                    public void afterProcessValidations() {
                        System.out.println("afterProcessValidations:");
                    }

                    @BeforePhase(Phase.INVOKE_APPLICATION)
                    public void beforeInvokeApplication() {
                        System.out.println("beforeInvokeApplication:");
                    }

                    @AfterPhase(Phase.INVOKE_APPLICATION)
                    public void afterInvokeApplication() {
                        System.out.println("afterInvokeApplication:");
                        Assert.assertEquals("test value ", "sema index 1", hmb.getValue());
                    }
                });
    }
    @Test
    @InSequence(3)
    public final void browserEditMerchant() throws Exception {
        Warp
                .initiate(new Activity() {
                    @Override
                    public void perform() {
                        browser.get(deploymentUrl.toExternalForm() + "page/index2.xhtml");
                        WebElement txt = browser.findElement(By.id("form:txt"));
                        Assert.assertEquals("test value ", "hello from test", txt.getAttribute("value")); 
                    }
                })
                .inspect(new Inspection() {
                    private static final long serialVersionUID = 1L;

                    @Inject
                    IndexManagedBean hmb;

                    @BeforePhase(Phase.RENDER_RESPONSE)
                    public void beforeRenderResponse() {
                        System.out.println("beforeRenderResponse:");
                    }

                    @AfterPhase(Phase.RENDER_RESPONSE)
                    public void afterRenderResponse() {
                        System.out.println("afterRenderResponse:");
                    }

                    @BeforePhase(Phase.RESTORE_VIEW)
                    public void beforeRestoreView() {
                        System.out.println("beforeRestoreView:===================");
                    }

                    @AfterPhase(Phase.RESTORE_VIEW)
                    public void afterRestoreView() {
                        System.out.println("afterRestoreView:");
                        hmb.setValue("hello from test");
                    }

                });
    }

    @Test
    @InSequence(4)
    public final void sleep() throws Exception {
        Thread.sleep(10000);
    }
}
