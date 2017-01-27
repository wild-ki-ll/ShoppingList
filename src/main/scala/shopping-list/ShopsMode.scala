/**
  * Created by kirill on 24.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^._
import ShoppingList.ListMode._
import ShoppingList.ModeType.ModeType

object ShopsMode {

  def changeValue(oldValue: Any, newValue: Any, modeType: ModeType): Any = if (modeType == ModeType.view) oldValue else newValue

  def onChangeName($: BackendScope[Unit, StateListMode[Shop]], modeType: ModeType)(e: ReactEventI) = {
    val newVal = e.target.value
    $.modState(s => s.copy(curr = ListItem(s.curr.id, Shop(changeValue(s.curr.data.name, newVal, modeType).toString, s.curr.data.category, s.curr.data.address))))
  }

  def onChangeCategory($: BackendScope[Unit, StateListMode[Shop]], modeType: ModeType)(e: ReactEventI) = {
    val newVal = e.target.value
    $.modState(s => s.copy(curr = ListItem(s.curr.id, Shop(s.curr.data.name, changeValue(s.curr.data.category, newVal, modeType).toString, s.curr.data.address))))
  }

  def onChangeAddress($: BackendScope[Unit, StateListMode[Shop]], modeType: ModeType)(e: ReactEventI) = {
    val newVal = e.target.value
    $.modState(s => s.copy(curr = ListItem(s.curr.id, Shop(s.curr.data.name, s.curr.data.category, changeValue(s.curr.data.name, newVal, modeType).toString))))
  }

  case class Shop(name: String = "", category: String = "", address: String = "")
  object Shop {
    implicit val ShopListItemLike: ListItemLike[Shop] = new ListItemLike[Shop] {
      def getEmptyItem: ListItem[Shop] = ListItem[Shop](0, Shop())
      def createRow(row: ListItem[Shop]) =
        List(
          <.td(row.data.name),
          <.td(row.data.category),
          <.td(row.data.address)
        )

      def createAddButtonToTable = List(<.button("Ф"))

      def createDetails(sh: ListItem[Shop], $: BackendScope[Unit, StateListMode[Shop]], modeType: ModeType) =
        <.div(
          <.div(<.label("Название"), <.input(
            ^.`type` := "text",
            ^.value := sh.data.name ,
            ^.onChange ==> onChangeName($, modeType))),
          <.div(<.label("Категория"), <.select(
            <.option("Продуктовый"),
            <.option("Продовольственный"),
            <.option("Прочее"),
            ^.value := sh.data.category,
            ^.onChange ==> onChangeCategory($, modeType))),
          <.div(<.label("Адрес"), <.input(
            ^.`type` := "text",
            ^.value := sh.data.address,
            ^.onChange ==> onChangeAddress($, modeType)))
        )
      def createHeaderColumnsTable =
        List(
          <.td("Название"),
          <.td("Категория"),
          <.td("Адрес"))

      def createLabelCaption = <.label("Список магазинов", ^.float := "left")
    }
  }

  val createMode = ReactComponentB[Unit]("ShopsMode")
    .initialState(StateListMode[Shop](ModeType.view, ListItem(0, Shop()), List(), List()))
    .renderBackend[BackendList[Shop]]
    .build

  val createShopsMode = <.div(createMode())
}
