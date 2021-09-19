package ru.ezhov.example.chapters.c1_Getting_started_with_Kotlin

class AppCompanionObjectFromFun {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AppCompanionObjectFromFun().run()
        }
    }

    fun run() {
        println("Hello World")
    }
}