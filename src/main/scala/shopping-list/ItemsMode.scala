/**
  * Created by kirill on 23.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, Callback}
import japgolly.scalajs.react.vdom.prefix_<^._

object ItemsMode {
  class Backend($: BackendScope[Unit, List[Item]]) {
    def createMenu =
      <.menu(
        <.button("Добавить", ^.onClick --> Callback.alert("Добавить в режиме покупок")),
        <.button("Редактировать"),
        <.button("Удалить"),
        <.button("Фильтр")
      )

    def render(s: List[Item]) =
      <.div (
        createMenu(),
        <.div ("Режим покупок")
      )
  }

  val createMode = ReactComponentB[Unit]("ItemMode")
    .initialState(List[Item]())
    .renderBackend[Backend]
    .build

  val createItemsMode = <.div(createMode())
}
