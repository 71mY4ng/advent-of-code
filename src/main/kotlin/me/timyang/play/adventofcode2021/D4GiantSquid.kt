package me.timyang.play.adventofcode2021

import java.util.*

class D4GiantSquid(
  val boards: List<List<List<String>>>,
  val calls: List<String>
) {
  val markers = Array(boards.size) {
    Array(5) {0}
  }
  fun searchForCall(call: String): List<Pair<Int, Int>> {
    val callExistences = ArrayList<Pair<Int, Int>>()
    boards.forEachIndexed { bIndex, b ->
      b.forEachIndexed {
          rIndex, r ->
        if (r.contains(call)) {
//          println("board: $bIndex, row: $rIndex, ${r.reduceRight{item, acc -> "$item, $acc" }}, found: $call")
          callExistences.add(bIndex to rIndex)
        }
      }
    }
    return callExistences
  }
  fun calling(calls: List<String>): CallResult {
    calls.forEachIndexed {
        callIndex, call ->
      for ((b, r) in searchForCall(call)) {
        markers[b][r]++
        if (markers[b][r] == 5) {
          return CallResult(b, r, callIndex)
        }
      }
    }
    error("calling failed")
  }
  fun printCalls() {
    calls.forEach { print("$it,")}
  }
  fun printBoards() {
    boards.forEachIndexed {
        index, arrayList ->
      println("## board $index:")
      arrayList.forEach {println(it)}
    }
  }

}

fun main() {
//  val input = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
//
//22 13 17 11  0
// 8  2 23  4 24
//21  9 14 16  7
// 6 10  3 18  5
// 1 12 20 15 19
//
// 3 15  0  2 22
// 9 18 13 17  5
//19  8  7 25 23
//20 11 10 24  4
//14 21 16 12  6
//
//14 21 17 24  4
//10 16 15  9 19
//18  8 23 26 20
//22 11 13  6  5
// 2  0 12  3  7
//  """.trimIndent().lines()
  val input = Utils.readFileLines("src/main/resources/adventofcode/d4_input")

  fun parseInput(lines: List<String>): Pair<List<String>, List<List<List<String>>>> {
    val calls = lines[0].split(",")
    val rowSplitPattern = """\s+""".toRegex()
    val boards = lines.subList(1, lines.size)
      .filter { it.trim() != "" }
      .map { it.trim().split(rowSplitPattern)  }
      .chunked(5)
    return calls to boards
  }

  val (calls, boards) = parseInput(input)
  val game = D4GiantSquid(boards, calls)

  val callResult = game.calling(calls)
  val bingo = calls[callResult.callIndex].toInt()
  val called = calls.subList(0, callResult.callIndex)
  val (board, row) = callResult.bingoBoard to callResult.bingoRow
  val umarkeds = boards[board].filterIndexed {
      rIndex, _ ->  rIndex != row
  }.flatten().filter { !called.contains(it) }

  println("unmarked: ${umarkeds.reduceRight { v, acc -> "$v, $acc"}}")
  println("called: $called")

  val unmarkedSum = umarkeds.sumOf { it.toInt() }

  println("bingo: $bingo, unmarkedSum: $unmarkedSum")
  println("final score: ${bingo * unmarkedSum}")

}

class CallResult(
  val bingoBoard: Int,
  val bingoRow: Int,
  val callIndex: Int
)
