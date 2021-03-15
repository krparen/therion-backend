package com.azoft.therion.mobile.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@MappedSuperclass
public abstract class AbstractTimestampAndSoftDeleteEntity<I> extends AbstractTimestampEntity<I> {

  @Column
  private Boolean isDeleted = false;
}
