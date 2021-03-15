package com.azoft.therion.mobile.entity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@MappedSuperclass
public abstract class AbstractTimestampEntity<I> extends AbstractEntity<I> {

  @Column(nullable = false)
  private OffsetDateTime created;

  @Column
  private OffsetDateTime updated;


  @PrePersist
  public void prePersist() {
    created = OffsetDateTime.now();
    updated = created;
  }

  @PreUpdate
  public void preUpdate() {
    updated = OffsetDateTime.now();
  }
}
