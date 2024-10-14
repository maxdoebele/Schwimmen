val name = "Schwimmen"
object Zeichen extends Enumeration {
  type Zeichen = Value
  val Pik, Herz, Kreuz, Caro = Value
}

object Zahl extends Enumeration {
  type Zahl = Value
  val 7, 8, 9, 10 = Value
}

println(s"Willkommen bei $name")
println("Es gibt diese Zeichen: ")
for (zeichen <- Zeichen.values) {
  println(zeichen)
}
// println("Es gibt diese Karten: " + Zeichen._)
println(s"mit diesen Zahlen: $Zahl")



