/**
  * Created by kirill on 24.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._

object ShopsMode {

  object ModeType extends Enumeration {
    type ModeType = Value
    val add, edit, view = Value
  }
  import ModeType._

  def emptyShop: Shop = Shop(0, "", 0, "")
  case class StateShopMode(modeType: ModeType = view, curr: Shop = emptyShop, list: List[Shop] = List[Shop]())

  class Backend($: BackendScope[Unit, StateShopMode]) {

    // change state
    def add()   = $.modState(s => s.copy(modeType = ModeType.add))
    def edit()  = $.modState(s => s.copy(modeType = ModeType.edit))
    def cancel()= $.modState(s => s.copy(modeType = ModeType.view, curr = emptyShop))

    def save() = {
      $.modState(s => {
        s.modeType match {
          case ModeType.add   => {
            val newId = if (s.list.length > 0) s.list.last.id + 1 else 1
            s.copy(modeType = view, curr = emptyShop, list = s.list :+ Shop(newId, s.curr.name, s.curr.category, s.curr.address))
          }
          case ModeType.edit  => s.copy(modeType = view)
          case _ => s.copy(modeType = view)
        }
      })
    }

    def onChangeName(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(curr = Shop(s.curr.id, if (s.modeType == view) s.curr.name else newVal, s.curr.category, s.curr.address)))
    }

    def onChangeAddress(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(curr = Shop(s.curr.id, s.curr.name, s.curr.category, if (s.modeType == view) s.curr.address else newVal)))
    }

    // create UI
    def table(s: List[Shop]) = {
      <.div(
        <.table(
          <.caption(
            <.label("Спиок магазинов", ^.float := "left" ),
            <.div ( ^.float := "right",
              <.button("Ф"),
              <.button("X")
            )
          ),
          <.thead(
             <.tr(
               <.td(<.input.checkbox()),
               <.td("Название"),
               <.td("Категория"),
               <.td("Адрес")
             )
          ),
          <.tbody(
            s.map(sh => {
              <.tr(
                <.td(<.input.checkbox()),
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

    def details(sh: Shop) = {
      <.div (
        <.div( ^.float := "left",
          <.div(<.label ("Название"), <.input (^.`type` := "text", ^.value  := sh.name, ^.onChange ==> onChangeName)),
          <.div(<.label ("Категория"),<.select(
            <.option("Продуктовый"),
            <.option("Продовольственный"),
            <.option("Прочее"),
            ^.value  := sh.category)),
          <.div (<.label ("Адрес"),   <.input (^.`type` := "text", ^.value  := sh.address, ^.onChange ==> onChangeAddress))
        ),
        <.div ( ^.float := "right",
          <.div (
            <.button("+", ^.onClick --> add),
            <.button("/", ^.onClick --> edit)
          ),
          <.div (
            <.button("V", ^.onClick --> save),
            <.button("X", ^.onClick --> cancel)
          )
        )
      )
    }

    def render(s: StateShopMode) = {
      <.div(^.float := "left",
        details(s.curr),
        table(s.list)
      )
    }
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(StateShopMode())
    .renderBackend[Backend]
    .build

  val createShopsMode = <.div(createMode())
}
