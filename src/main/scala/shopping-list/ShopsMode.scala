/**
  * Created by kirill on 24.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._

object ShopsMode {

  object ModeType extends Enumeration {
    type ModeType = Value
    val listMode, itemMode, filterMode = Value
  }
  import ModeType._

  def emptyShop: Shop = Shop(0, "", 0, "")
  case class StateShopMode(modeType: ModeType = listMode, curr: Shop = emptyShop, list: List[Shop] = List[Shop]())

  class Backend($: BackendScope[Unit, StateShopMode]) {

    // change state
    def showItemData()      = $.modState(s => s.copy(modeType = itemMode))
    def showList()          = $.modState(s => s.copy(modeType = listMode))
    def showFilter()        = $.modState(s => s.copy(modeType = filterMode))
    def delShop(delId: Int) = $.modState(s => s.copy(list = s.list.filter(sh=> sh.id != delId)))

    def addShop()     = $.modState(s => {
      val newId = if (s.list.length > 0) s.list.last.id + 1 else 1
      s.copy(modeType = listMode, curr = emptyShop, list = s.list :+ Shop(newId, s.curr.name, s.curr.category, s.curr.address))
    })

    def onChangeName(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(curr = Shop(s.curr.id, newVal, s.curr.category, s.curr.address)))
    }

    def onChangeAddress(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(curr = Shop(s.curr.id, s.curr.name, s.curr.category, newVal)))
    }

    // create UI
    def createMenu =
      <.menu(
        <.button("Добавить",      ^.onClick --> showItemData()),
        <.button("Редактировать", ^.onClick --> showItemData()),
        <.button("Фильтр",        ^.onClick --> showFilter())
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
               <.td("Адрес"),
               <.td("Действия")
             )
          ),
          <.tbody(
            s.map(sh => {
              <.tr(
                <.td (sh.name),
                <.td (sh.category),
                <.td (sh.address),
                <.td (
                  <.button ("X", ^.onClick --> delShop(sh.id))
                )
              )
            })
          )
        ),
        <.div ("Всего строк в списке: " + s.length.toString)
      )
    }

    def createFilter = {<.div("Тут будет фильтр")}

    def createDataMode(s: StateShopMode) =
      s.modeType match {
        case ModeType.itemMode    => createItem(s.curr)
        case ModeType.filterMode  => createFilter()
        case _                    => createTable(s.list)
      }

    def render(s: StateShopMode) = {
      <.div(
        createMenu(),
        createDataMode(s)
      )
    }
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(StateShopMode())
    .renderBackend[Backend]
    .build

  val createShopsMode = <.div(createMode())
}
