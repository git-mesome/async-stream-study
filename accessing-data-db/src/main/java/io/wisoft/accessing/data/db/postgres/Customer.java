package io.wisoft.accessing.data.db.postgres;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Customer {

  @Id
  private Long id;
  private final String firstName;
  private final String lastName;

  public Customer(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return String.format("Customer [id=%d, firstName='%s', lastName='%s']",
        id, firstName, lastName);
  }

}