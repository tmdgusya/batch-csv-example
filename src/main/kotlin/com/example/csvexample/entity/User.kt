package com.example.csvexample.entity

import javax.persistence.*

@Entity
@Table(name = "user")
class User(
  name: String,
  email: String
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
  @Column(name = "name", length = 20)
  var name: String = name
  @Column(name = "email", length = 30)
  var email: String = email

  override fun toString(): String {
    return "User(id=$id, name='$name', email='$email')"
  }

}