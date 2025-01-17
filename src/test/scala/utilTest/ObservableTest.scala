package utilTest

import org.scalatest.wordspec.AnyWordSpec
import util.{Observable, Observer}

class ObservableTest extends AnyWordSpec {

  class TestObserver extends Observer {
    var updated: Boolean = false

    override def update(): Unit = {
      updated = true
    }

    def reset(): Unit = {
      updated = false
    }
  }

  "An Observable" should {

    "notify all added observers when notifySubscribers is called" in {
      val observable = new Observable
      val observer1 = new TestObserver
      val observer2 = new TestObserver

      observable.add(observer1)
      observable.add(observer2)

      observable.notifySubscribers()

      assert(observer1.updated, "Observer1 should be updated")
      assert(observer2.updated, "Observer2 should be updated")
    }

    "not notify removed observers when notifySubscribers is called" in {
      val observable = new Observable
      val observer1 = new TestObserver
      val observer2 = new TestObserver

      observable.add(observer1)
      observable.add(observer2)
      observable.remove(observer1)

      observable.notifySubscribers()

      assert(!observer1.updated, "Removed observer1 should not be updated")
      assert(observer2.updated, "Observer2 should still be updated")
    }

    "do nothing when no observers are added and notifySubscribers is called" in {
      val observable = new Observable

      // Keine Observers hinzugefügt, daher sollte keine Exception auftreten oder etwas passieren
      observable.notifySubscribers()
      assert(true, "Calling notifySubscribers with no observers should not throw errors")
    }

    "allow observers to reset their state between notifications" in {
      val observable = new Observable
      val observer = new TestObserver

      observable.add(observer)

      // Erste Benachrichtigung
      observable.notifySubscribers()
      assert(observer.updated, "Observer should be updated after first notification")

      // Zustand zurücksetzen
      observer.reset()
      assert(!observer.updated, "Observer state should be reset")

      // Zweite Benachrichtigung
      observable.notifySubscribers()
      assert(observer.updated, "Observer should be updated after second notification")
    }
  }
}
