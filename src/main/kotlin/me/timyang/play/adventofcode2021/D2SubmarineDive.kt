package me.timyang.play.adventofcode2021

/**
 * @link https://adventofcode.com/2021/day/2
 */
class D2SubmarineDive {

  fun partOneProc(lines: List<String>): Int {
    var horizontal = 0
    var depth = 0
    lines.forEach {
      val word = it.split(" ")
      val cnt =  word[1].toInt()
      when (word[0]) {
        "forward" -> horizontal += cnt
        "down" -> depth += cnt
        "up" -> depth -= cnt
      }
    }

    return horizontal * depth
  }

  fun part2Proc(lines: List<String>): Int {
    var horizontal = 0
    var depth = 0
    var aim = 0
    lines.forEach {
      val word = it.split(" ")
      val cnt =  word[1].toInt()
      when (word[0]) {
        "forward" -> {
          horizontal += cnt
          depth += (aim * cnt)
        }
        "down" -> {
          aim += cnt
        }
        "up" -> {
          aim -= cnt
        }
      }
    }
    return horizontal * depth
  }
}

fun main() {
  val lines = Utils.readFileLines("src/main/resources/adventofcode/d2_input")
//  val input = """
//    forward 5
//    down 5
//    forward 8
//    up 3
//    down 8
//    forward 2
//  """.trimIndent()
//
//  val lines = input.split("\n")
  val d2 = D2SubmarineDive()
  val ans1 = d2.partOneProc(lines)
  val ans2 = d2.part2Proc(lines)
  println(ans1)
  println(ans2)
}
