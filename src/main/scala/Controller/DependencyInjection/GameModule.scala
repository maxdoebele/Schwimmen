package Controller.DependencyInjection

import Controller.COR.CORImpl._
import Controller.GameBuilder.GameBuilder
import Model.BaseImpl.{GameState, User}
import Controller.GameBuilder.GameBuilderImpl.{BuildNewGame, BuildNewRound}
import Controller.Controller
import FileIO.FileIOImpl.{FileIOJSON, FileIOYAML}
import FileIO.FileIO
import com.google.inject.{AbstractModule, Singleton}
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule

class GameModule extends AbstractModule with ScalaModule{
  override def configure(): Unit = {
    bind[Seq[String]].annotatedWith(Names.named("playerNames")).toInstance(Seq.empty[String])
    bind[GameBuilder].to[BuildNewGame]
    bind[Controller].in(classOf[Singleton])
    bind[FileIO].to[FileIOJSON]
    //bind[FileIO].to[FileIOXML]
    //bind[FileIO].to[FileIOYAML]
  }
}