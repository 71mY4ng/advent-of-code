package me.timyang.play.foofuns

open class Shape

class Rectangle(var height: Double, var length: Double): Shape() {
  var perimeter = (height + length) * 2
}
