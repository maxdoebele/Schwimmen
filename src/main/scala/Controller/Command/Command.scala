package Controller.Command

/**
 * Trait for Command Pattern
 * execute method to execute wanted command
 * undoStep method to undo the command
 * redoStep method to redo the command
 * @see CommandImpl
 */

trait Command {

  def execute() : Unit

  def undoStep(): Unit
  
  def redoStep(): Unit
  
}
