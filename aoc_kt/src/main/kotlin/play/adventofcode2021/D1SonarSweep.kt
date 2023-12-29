package me.timyang.play.adventofcode2021

import java.io.File
import java.nio.charset.StandardCharsets

/**
 * @link https://www.adventofcode.com/2021/day/1
 */
class D1SonarSweep {
}

fun main() {

  val sweepRecords = Utils.readFileLines("src/main/resources/adventofcode/d1_input");
  var increasedCnt = 0

  sweepRecords.forEachIndexed { index, s ->
    val increased = index > 0 && s.toInt() > sweepRecords[index - 1].toInt()
    if (index == 0) {
      println("no previous record for [$index]$s")
    } else {
      println("compare: [${index - 1}]:${sweepRecords[index - 1]} ${if (increased) "<" else ">"} [$index]$s : ${if (increased) "increase" else "decreased"}")
      if (increased) { increasedCnt += 1 }
    }
  }
  println(increasedCnt)

}
