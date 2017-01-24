/**
  * Created by kirill on 24.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, Callback}
import japgolly.scalajs.react.vdom.prefix_<^._

object ShopsMode {
  class Backend($: BackendScope[Unit, List[Shop]]) {

    def addShop   () = $.modState(s => s :+ Shop("1", 2, "3"))
    def editShop  () = $.modState(s => s)
    def deleteShop() = $.modState(s => s)
    def filterShop() = $.modState(s => s)

    def createMenu =
      <.menu(
        <.button("Добавить",      ^.onClick --> addShop()),
        <.button("Редактировать", ^.onClick --> editShop()),
        <.button("Удалить",       ^.onClick --> deleteShop()),
        <.button("Фильтр",        ^.onClick --> filterShop())
      )

    def createTable(s: List[Shop]) = {
      <.div(
        <.table(
          <.tbody(
            <.tr(
              <.td("Название"),
              <.td("Категория"),
              <.td("Адрес")
            ),
            s.map(sh => {
              <.tr(
                <.td (sh.name),
                <.td (sh.category),
                <.td (sh.address)
              )
            })
          )
        ),
        <.div ("Всего в списке " + s.length.toString + " записей" )
      )
    }
    def render(s: List[Shop]) =
      <.div (
        createMenu(),
        createTable(s)
      )
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(List[Shop]())
    .renderBackend[Backend]
    .build

  val createShopsMode = <.div(createMode())
}
