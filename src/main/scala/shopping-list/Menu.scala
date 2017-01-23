/**
  * Created by kirill on 20.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._

object MainMenu {

  class MainMenu($: BackendScope[Unit, Menu]) {
    def render(s: Menu) =
      <.div(
        <.menu(
          s.menuItems.map(i => <.button(
            ^.onClick --> i._2,
            i._1))))
  }

  def ButtonPress1: Callback =  Callback.alert("Список покупок")
  def ButtonPress2: Callback =  Callback.alert("Список магазинов")
  def ButtonPress3: Callback =  Callback.alert("Справочники")
  def ButtonPress4: Callback =  Callback.alert("Настройки")

  val createMainMenu =
    ReactComponentB[Unit]("MainMenu")
      .initialState(new Menu(List(
        ("Список покупок", ButtonPress1),
        ("Магазины",       ButtonPress2),
        ("Справочники",    ButtonPress3),
        ("Настройки",      ButtonPress4)
      )))
      .renderBackend[MainMenu]
      .build
}



