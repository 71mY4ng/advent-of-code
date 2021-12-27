import me.timyang.play.foofuns.AbsClass
import me.timyang.play.foofuns.PlayFuncs
import me.timyang.play.foofuns.Rectangle
import java.io.File

fun main(args: Array<String>) {

  val func :PlayFuncs = PlayFuncs()
  val sum = func.sum(1, 5)

  val rectangle = Rectangle(5.0, 2.0)
  fun sumA(a: Int, b: Int) = a + b
  println("Hello World! ${sumA(1, 10)}")
  println("Perimeter is ${rectangle.perimeter}")

  println("Perimeter is ${ if (rectangle.perimeter > 7) "greater" else "normal"}")

  val items = listOf("apple", "banana", "kiwifruit")

  val map = mapOf("a" to 1, "b" to 2, "c" to 3)
  for ((k, v) in map) {
    println("$k -> $v")
  }

  val p:String by lazy {
    if (Rectangle(5.0, 2.0).perimeter > 7) "greater" else "normal"
  }
  println(p)

  val myObj = object : AbsClass() {
    override fun doSomething() {
      TODO("Not yet implemented")
    }

    override fun sleep() {
      TODO("Not yet implemented")
    }
  }

  val listFiles = File("src/test").listFiles()
  println(listFiles?.size ?: "empty")

  fun transformValue(input: Any) = when (input) {
    is String -> input + "_suffix"
    else -> input
  }
  fun transformValue(input: Array<File>) = input.map { it.absolutePath }

  val result = listFiles?.let {
    println("if not null: execute here")
    transformValue(it)
  } ?: "defaultValue"

  println(result)
  println(transformValue("someText"))

  infix fun Int.times(str: String) = str.repeat(this)
  println(2 times "Bye ")

  val pair = "Ferrari" to "Katrina"
  println(pair)

  infix fun String.onto(other: String) = Pair(this, other)
  val myPair = "McLaren" onto "Lucas"
  println(myPair)

  val sophia = Person("Sophia")
  val claudia = Person("Claudia")
  sophia likes claudia

  println(sophia.likedPeople)

  operator fun String.get(range: IntRange) = substring(range)
  val str = "Always forgive your enemies; nothing annoys them so much"
  println(str[0..14])

  log("Hello", "Hallo", "Nihao", "Ciao")

  var neverNull: String? = "initial"
  neverNull = null
  println(describeString(neverNull))

  val stackItems = MutableStack("a", "b", "c")
  stackItems.push("newD")
  println(stackItems)

  val user = User("Alex", 1)
  val secUser = User("Alex", 1)
  println(User("Alex", 1).hashCode() == User("Alex", 1).hashCode())
  println(user.hashCode())
  println(secUser.hashCode())
  println(user.copy())
  println(user == user.copy())
  println(user.copy(name = "Max", id = 4))

}

fun printAll(vararg msgs: String) {
  for (m in msgs) println(m)
}
fun log(vararg entries: String) {
  printAll(*entries)
}

class Person(val name: String) {
  val likedPeople = mutableListOf<Person>()
  infix fun likes(other: Person) { likedPeople.add(other) }
}

fun describeString(maybeString: String?): String {
  return if (maybeString != null && maybeString.isNotEmpty()) {
    "String of len ${maybeString.length}"
  } else {
    "String null or empty"
  }
}

class MutableStack<E>(vararg items: E) {
  private val elements = items.toMutableList()

  fun push(element: E) = elements.add(element)
  fun peek(): E = elements.last()

  override fun toString(): String = "MutableStack(${elements.joinToString()})"
}

fun <E> toMutableStack(vararg items: E) = MutableStack(*items)

data class User(val name: String, val id: Int) {
  override fun equals(other: Any?): Boolean = other is User && other.id == this.id
}
