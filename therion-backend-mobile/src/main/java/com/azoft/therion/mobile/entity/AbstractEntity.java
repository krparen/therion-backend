package com.azoft.therion.mobile.entity;

import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public abstract class AbstractEntity<I> {

  public abstract I getId();

  private static final long serialVersionUID = 6331369708130810826L;

  /**
   * Уникальный идентификатор сущности - первичный ключ.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  @Access(AccessType.PROPERTY)
  private Long id;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AbstractEntity<I> that = (AbstractEntity<I>) o;

    if (getId() == null && that.getId() == null) {
      return false;
    }

    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
