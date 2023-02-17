package org.infinispan.tutorial.simple.local;

import org.infinispan.Cache;
import org.infinispan.commons.api.CacheContainerAdmin;
import org.infinispan.commons.marshall.JavaSerializationMarshaller;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;

public class InfinispanLocalPersist {

   public static void main(String[] args) throws Exception {

       ReliabilityTestUtils.cleanUpCacheFiles();

      // Setup up a clustered cache manager
      GlobalConfigurationBuilder global = new GlobalConfigurationBuilder();
      global.serialization()
              .marshaller(new JavaSerializationMarshaller())
              .allowList()
              .addClasses(Person.class);
      global.transport().defaultTransport();

      // Initialize the cache manager
      DefaultCacheManager cacheManager = new DefaultCacheManager(global.build());

      //Create cache configuration. Local and soft-index-file-store
      ConfigurationBuilder builder = new ConfigurationBuilder();
      builder.clustering().cacheMode(CacheMode.LOCAL);
      builder.persistence().passivation(false)
              .addSoftIndexFileStore()
              .shared(false)
              .dataLocation("tmp/cache/data")
              .indexLocation("tmp/cache/index");

      // Obtain a cache
      Cache<Person, String> cache = cacheManager.administration().withFlags(CacheContainerAdmin.AdminFlag.VOLATILE)
            .getOrCreateCache("cache", builder.build());

      // Store the current node address in some random keys
      for (int i = 0; i < 1; i++) {
          cache.put(new Person("john" + i), cacheManager.getNodeAddress());
      }
      // Display the current cache contents for the whole cluster
      System.out.println("------- Display the current cache contents for the whole cluster");
      cache.entrySet().forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));

//      System.out.println("------- Display the current cache contents for this node");
//      // Display the current cache contents for this node
//      // Note: By default numOwners=2, so in a cluster with 2 nodes, each node owns all the keys:
//      // some of the keys as "primary owner" and some keys as "backup owner"
//      cache.getAdvancedCache().withFlags(Flag.CACHE_MODE_LOCAL).entrySet()
//            .forEach(entry -> System.out.printf("%s = %s\n", entry.getKey(), entry.getValue()));
//      // Stop the cache manager and release all resources
      cacheManager.stop();
   }

}
