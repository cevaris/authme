package com.cevaris.authme.utils;

import java.util.List;

import com.cevaris.authme.app.modules.AppModule;
import com.cevaris.authme.aws.modules.AwsCredentialsModule;
import com.cevaris.authme.aws.modules.AwsKmsModule;
import com.google.common.collect.Lists;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.name.Names;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyStoreIntegrationTest {

  private List<Module> modules = Lists.newArrayList(
      new AwsCredentialsModule(),
      new AwsKmsModule(),
      new AppModule(),
      new TestModule()
  );
  private Injector injector = Guice.createInjector(Stage.PRODUCTION, modules);
  private PropertyStore store;

  @Test
  public void testSuccessful() {
    assertEquals(1234, store.getInt("test.int"));
    assertEquals(830076380524208128L, store.getLong("test.long"));
    assertEquals("test my string", store.get("test.str"));
  }

  @Before
  public void setUp() throws Exception {
    store = injector.getInstance(PropertyStore.class);
  }

  @After
  public void tearDown() throws Exception {
  }

  class TestModule implements Module {
    public void configure(Binder binder) {
      binder.bind(String.class)
          .annotatedWith(Names.named(PropertyStore.PROPERTY_RESOURCE_PATH))
          .toInstance("/enc-test.properties");
    }
  }

}
