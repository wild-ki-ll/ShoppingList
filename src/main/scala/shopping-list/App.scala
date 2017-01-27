package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactDOM}

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import japgolly.scalajs.react.vdom.prefix_<^._

import ItemsMode.createItemsMode
import ShopsMode.createShopsMode
import NewItemsMode.createNewItemsMode

object App extends JSApp {

  class Backend($: BackendScope[Unit, State]) {
    def createMenu =
      <.menu(
        <.button( items.name,     ^.onClick --> setActiveMode(items.id)     ),
        <.button( shops.name,     ^.onClick --> setActiveMode(shops.id)     ),
        <.button( refs.name,      ^.onClick --> setActiveMode(refs.id)      ),
        <.button( sett.name,      ^.onClick --> setActiveMode(sett.id)      ),
        <.button( newItems.name,  ^.onClick --> setActiveMode(newItems.id)  )
      )

    def setActiveMode(idMode: Int) = $.modState(s => s.copy(activeMode = idMode))

    def emptyMode() = <.div("Нет такого режима")

    def setupActiveMode(idMode: Int) = {
      idMode match {
        case 1 => createItemsMode()
        case 2 => createShopsMode()
        //        case 3 => createReferenceMode()
        //        case 4 => createSettingsMode()
        case 5 => createNewItemsMode()
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
  def newItems = Mode(id=5, name="Покупки(new)")

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

