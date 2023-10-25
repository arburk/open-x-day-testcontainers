package com.baloise.open.xday.testcontainers.jpa.demo;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.baloise.open.xday.testcontainers.jpa.AbstractOracleTest;

import static org.junit.jupiter.api.Assertions.*;

class DemoViewUsingListaggTest extends AbstractOracleTest {

  @Test
  void ViewTest() {
    // generate base data
    final DemoParts testPart1 = DemoParts.builder().PartName("Compulsory").build();
    final DemoParts testPart2 = DemoParts.builder().PartName("Optional").build();
    final DemoEntity testEntity = DemoEntity.builder().name("Assembly").build();
    testEntity.setParts(Arrays.asList(testPart1, testPart2));
    entityManager.persist(testEntity);
    entityManager.flush();

    //query data
    final List<DemoViewUsingListagg> result = entityManager
        .createQuery("Select v From DemoViewUsingListagg v", DemoViewUsingListagg.class)
        .getResultList();

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals("Assembly", result.get(0).getName());
    assertEquals("Compulsory,Optional", result.get(0).getParts()); // Test ListAgg function
  }
}
