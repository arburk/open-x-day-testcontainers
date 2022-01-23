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

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "DEMOTABLE")
public class DemoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DemoEntitySeq")
  @SequenceGenerator(name = "DemoEntitySeq", sequenceName = "DEMO_SEQ", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "RECORDED", nullable = false)
  private LocalDateTime recorded;

  @OneToMany(mappedBy = "demoEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
  private List<DemoParts> parts;

  public void setParts(List<DemoParts> parts) {
    this.parts = parts;
    parts.forEach(p -> p.setDemoEntity(this));
  }

  @PrePersist
  public void setDateBeforeSaving() {
    this.recorded = LocalDateTime.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DemoEntity that = (DemoEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "DemoEntity{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", recorded=" + recorded +
               '}';
  }
}
