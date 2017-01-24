/**
  * Created by kirill on 24.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, Callback}
import japgolly.scalajs.react.vdom.prefix_<^._

object ShopsMode {
  class Backend($: BackendScope[Unit, List[Shop]]) {
    def createMenu =
      <.menu(
        <.button("Добавить", ^.onClick --> Callback.alert("Добавить в режиме магазинов")),
        <.button("Редактировать"),
        <.button("Удалить"),
        <.button("Фильтр")
      )

    def render(s: List[Shop]) =
      <.div (
        createMenu(),
        <.div ("Режим магазинов")
      )
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(List[Shop]())
    .renderBackend[Backend]
    .build

  val createShopsMode = <.div(createMode())
}
