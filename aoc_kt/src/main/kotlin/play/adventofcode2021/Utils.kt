package me.timyang.play.adventofcode2021

import java.io.File
import java.nio.charset.StandardCharsets

class Utils {
  companion object {
    fun readFileAsFileIO(filename: String) = File(filename).inputStream().readBytes().toString(StandardCharsets.UTF_8)
    fun readFileLines(filename: String): List<String> = File(filename).readLines()
    fun getResourceFile(filename: String) = this::class.java.getResource(filename)?.readText(StandardCharsets.UTF_8)
    fun ls(pwd: String) = File(pwd).listFiles()
  }
}
