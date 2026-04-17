package com.calmvest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.calmvest"])
class CalmvestApplication

fun main(args: Array<String>) {
    runApplication<CalmvestApplication>(*args)
}
