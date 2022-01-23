package com.example.csvexample.configuration

import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource

@Configuration
class JdbcCustomCursor(
  val dataSource: DataSource
) {

  fun <T> jdbcCustomItemReader(
    fetchSize: Int = 10,
    sql: String,
    clazz: Class<T>
  ): JdbcCursorItemReader<T> {
    return JdbcCursorItemReaderBuilder<T>()
      .fetchSize(fetchSize)
      .dataSource(dataSource)
      .rowMapper(BeanPropertyRowMapper<T>(clazz))
      .sql(sql)
      .name(JDBC_CURSOR_BEAN_NAME)
      .build()
  }

  companion object {
    const val JDBC_CURSOR_BEAN_NAME = "jdbcCursorItemReader"
  }

}