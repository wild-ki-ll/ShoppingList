/**
  * Created by kirill on 20.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import ShoppingList.SLApp.State

object Menu {

  class Menu($: BackendScope[Unit, State]) {
    def render(s: State) =
      <.div(
        <.menu(
          s.map(i => <.button(
            ^.onClick --> i._2,
            i._1))))
  }

  def ButtonPress1: Callback =  Callback.alert("Список покупок")
  def ButtonPress2: Callback =  Callback.alert("Список магазинов")
  def ButtonPress3: Callback =  Callback.alert("Список категорий покупок")
  def ButtonPress4: Callback =  Callback.alert("Список категорий магазинов")
  def ButtonPress5: Callback =  Callback.alert("Настройки")


  val MainMenu =
    ReactComponentB[Unit]("MainMenu")
      .initialState(Vector(
        ("Список покупок",              ButtonPress1),
        ("Список магазинов",            ButtonPress2),
        ("Список категорий покупок",    ButtonPress3),
        ("Список категорий магазинов",  ButtonPress4),
        ("Настройки",                   ButtonPress5)
      ))
      .renderBackend[Menu]
      .build
}
