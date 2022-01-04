package me.timyang.play.adventofcode2021

class D4GiantSquid(
  var boards: List<Board>,
  val calls: List<Int>
) {
  fun calling(calls: List<Int>) {
    calls.forEach { call ->
      boards.forEach { b ->
        b.markCalled(call)
        if (!b.completed && b.isComplete()) {
          val sumOfUnmarkeds = b.unmarked().sumOf { grid -> grid.value }
          println("bingo: $call, unmarkedSum: $sumOfUnmarkeds")
          println("final score: ${call * sumOfUnmarkeds}")
          b.completed = true
        }
      }
    }
  }

  fun printCalls() {
    calls.forEach { print("$it,")}
  }

  fun printBoards() {
    boards.forEachIndexed {
        index, board ->
      println("## board $index: ")
      println(board)
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

  fun parseInput(lines: List<String>): Pair<List<Int>, List<Board>> {
    val calls = lines[0].split(",").map { it.toInt() }
    val rowSplitPattern = """\s+""".toRegex()
    val boards = lines.drop(1)
      .filter { it.isNotBlank() }
      .map { it.trim().split(rowSplitPattern)  }
      .chunked(5)
      .map {
        Board.fromCollection(it)
      }
    return calls to boards
  }

  val (calls, boards) = parseInput(input)
  val game = D4GiantSquid(boards, calls)

  game.calling(calls)
}

data class Board(val rows: List<List<Grid>>, var completed: Boolean = false) {

  companion object {
    fun fromCollection(rows: List<List<String>>): Board {
      return Board(rows.map { cols -> cols.map { Grid(value=it.toInt()) } })
    }
  }

  fun markCalled(call: Int){
    this.rows.map { columns ->
      columns.map { grid ->
        if (grid.value == call) {
          grid.mark()
        }
      }
    }
  }

  fun unmarked(): List<Grid> {
    return rows.flatten().filterNot { it.marked }
  }

  fun isComplete() = checkRow() || checkColumn()

  private fun checkRow(): Boolean {
    return rows.any {
      it.all { grid -> grid.marked }
    }
  }

  private fun checkColumn(): Boolean {
    return rows.indices.any { col ->
      rows[0].indices.map { row -> rows[row][col] }.all { grid -> grid.marked }
    }
  }
}

class Grid(
  var marked: Boolean = false,
  val value: Int
) {
  fun mark() {
    this.marked = true
  }
}
