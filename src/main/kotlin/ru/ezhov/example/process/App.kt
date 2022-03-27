package ru.ezhov.example.process

import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

fun main() {
  val timer = Timer()
  timer.schedule(
    object : TimerTask() {
      override fun run() {
        val pb = ProcessBuilder(listOf(
//    "cmd",
//    "/c",
//    "dir",
          "D:\\programs\\Git-2.28.0-64\\bin\\sh.exe",
          "-c",
          "curl --insecure -I -X GET \"https://www.google.com/\""
        ))
        pb.redirectErrorStream(true)
        pb.inheritIO()
        val process = pb.start()

        process.inputStream.use { ins ->
          println(String(ins.readAllBytes()))
        }

        process.waitFor(5, TimeUnit.SECONDS)
      }

    },
    0,
    10000
  )

//  timer.cancel()
}
