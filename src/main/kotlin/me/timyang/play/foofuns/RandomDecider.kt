package me.timyang.play.foofuns

class RandomDecider {
  fun randomElement(vararg elements: Any): Any {
    val maxIdx = elements.size - 1
    val randomIdx = (0..maxIdx).shuffled().first()
    return elements[randomIdx]
  }
}

fun main() {
  val rnd = RandomDecider().randomElement("Play Guitar", "Learn Kotlin Coding", "Sleep", "Play MGSV 5")
  println("the decide for next hour: $rnd")
}
