package ru.ezhov.example.taskparser

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskParser(
    private val now: () -> LocalDate
) {
    fun description() =
        """
            Формат:
                ДатаСортировка текст

            Дата:
                н - не задана дата
                с - сегодня
                з - завтра
                пз - послезавтра
                10 - 10-е число текщуего месяца
                1210 - 10-е число 12-го месяца
                20250805 - дата в полном формате: ГодМесяцДень
            Сортировка:
                н - не задана сортировка (задача будет в конце)
                000 - 2350 (время дня)

            Примеры:
                нн Задача без даты и приоритета
                сн Задача на сегодня без приоритета
                с800 Задача на сегодня с приоритетом
                з800 Задача на завтра с приоритетом
                пз800 Задача на послезавтра с приоритетом
                пз800 Задача на послезавтра с приоритетом
                пзн Задача на послезавтра без приоритета
                10н Задача на 10е число этого месяца без приоритета
                1210н Задача на 10е число декабря без приоритета
                20250805н Задача на 5е число августа 2025 года без приоритета
                202508052300 Задача на 5е число августа 2025 года с приоритетом
        """.trimIndent()


    fun parse(text: String): ParsedTask {
        val configs = checkAndGetConfig(text)
        val resolveDateAndSorted = resolveDateAndSorted(configs.config)

        return ParsedTask(
            date = resolveDateAndSorted.date,
            sorted = resolveDateAndSorted.sorted,
            text = configs.text
        )
    }

    private fun checkAndGetConfig(text: String): WorkTask {
        val trimmedTask = text.trim().split(" ")
        if (trimmedTask.size < 2) {
            throw IllegalArgumentException("Wrong task text")
        }

        return WorkTask(
            config = trimmedTask[0].lowercase(),
            text = trimmedTask.subList(1, trimmedTask.size).joinToString(separator = " ")
        )
    }

    private fun resolveDateAndSorted(config: String): DateAndSortedTask =
        resolveDate(config).let { data ->
            DateAndSortedTask(
                date = data.first,
                sorted = resolveSorted(config = data.second)
            )
        }

    private fun resolveDate(config: String): Pair<LocalDate?, String> {
        if (config.startsWith("н")) return Pair(null, config.substring(1))

        val nowDateRegEx = "^с".toRegex()
        val tomorrowDateRegEx = "^з".toRegex()
        val dayAfterTomorrowRegEx = "^пз".toRegex()
        val fullDateRegEx = "^\\d{8}".toRegex()
        val dayAndMonthRegEx = "^\\d{4}".toRegex()
        val dayRegEx = "^\\d{2}".toRegex()

        return when {
            config.contains(nowDateRegEx) -> Pair(first = now(), second = config.replace(nowDateRegEx, ""))
            config.contains(tomorrowDateRegEx) -> Pair(first = now().plusDays(1), second = config.replace(tomorrowDateRegEx, ""))
            config.contains(dayAfterTomorrowRegEx) -> Pair(
                first = now().plusDays(2),
                second = config.replace(dayAfterTomorrowRegEx, "")
            )

            // Последовательность ниже важна, так как идём от большего к меньшему

            config.contains(fullDateRegEx) -> {
                val full = config.substring(0, 8)

                Pair(
                    first = LocalDate.parse(full, DateTimeFormatter.ofPattern("yyyyMMdd")),
                    second = config.replace(fullDateRegEx, "")
                )
            }

            config.contains(dayAndMonthRegEx) -> {
                val month = config.substring(0, 2)
                val day = config.substring(2, 4)

                Pair(
                    first = now()
                        .withDayOfMonth(day.toInt())
                        .withMonth(month.toInt()),
                    second = config.replace(dayAndMonthRegEx, "")
                )
            }

            config.contains(dayRegEx) -> {
                val value = dayRegEx.find(config)!!.value

                Pair(first = now().withDayOfMonth(value.toInt()), second = config.replace(dayRegEx, ""))
            }

            else -> throw IllegalArgumentException("Wrong date format 'config'")
        }
    }

    private fun resolveSorted(config: String): Int {
        if (config.endsWith("н")) return 5000

        return config.toInt()
    }
}

data class ParsedTask(
    val date: LocalDate?,
    val sorted: Int,
    val text: String,
)

private data class WorkTask(
    val config: String,
    val text: String,
)

private data class DateAndSortedTask(
    val date: LocalDate?,
    val sorted: Int,
)


