package com.baloise.open.xday.testcontainers.jpa.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "COMBINED_VIEW")
public class DemoViewUsingListagg {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "PARTS")
  private String parts;

  @Override
  public String toString() {
    return "DemoViewUsingListagg{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", parts='" + parts + '\'' +
               '}';
  }
}
