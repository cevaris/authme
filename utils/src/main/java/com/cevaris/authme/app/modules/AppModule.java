package com.cevaris.authme.app.modules;

import javax.inject.Named;

import com.amazonaws.services.kms.AWSKMSClient;
import com.cevaris.authme.utils.PropertyStore;
import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppModule implements Module {
  @Singleton
  @Provides
  PropertyStore providesPropertyStore(
      AWSKMSClient awskmsClient,
      @Named(PropertyStore.PROPERTY_RESOURCE_PATH) String resourcePath
  ) {
    Preconditions.checkNotNull(resourcePath);
    PropertyStore propertyStore = new PropertyStore(awskmsClient, resourcePath);
    propertyStore.loadResource();
    return propertyStore;
  }

  public void configure(Binder binder) {

  }
}
