package utilTest

import Controller.Command.Command
import org.scalatest.wordspec.AnyWordSpec
import util.UndoManager

class UndoManagerTest extends AnyWordSpec {

  class TestCommand(var state: String) extends Command {
    override def execute(): Unit = {
      state = "Executed"
    }

    override def undoStep(): Unit = {
      state = "Undone"
    }

    override def redoStep(): Unit = {
      state = "Redone"
    }
  }

  "UndoManager" should {

    "execute a command and ensure it behaves correctly" in {
      val undoManager = new UndoManager()
      val command = new TestCommand("Initial")

      undoManager.execute(command)

      assert(command.state == "Executed", "Command should be executed after calling execute()")
    }

    "undo a command and ensure its state is updated correctly" in {
      val undoManager = new UndoManager
      val command = new TestCommand("Initial")

      undoManager.execute(command)
      undoManager.undoStep()

      assert(command.state == "Undone", "Command should be undone after calling undoStep()")
    }

    "redo a command and ensure its state is updated correctly" in {
      val undoManager = new UndoManager
      val command = new TestCommand("Initial")

      undoManager.execute(command)
      undoManager.undoStep()
      undoManager.redoStep()

      assert(command.state == "Redone", "Command should be redone after calling redoStep()")
    }

    "do nothing when undo or redo is called on empty stacks" in {
      val undoManager = new UndoManager
      val command = new TestCommand("Initial")

      undoManager.undoStep()
      assert(command.state == "Initial", "Command state should remain unchanged when undo is called on an empty stack")

      undoManager.redoStep()
      assert(command.state == "Initial", "Command state should remain unchanged when redo is called on an empty stack")
    }

    "handle multiple undo and redo operations correctly" in {
      val undoManager = new UndoManager
      val command1 = new TestCommand("Initial1")
      val command2 = new TestCommand("Initial2")

      undoManager.execute(command1)
      undoManager.execute(command2)

      // Erster Undo
      undoManager.undoStep()
      assert(command2.state == "Undone", "Second command should be undone after the first undo")
      assert(command1.state == "Executed", "First command should remain executed after the first undo")

      // Zweiter Undo
      undoManager.undoStep()
      assert(command1.state == "Undone", "First command should be undone after the second undo")

      // Erster Redo
      undoManager.redoStep()
      assert(command1.state == "Redone", "First command should be redone after the first redo")

      // Zweiter Redo
      undoManager.redoStep()
      assert(command2.state == "Redone", "Second command should be redone after the second redo")
    }
  }
}
