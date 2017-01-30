/**
  * Created by maria on 27.01.2017.
  */
package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactEventI}
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

    def nameChanged(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(
        currItem = NewItem(s.currItem.id, newVal, s.currItem.category),
        itemsCurr = s.itemsAll.diff(s.itemsInList).filter(_.name.toLowerCase().startsWith(newVal)))
      )
    }

    def render(s: NewItemModeState) =
      <.div (
        <.div(
          <.label("Наименование:", ^.float:="none"),
          <.input.text(^.onKeyUp ==> nameChanged)
        ),
        <.div(
          if (s.itemsCurr.length == 0) <.div(^.float:="left", ^.padding:="1em", ^.width:="10em", "Продукт не найден")
          else <.ul(^.float:="left", s.itemsCurr.sortWith(_.name <= _.name).map(item => <.li(item.name))),
          <.div(^.float:="left", ^.paddingTop:="0.5em",
            <.button(">>", ^.display:="block", ^.onClick --> $.modState(s => s.copy(
              currItem = NewItem(0, "", ""),
              itemsCurr = List[NewItem](),
              itemsInList = s.itemsInList ++ s.itemsCurr))),
            <.button("<<", ^.display:="block", ^.onClick --> $.modState(s => s.copy(
              currItem = NewItem(0, "", ""),
              itemsCurr =
                if (s.currItem.name.isEmpty) s.itemsAll
                else s.itemsAll.diff(s.itemsInList).filter(_.name.toLowerCase().startsWith(s.currItem.name))
                ,
              itemsInList = List[NewItem]())))
          ),
          if (s.itemsInList.length == 0) <.div(^.float:="left", ^.padding:="1em", "В списке ни одного продукта")
          else <.ul(^.float:="left", s.itemsInList.sortWith(_.name <= _.name).map(item => <.li(item.name)))
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

  val itemsInList = List(
    NewItem(0, "один-один", ""),
    NewItem(1, "два-один", ""),
    NewItem(2, "три-один", ""),
    NewItem(3, "четыре-один", ""),
    NewItem(4, "пять-один", ""),
    NewItem(5, "шесть-один", ""),
    NewItem(6, "семь-один", ""),
    NewItem(7, "восемь-один", "")
  )
  val createMode = ReactComponentB[Unit]("NewItemMode")
    .initialState(
      NewItemModeState(NewItem(0, "", ""), /*TODO: remove dummies*/ items++itemsInList, List[NewItem](), items++itemsInList))
    .renderBackend[Backend]
    .build

  val createNewItemsMode = <.div(createMode())
}
