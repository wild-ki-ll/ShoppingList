package ShoppingList

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactDOM}

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import japgolly.scalajs.react.vdom.prefix_<^._
import MainMenu.createMainMenu

object AppObj extends JSApp {

  def createHelloWorld(txt: String) = <.div ("Hello World " + txt)

  class App($: BackendScope[Unit, State]) {
    def setNewState(newTxt: String) = $.modState(_.copy(txt = newTxt))

    def render(s: State) =
      <.div (
        createMainMenu(),
        createHelloWorld(s.txt),
        <.button( "text",  ^.onClick --> setNewState("text")),
        <.button( "empty", ^.onClick --> setNewState("empty"))
      )
  }

  def emptyMenu = Menu (List[String]())
  def mainMenu = emptyMenu
  def items = Items(emptyMenu, List[Item]())
  def shops = Shops(emptyMenu, List[Shop]())
  def refs = References(emptyMenu, List[Ref]())
  def set  = Settings(emptyMenu, List[SettingItem]())

  def initState = State(mainMenu, items, shops, refs, set, "")

  val MainApp = ReactComponentB[Unit]("MainComponent")
    .initialState(initState)
    .renderBackend[App]
    .build

  def main(): Unit = {
    ReactDOM.render(MainApp(), document.getElementById("app"))
  }
}

