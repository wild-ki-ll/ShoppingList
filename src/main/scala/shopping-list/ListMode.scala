/**
  * Created by kirill on 26.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope}
import japgolly.scalajs.react.vdom.prefix_<^._

object ModeType extends Enumeration {
  type ModeType = Value
  val add, edit, view = Value
}
import ModeType._

object ListMode {

  trait ListItemLike[A] {
    def getEmptyItem: ListItem[A]
    def createLabelCaption: TagMod
    def createAddButtonToTable: List[TagMod]
    def createDetails(d: ListItem[A], $: BackendScope[Unit, StateListMode[A]], modeType: ModeType): TagMod
    def createHeaderColumnsTable: List[TagMod]
    def createRow(row: ListItem[A]): List[TagMod]
  }

  case class ListItem[A](id: Int, data: A)

  case class StateListMode[A](
    modeType: ModeType,
    curr: ListItem[A],
    list: List[ListItem[A]],
    checkedValues: List[Int])

  class BackendList[A]($: BackendScope[Unit, StateListMode[A]])(implicit LI: ListItemLike[A]) {
    // change state
    def add()    = $.modState(s => s.copy(modeType = ModeType.add, curr = LI.getEmptyItem))
    def edit()   = $.modState(s => s.copy(modeType = ModeType.edit))
    def cancel() = $.modState(s => s.copy(modeType = ModeType.view, curr = LI.getEmptyItem))

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
        s.copy(list = s.list.filter(sh => !s.checkedValues.contains(sh.id)), checkedValues = List[Int](), curr = LI.getEmptyItem)
      })
    }

    def save() = {
      $.modState(s => {
        s.modeType match {
          case ModeType.add   => {
            val newId = if (s.list.length > 0) s.list.last.id + 1 else 1
            s.copy(modeType = view, curr = LI.getEmptyItem, list = s.list :+ ListItem(newId, s.curr.data))
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


    // create UI
    def table(s: List[ListItem[A]], checkedValues: List[Int], curr: ListItem[A]) = {
      <.div(
        <.table(
          <.caption(
            LI.createLabelCaption,
            <.div ( ^.float := "right",
              LI.createAddButtonToTable,
              <.button("X", ^.onClick --> delete, ^.disabled := checkedValues.length == 0)
            )
          ),
          <.thead(
            <.tr(
              <.td(<.input.checkbox(^.onChange --> onCheckAll, ^.checked := checkedValues.length == s.length && s.length>0)),
              LI.createHeaderColumnsTable
            )
          ),
          <.tbody(
            s.map(row => {
              val backgroundColor: String = if (row.id == curr.id) "silver" else "white"
              <.tr(^.onClick --> setCurrentValue(row.id), ^.backgroundColor := backgroundColor,
                <.td(<.input.checkbox(^.onChange --> onCheck(row.id), ^.checked := checkedValues.contains(row.id))),
                LI.createRow(row)
              )
            })
          )
        ),
        <.div ("Всего строк в списке: " + s.length.toString)
      )
    }

    def details(li: ListItem[A], modeType: ModeType) = {
      <.div (
        <.div( ^.float := "left",
          LI.createDetails(li, $, modeType)
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

    def render(s: StateListMode[A]) = {
      <.div(^.float := "left",
        details(s.curr, s.modeType),
        table(s.list, s.checkedValues, s.curr)
      )
    }
  }
}
