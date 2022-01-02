package me.timyang.play.adventofcode2021

import java.lang.StringBuilder

/**
 * @link https://adventofcode.com/2021/day/3
 */
fun ans1(inputRecs: List<String>): Int {
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

  return gammaRte * epsilonRte
}

fun ans2(inputRecs: List<String>): Int {

  fun calculateLifeSupportRating(rateType: RateType): Int {
    val patternStr = StringBuilder("")
    for (i in inputRecs[0].toCharArray().indices) {
      val arr = IntArray(2){0}
      val filtered = inputRecs.filter { it.startsWith(patternStr.toString()) }
      filtered.forEach {
        arr[it[i].digitToInt()]++
      }
      if (filtered.size == 1) {
        return filtered.single().toInt(2)
      }

      val patternBit =  when (rateType) {
        RateType.OxygenRating -> if (arr[0] == arr[1]) 1 else arr.indices.maxByOrNull { bit -> arr[bit] }!!
        RateType.CO2Rating -> if (arr[0] == arr[1]) 0 else arr.indices.minByOrNull { bit -> arr[bit] }!!
      }
      patternStr.append(patternBit)
    }
    return patternStr.toString().toInt(2)
  }
  val oxygenGenRating = calculateLifeSupportRating(RateType.OxygenRating)
  val co2ScrubberRate = calculateLifeSupportRating(RateType.CO2Rating)
  println("oxygen generator rating: $oxygenGenRating, bin: ${oxygenGenRating.toString(2)}")
  println("CO2 Scrubber rating: $co2ScrubberRate, bin: ${co2ScrubberRate.toString(2)}")

  return oxygenGenRating * co2ScrubberRate
}

enum class RateType {
  OxygenRating, CO2Rating
}

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
  println("power consumption: ${ans1(inputRecs)}")
  println("life support rating: ${ans2(inputRecs)}")
}
