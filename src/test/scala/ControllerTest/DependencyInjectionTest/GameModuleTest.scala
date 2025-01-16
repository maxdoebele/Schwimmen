package ControllerTest.DependencyInjectionTest

import org.scalatest.wordspec.AnyWordSpec
import com.google.inject.{Guice, Key}
import Controller.DependencyInjection.GameModule
import Controller.GameBuilder.GameBuilder
import Controller.GameBuilder.GameBuilderImpl.BuildNewGame
import Controller.Controller
import FileIO.FileIO
import _root_.FileIO.FileIOImpl.FileIOXML
import com.google.inject.name.Names

class GameModuleTest extends AnyWordSpec {

  "GameModule" should {

    "bind GameBuilder to BuildNewGame" in {
      val injector = Guice.createInjector(new GameModule)
      val gameBuilder = injector.getInstance(classOf[GameBuilder])
      assert(gameBuilder.isInstanceOf[BuildNewGame])
    }

    "bind Controller as a singleton" in {
      val injector = Guice.createInjector(new GameModule)
      val controller1 = injector.getInstance(classOf[Controller])
      val controller2 = injector.getInstance(classOf[Controller])
      assert(controller1 eq controller2) // Singleton check
    }

    "bind FileIO to FileIOXML" in {
      val injector = Guice.createInjector(new GameModule)
      val fileIO = injector.getInstance(classOf[FileIO])
      assert(fileIO.isInstanceOf[FileIOXML])
    }
  }
}

