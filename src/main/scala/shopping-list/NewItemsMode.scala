/**
  * Created by maria on 27.01.2017.
  */
package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^._
import ModeType._
import ShoppingList.ItemsMode.ItemsModeState

object NewItemsMode {
  case class NewItem (id: Int, name: String, category: String)

  case class NewItemModeState(
    currItem:     NewItem,
    itemsCurr:    List[NewItem],
    itemsInList:  List[NewItem],
    itemsAll:     List[NewItem]
  )

  class Backend($: BackendScope[Unit, NewItemModeState]) {
    def render(s: NewItemModeState) =
      <.div (
        <.div(
          <.label("Наименование:", ^.float:="none"),
          <.input.text()
        ),
        <.div(
          <.ul(
            s.itemsCurr.map(item => <.li(item.name))
          )
        )
      )
  }

  val items = List(
    NewItem(0, "один", ""),
    NewItem(1, "два", ""),
    NewItem(2, "три", ""),
    NewItem(3, "четыре", ""),
    NewItem(4, "пять", ""),
    NewItem(5, "шесть", ""),
    NewItem(6, "семь", ""),
    NewItem(7, "восемь", "")
  )
  val createMode = ReactComponentB[Unit]("NewItemMode")
    .initialState(NewItemModeState(NewItem(0, "", ""), items, List[NewItem](), List[NewItem]()))
    .renderBackend[Backend]
    .build

  val createNewItemsMode = <.div(createMode())
}
