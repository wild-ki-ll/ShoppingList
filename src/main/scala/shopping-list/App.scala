package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactDOM}

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import japgolly.scalajs.react.vdom.prefix_<^._

import ItemsMode.createItemsMode
import ShopsMode.createShopsMode

object App extends JSApp {

  class Backend($: BackendScope[Unit, State]) {
    def createMenu =
      <.menu(
        <.button( "Покупки",    ^.onClick --> setActiveMode(items.id)),
        <.button( "Магазины",   ^.onClick --> setActiveMode(shops.id)),
        <.button( "Справочники",^.onClick --> setActiveMode(refs.id)),
        <.button( "Настройки",  ^.onClick --> setActiveMode(sett.id))
      )

    def setActiveMode(idMode: Int) = $.modState(s => s.copy(activeMode = idMode))

    def emptyMode() = <.div("Нет такого режима")

    def setupActiveMode(idMode: Int) = {
      idMode match {
        case 1 => createItemsMode()
        case 2 => createShopsMode()
        //        case 3 => createReferenceMode()
        //        case 4 => createSettingsMode()
        case _ => emptyMode()
      }
    }

    def render(s: State) =
      <.div (
        createMenu(),
        setupActiveMode(s.activeMode)
      )
  }

  def items = Mode(id=1, name="Покупки")
  def shops = Mode(id=2, name="Магазины")
  def refs  = Mode(id=3, name="Спрвочники")
  def sett  = Mode(id=4, name="Настройки")

  def initState = State(
    modes = List(items, shops, refs, sett),
    0/*activate empty mode*/)

  val MainApp = ReactComponentB[Unit]("MainComponent")
    .initialState(initState)
    .renderBackend[Backend]
    .build

  def main(): Unit = {
    ReactDOM.render(MainApp(), document.getElementById("app"))
  }
}

