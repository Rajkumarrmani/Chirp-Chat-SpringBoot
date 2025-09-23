package com.rk.learningchirp

import Test
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LearningChirpApplication

fun main(args: Array<String>) {
	Test()
	runApplication<LearningChirpApplication>(*args)
}
