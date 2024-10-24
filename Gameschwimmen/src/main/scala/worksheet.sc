println("Willkommen bei Schwimmen :)")

val suits = Seq("♥", "♠", "♦", "♣")
val ranks = Seq("A", "K", "Q", "J", "10", "9", "8", "7")

println("Das ist das Kartendeck: ")
for (suit <- suits; rank <- ranks) {
  println("+-------+")
  println(f"|  $rank%-2s$suit  |")
  println("|       |")
  println("|       |")
  println("+-------+")
  println()
}
