/**
  * Created by kirill on 20.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._

object MainMenu {

  class MainMenu($: BackendScope[Unit, State]) {
    def render(s: State) =
      <.div(
        <.menu(
          <.button( "Покупки",    ^.onClick --> ButtonPress1),
          <.button( "Магазины",   ^.onClick --> ButtonPress2),
          <.button( "Справочники",^.onClick --> ButtonPress3),
          <.button( "Настройки",  ^.onClick --> ButtonPress4)
        ))

    def ButtonPress1: Callback =  $.modState(_.copy(txt = "button1"))
    def ButtonPress2: Callback =  $.modState(_.copy(txt = "button2"))
    def ButtonPress3: Callback =  $.modState(_.copy(txt = "button3"))
    def ButtonPress4: Callback =  $.modState(_.copy(txt = "button4"))
  }

  val createMainMenu =
    ReactComponentB[Unit]("MainMenu")
      .initialState(AppObj.initState)
      .renderBackend[MainMenu]
      .build
}



