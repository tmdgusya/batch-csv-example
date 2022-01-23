package com.example.csvexample

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class CsvExampleApplication

fun main(args: Array<String>) {
	runApplication<CsvExampleApplication>(*args)
}
