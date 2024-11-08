import View.Main
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}

class MainTest extends AnyWordSpec with Matchers {

  "The Main object" should {

    "greet the players, accept their names, shuffle the deck, and output the results" in {
      val simulatedInput = new ByteArrayInputStream("Max Emilia Laurin\n".getBytes)
      // Capture the output
      val outputCapture = new ByteArrayOutputStream()

      // Redirect input and output to our simulated streams
      Console.withIn(simulatedInput) {
        Console.withOut(outputCapture) {
          // Run the main method
          Main.main(Array.empty)
        }
      }

      // Capture the output as a string and perform assertions
      val output = outputCapture.toString

      // Check if the program's greeting, player prompt, and shuffling messages are printed
      output should include ("Willkommen bei Schwimmen :)")
      output should include ("Bitte sag mir die Namen der Mitspieler:")
      output should include ("Alles Klar, die Karten werden nun gemischt und ausgeteilt!")
    }
  }
}
