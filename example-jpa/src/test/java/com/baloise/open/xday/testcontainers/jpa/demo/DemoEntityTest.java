/*
 * Copyright 2022 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package com.baloise.open.xday.testcontainers.jpa.demo;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.baloise.open.xday.testcontainers.jpa.AbstractOracleTest;

import static org.junit.jupiter.api.Assertions.*;

class DemoEntityTest extends AbstractOracleTest {

  @Test
  void basicInsertTest() {
    // get timestamp before record is added for comparison
    final LocalDateTime beginOfTest = LocalDateTime.now();

    // generate test object
    final DemoParts testPart1 = DemoParts.builder().PartName("SubPart_1").build();
    final DemoParts testPart2 = DemoParts.builder().PartName("SubPart_2").build();
    final DemoEntity testEntity = DemoEntity.builder().name("FirstEntry").build();
    testEntity.setParts(Arrays.asList(testPart1, testPart2));
    entityManager.persist(testEntity);
    entityManager.flush();

    // get timestamp after record has been added for comparison
    final LocalDateTime afterEntityPeristed = LocalDateTime.now();

    final DemoEntity result = entityManager
        .createQuery("SELECT e FROM DemoEntity e WHERE e.name = 'FirstEntry'", DemoEntity.class)
        .getSingleResult();

    //now assert that DB logic works as expected
    assertAll(
        () -> assertEquals(testEntity, result),
        () -> assertNotNull(testEntity.getId()),
        () -> assertTrue(testEntity.getId() > 0),
        () -> assertEquals("FirstEntry", result.getName()),
        () -> assertNotNull(result.getRecorded()),
        () -> assertTrue(result.getRecorded().isAfter(beginOfTest)),
        () -> assertTrue(result.getRecorded().isBefore(afterEntityPeristed)),
        () -> assertEquals(2, result.getParts().size()),
        () -> assertTrue(result.getParts().stream().anyMatch(p -> p.getPartName().equals("SubPart_1"))),
        () -> assertTrue(result.getParts().stream().anyMatch(p -> p.getPartName().equals("SubPart_2")))
    );
  }
}
