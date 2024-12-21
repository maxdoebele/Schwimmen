package Controller.Command

trait Command {

  def execute() : Unit

  def undoStep(): Unit
  
  def redoStep(): Unit
  
  
}
