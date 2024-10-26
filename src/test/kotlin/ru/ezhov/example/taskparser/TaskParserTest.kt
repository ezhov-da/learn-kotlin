package ru.ezhov.example.taskparser

import org.assertj.core.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate

private val localDateNow = LocalDate.of(2024, 10, 26)

class TaskParserTest {
    @ParameterizedTest
    @MethodSource("args")
    fun `should be success parse`(input: String, result: ParsedTask) {
        Assertions.assertThat(TaskParser() { localDateNow }.parse(input)).isEqualTo(result)
    }

    companion object {
        @JvmStatic
        fun args() =
            mapOf(
                "нн Задача без даты и приоритета" to ParsedTask(date = null, sorted = 5000, text = "Задача без даты и приоритета"),
                "сн Задача на сегодня без приоритета" to ParsedTask(date = localDateNow, sorted = 5000, text = "Задача на сегодня без приоритета"),
                "с800 Задача на сегодня с приоритетом" to ParsedTask(date = localDateNow, sorted = 800, text = "Задача на сегодня с приоритетом"),
                "з700 Задача на завтра с приоритетом" to ParsedTask(
                    date = localDateNow.plusDays(1),
                    sorted = 700,
                    text = "Задача на завтра с приоритетом"
                ),
                "пз800 Задача на послезавтра с приоритетом" to ParsedTask(
                    date = localDateNow.plusDays(2),
                    sorted = 800,
                    text = "Задача на послезавтра с приоритетом"
                ),
                "пзн Задача на послезавтра без приоритета" to ParsedTask(
                    date = localDateNow.plusDays(2),
                    sorted = 5000,
                    text = "Задача на послезавтра без приоритета"
                ),
                "10н Задача на 10е число этого месяца без приоритета" to ParsedTask(
                    date = localDateNow.withDayOfMonth(10),
                    sorted = 5000,
                    text = "Задача на 10е число этого месяца без приоритета"
                ),
                "1210н Задача на 10е число декабря без приоритета" to ParsedTask(
                    date = localDateNow.withDayOfMonth(10).withMonth(12),
                    sorted = 5000,
                    text = "Задача на 10е число декабря без приоритета"
                ),
                "1210555 Задача на 10е число декабря с приоритетом" to ParsedTask(
                    date = localDateNow.withDayOfMonth(10).withMonth(12),
                    sorted = 555,
                    text = "Задача на 10е число декабря с приоритетом"
                ),
                "20250805н Задача на 5е число августа 2025 года без приоритета" to ParsedTask(
                    date = localDateNow.withDayOfMonth(5).withMonth(8).withYear(2025),
                    sorted = 5000,
                    text = "Задача на 5е число августа 2025 года без приоритета"
                ),
                "202508052300 Задача на 5е число августа 2025 года с приоритетом" to ParsedTask(
                    date = localDateNow.withDayOfMonth(5).withMonth(8).withYear(2025),
                    sorted = 2300,
                    text = "Задача на 5е число августа 2025 года с приоритетом"
                ),
            ).map { e -> Arguments.of(e.key, e.value) }
    }
}
