package me.timyang.play.adventofcode2021

/**
 * @link https://adventofcode.com/2021/day/3
 */

fun main() {
//  val inputRecs = """00100
//11110
//10110
//10111
//10101
//01111
//00111
//11100
//10000
//11001
//00010
//01010
//  """.trimIndent().split("\n")
  val inputRecs = Utils.readFileLines("src/main/resources/adventofcode/d3_input")
  val bucketSize = inputRecs[0].toCharArray().size
  val arr2d = Array(bucketSize) {
    Array(2) {0}
  }
  inputRecs.forEach {
    it.toCharArray().forEachIndexed {
      index, c -> arr2d[index][c.digitToInt()]++
    }
  }
  var gammaRte = 0
  var epsilonRte = 0
  arr2d.forEach {
    val gIndex = it.indices.maxByOrNull { bit -> it[bit] }!!
    gammaRte = gammaRte shl 1 or gIndex
    epsilonRte = epsilonRte shl 1 or ((gIndex + 1) % 2)
  }
  println("gamma rate: $gammaRte, bin: ${gammaRte.toString(2)}")
  println("epsilon rate: $epsilonRte, bin: ${epsilonRte.toString(2)}")

  println("power consumption: ${gammaRte * epsilonRte}")
}
