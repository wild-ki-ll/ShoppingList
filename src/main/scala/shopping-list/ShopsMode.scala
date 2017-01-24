/**
  * Created by kirill on 24.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._

object ShopsMode {

  case class StateShopMode(isList: Boolean = true, name: String = "", addr: String = "", list: List[Shop] = List[Shop]())

  class Backend($: BackendScope[Unit, StateShopMode]) {

    // change state
    def showItemData()= $.modState(s => s.copy(isList = false))
    def showList()    = $.modState(s => s.copy(isList = true))
    def deleteShop()  = $.modState(s => s)
    def filterShop()  = $.modState(s => s)
    def addShop()     = $.modState(s => s.copy(isList = true, name="", addr="", list = s.list :+ Shop(s.name, 0, s.addr)))

    def onChangeName(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(name = newVal))
    }

    def onChangeAddress(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(addr = newVal))
    }

    // create UI
    def createMenu =
      <.menu(
        <.button("Добавить",      ^.onClick --> showItemData()),
        <.button("Редактировать", ^.onClick --> showItemData()),
        <.button("Удалить",       ^.onClick --> deleteShop()),
        <.button("Фильтр",        ^.onClick --> filterShop())
      )

    def createItem(sh: Shop) = {
      <.div (
        <.div (<.div ("Название"), <.input(^.`type` := "text", ^.value  := sh.name,    ^.onChange ==> onChangeName)),
        <.div (<.div ("Категория"),<.input(^.`type` := "text", ^.value  := sh.category)),
        <.div (<.div ("Адрес"),    <.input(^.`type` := "text", ^.value  := sh.address, ^.onChange ==> onChangeAddress)),
        <.div (
          <.button("Сохранить", ^.onClick --> addShop()),
          <.button("Отмена",    ^.onClick --> showList())
        )
      )
    }

    def createTable(s: List[Shop]) = {
      <.div(
        <.table(
          <.caption("Спиок магазинов"),
          <.thead(
             <.tr(
               <.td("Название"),
               <.td("Категория"),
               <.td("Адрес")
             )
          ),
          <.tbody(
            s.map(sh => {
              <.tr(
                <.td (sh.name),
                <.td (sh.category),
                <.td (sh.address)
              )
            })
          )
        ),
        <.div ("Всего строк в списке: " + s.length.toString)
      )
    }

    def render(s: StateShopMode) = {
      <.div(
        createMenu(),
        if (s.isList) createTable(s.list) else createItem(Shop(s.name, 0, s.addr))
      )
    }
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(StateShopMode())
    .renderBackend[Backend]
    .build

  val createShopsMode = <.div(createMode())
}
