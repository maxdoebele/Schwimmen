package util

/**
 * Observer trait
 * update method to update the observer
 * @see Observable
 *      UndoManager
 */

trait Observer {
  def update(): Unit
}