package com.example.csvexample.extractor

import com.example.csvexample.entity.User
import org.springframework.batch.item.file.transform.FieldExtractor

class UserExtractor: FieldExtractor<User> {

  override fun extract(item: User): Array<Any> {
    return arrayOf(item.id.toString(), item.name, item.email)
  }

}