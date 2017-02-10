package com.cevaris.authme.utils.testing;

import java.util.List;

import com.cevaris.authme.aws.modules.AwsCredentialsModule;
import com.cevaris.authme.aws.modules.AwsKmsModule;
import com.cevaris.authme.utils.PropertyStore;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyStoreIntegrationTest {

  private List<Module> modules = Lists.newArrayList(
      (Module) new AwsCredentialsModule(),
      (Module) new AwsKmsModule()
  );
  private Injector injector = Guice.createInjector(Stage.PRODUCTION, modules);
  private PropertyStore store;

  @Test
  public void testSuccessful() {
    store.loadResource("/enc-test.properties");

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

}
