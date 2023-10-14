package com.vulinh.model.entity;

import com.vulinh.utils.JPAIdentifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "user")
@Where(clause = "is_disabled is null or is_disabled = false")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@With
public class User extends JPAIdentifiable<String> {

  @Serial private static final long serialVersionUID = 5596380319401903335L;

  @Id private String id;

  private String username;
  private String password;
  private Boolean isDisabled;
  private String description;
}
