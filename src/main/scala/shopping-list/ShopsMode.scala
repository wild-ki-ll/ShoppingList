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
  case class StateShopMode(
    modeType: ModeType = view,
    curr: Shop = emptyShop,
    list: List[Shop] = List[Shop](),
    checkedValues: List[Int] = List[Int]())

  class Backend($: BackendScope[Unit, StateShopMode]) {

    // change state
    def add()    = $.modState(s => s.copy(modeType = ModeType.add, curr = emptyShop))
    def edit()   = $.modState(s => s.copy(modeType = ModeType.edit))
    def cancel() = $.modState(s => s.copy(modeType = ModeType.view, curr = emptyShop))

    def onCheck(id: Int) =
      $.modState(s => {
        val newCheckedValues: List[Int] =
          if (s.checkedValues.contains(id)) {
            s.checkedValues.filter(v => v != id)
          } else {
            s.checkedValues :+ id
          }
        s.copy(checkedValues = newCheckedValues)
      })

    def onCheckAll() =
      $.modState(s => {
        val newCheckedValues: List[Int] =
          if (s.list.length == s.checkedValues.length) {
            List[Int]()
          } else {
            s.list.map(sh => sh.id)
          }
        s.copy(checkedValues = newCheckedValues)
      })

    def delete() = {
      $.modState(s => {
        s.copy(list = s.list.filter(sh => !s.checkedValues.contains(sh.id)), checkedValues = List[Int](), curr = emptyShop)
      })
    }

    def save() = {
      $.modState(s => {
        s.modeType match {
          case ModeType.add   => {
            val newId = if (s.list.length > 0) s.list.last.id + 1 else 1
            s.copy(modeType = view, curr = emptyShop, list = s.list :+ Shop(newId, s.curr.name, s.curr.category, s.curr.address))
          }
          case ModeType.edit  => {
            val newList = s.list.map(sh => if (sh.id == s.curr.id) s.curr else sh)
            s.copy(modeType = view, list = newList)
          }
          case _ => s.copy(modeType = view)
        }
      })
    }

    def setCurrentValue(id: Int) = {
      $.modState(s => s.copy(curr = s.list.filter(sh=>sh.id == id)(0)))
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
    def table(s: List[Shop], checkedValues: List[Int], curr: Shop) = {
      <.div(
        <.table(
          <.caption(
            <.label("Спиок магазинов", ^.float := "left" ),
            <.div ( ^.float := "right",
              <.button("Ф"),
              <.button("X", ^.onClick --> delete, ^.disabled := checkedValues.length == 0)
            )
          ),
          <.thead(
             <.tr(
               <.td(<.input.checkbox(^.onChange --> onCheckAll, ^.checked := checkedValues.length == s.length && s.length>0)),
               <.td("Название"),
               <.td("Категория"),
               <.td("Адрес")
             )
          ),
          <.tbody(
            s.map(sh => {
              val backgroundColor: String = if (curr.id == sh.id) "silver" else "white"
              <.tr(^.onClick --> setCurrentValue(sh.id), ^.backgroundColor := backgroundColor,
                <.td(<.input.checkbox(^.onChange --> onCheck(sh.id), ^.checked := checkedValues.contains(sh.id))),
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

    def details(sh: Shop, modeType: ModeType) = {
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
            <.button("+", ^.onClick --> add,  ^.disabled := modeType!=ModeType.view),
            <.button("/", ^.onClick --> edit, ^.disabled := modeType!=ModeType.view)
          ),
          <.div (
            <.button("V", ^.onClick --> save,   ^.disabled := modeType==ModeType.view),
            <.button("X", ^.onClick --> cancel, ^.disabled := modeType==ModeType.view)
          )
        )
      )
    }

    def render(s: StateShopMode) = {
      <.div(^.float := "left",
        details(s.curr, s.modeType),
        table(s.list, s.checkedValues, s.curr)
      )
    }
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(StateShopMode())
    .renderBackend[Backend]
    .build

  val createShopsMode = <.div(createMode())
}
