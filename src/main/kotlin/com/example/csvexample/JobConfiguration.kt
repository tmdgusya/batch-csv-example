package com.example.csvexample

import com.example.csvexample.configuration.JdbcCustomCursor
import com.example.csvexample.entity.User
import com.example.csvexample.extractor.UserExtractor
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersIncrementer
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.FieldExtractor
import org.springframework.batch.item.file.transform.LineAggregator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import javax.sql.DataSource

@Configuration
class JobConfiguration(
  val jobBuilderFactory: JobBuilderFactory,
  val stepBuilderFactory: StepBuilderFactory,
  val dataSource: DataSource
) {

  @Bean
  fun userItemToCSVJob(): Job {
    return jobBuilderFactory
      .get(USER_ITEM_TO_CSV_JOB)
      .start(userItemToCsvStep())
      .incrementer(RunIdIncrementer())
      .build()
  }

  @Bean
  fun userItemToCsvStep(): Step {
    return stepBuilderFactory
      .get(USER_ITEM_TO_CSV_STEP)
      .chunk<User, User>(chunkSize)
      .reader(JdbcCustomCursor(dataSource).jdbcCustomItemReader(
        fetchSize = chunkSize,
        sql = FIND_ALL_USERS_QUERY,
        clazz = User::class.java
      ))
      .writer(userItemToCsvWriter())
      .build()
  }

  @Bean
  fun userItemToCsvWriter(): FlatFileItemWriter<User> {
    return FlatFileItemWriterBuilder<User>()
      .resource(FileSystemResource("src/main/resources/$CSV_FILE_NAME"))
      .append(true)
      .lineAggregator(userDataCsvLineAggregator())
      .headerCallback {
        val headers = listOf("아이디", "이름", "이메일")
        it.write(headers.joinToString(","))
      }
      .name(USER_ITEM_TO_CSV_WRITER)
      .build()
  }

  private fun userDataCsvLineAggregator(): LineAggregator<User> {
    val aggregator = DelimitedLineAggregator<User>()
    aggregator.setDelimiter(",")
    aggregator.setFieldExtractor(UserExtractor())
    return aggregator
  }

  companion object {
    const val USER_ITEM_TO_CSV_JOB = "USER_ITEM_TO_CSV_JOB"
    const val USER_ITEM_TO_CSV_STEP = "USER_ITEM_TO_CSV_STEP"
    const val USER_ITEM_TO_CSV_WRITER = "USER_ITEM_TO_CSV_WRITER"
    const val chunkSize = 10
    const val FIND_ALL_USERS_QUERY = "SELECT id, name, email FROM user"
    const val CSV_FILE_NAME = "user_data.csv"
  }

}