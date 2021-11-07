package ru.ezhov.example.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * https://habr.com/ru/post/432942/
 * Паттерны и антипаттерны корутин в Kotlin
 */
fun main() = runBlocking {
    Example1.bad()
//    Example1.good()
}

/**
 * Оборачивайте асинхронные вызовы в coroutineScope или используйте SupervisorJob для обработки исключений.
 * Если в блоке async может произойти исключение, не полагайтесь на блок try/catch.
 */
object Example1 {

    /**
     * В приведённом примере функция doWork запускает новую корутину (1),
     * которая может выбросить необработанное исключение.
     * Если вы попытаетесь обернуть doWork блоком try/catch (2), приложение всё равно упадёт.
     * Это происходит потому, что отказ любого дочернего компонента job приводит к немедленному
     * отказу его родителя.
     */
    suspend fun bad() {
        val job: Job = Job()
        val scope = CoroutineScope(Dispatchers.Default + job)

        // may throw Exception
        fun doWorkAsync(): Deferred<String> = scope.async { // (1)
            if (1 == 1) throw IllegalStateException("OOps") else "GOOD"
        }

        fun loadData() = scope.launch {
            try {
                doWorkAsync().await() // (2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        loadData().join()

        for (i in 5..10) {
            coroutineScope {
                println("Test $i")
                delay(100)
            }
        }
    }

    /**
     *  Один из способов избежать ошибки — использовать SupervisorJob (1).
     *  Сбой или отмена выполнения дочернего компонента не
     *  приведёт к сбою родителя и не повлияет на другие компоненты.
     */
    suspend fun good() {
        val job = SupervisorJob()                               // (1)
        val scope = CoroutineScope(Dispatchers.Default + job)

        // may throw Exception
        fun doWorkAsync(): Deferred<String> = scope.async { // (1)
            if (1 == 1) throw IllegalStateException("OOps") else "GOOD"
        }

        fun loadData() = scope.launch {
            try {
                doWorkAsync().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        loadData().join()

        for (i in 5..10) {
            coroutineScope {
                println("Test $i")
                delay(100)
            }
        }
    }
}