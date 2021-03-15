package com.azoft.therion.mobile.entity;


import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "mobile_users")
@NoArgsConstructor
public class MobileUser extends AbstractTimestampAndSoftDeleteEntity<Long> {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String phone;

  private String password;

  private OffsetDateTime passwordSentAt;

  private Integer badLoginAmount;

  private OffsetDateTime firstBadLoginDate;

  private OffsetDateTime loginBlockedUntil;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "mobile_user_mobile_role",
      joinColumns = @JoinColumn(name = "mobile_user_id"),
      inverseJoinColumns = @JoinColumn(name = "mobile_role_id"))
  private Set<MobileRole> mobileRoles = new HashSet<>();

  public MobileUser(String name, String phone, String password) {
    this.name = name;
    this.phone = phone;
    this.password = password;
  }

  public boolean isLoginBlocked() {
    return loginBlockedUntil != null && loginBlockedUntil.isAfter(OffsetDateTime.now());
  }

  public void increaseBadLoginAmount() {

    if (badLoginAmount == null) {
      badLoginAmount = 1;
    } else {
      badLoginAmount += 1;
    }
  }
}
