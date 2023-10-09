package com.vulinh.model.entity;

import com.vulinh.utils.JPAIdentifiable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.*;

@Entity
@Table(name = "user_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@With
public class UserRole extends JPAIdentifiable<String> {

  @Serial private static final long serialVersionUID = -3958537711691307428L;

  @Id private String id;

  private String description;
}
