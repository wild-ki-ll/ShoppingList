/**
  * Created by kirill on 23.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB}
import japgolly.scalajs.react.vdom.prefix_<^.{<, ^, _}

object ItemsMode {

  case class ItemsModeState(
    idSubmode: Int
  )

  val customStyle = "customStyle".reactStyle

  class ItemBackend($: BackendScope[Unit, Item]) {
    def render(i: Item) =
      <.div(
        <.div(
          <.label("Наименование"),
          <.input(^.`type` := "text"),
          <.select(
            <.option("Продукты питания"),
            <.option("Бытовая химия"),
            <.option("Канцелярия"),
            <.option("Парфюмерия и косметика"),
            <.option("Книги"),
            <.option("Прочее")
          ),
          <.select(
            <.option("Молочные продукты"),
            <.option("Крупы"),
            <.option("Хлебо-булочные изделия"),
            <.option("Мясо"),
            <.option("Полуфабрикаты"),
            <.option("Прочее")
          )
        ),
        <.div(
          <.label("Количество"),
          <.input(^.`type` := "number"),
          <.select(
            <.option("шт"),
            <.option("кг"),
            <.option("л"),
            <.option("г")
          ),
          <.label(^.float := "none", <.input.checkbox(), "Важное!")
        )
      )
  }

  val сreateItemForm = ReactComponentB[Unit]("ItemSubmode")
    .initialState(Item(name = "", company = 0, unit = 0, quantity = 0, importance = false))
    .renderBackend[ItemBackend]
    .build

  val createItemSubmode = <.div(сreateItemForm())

  class Backend($: BackendScope[Unit, ItemsModeState]) {


    def createMenu =
      <.menu(
        <.button("Добавить",      ^.onClick --> $.modState(_.copy(idSubmode = 1))),
        <.button("Редактировать", ^.onClick --> $.modState(_.copy(idSubmode = 1))),
        <.button("Удалить",       ^.onClick --> $.modState(_.copy(idSubmode = 0))),
        <.button("Фильтр",        ^.onClick --> $.modState(_.copy(idSubmode = 0)))
      )

    def render(s: ItemsModeState) =
      <.div (
        createMenu(),
        if (s.idSubmode == 1) createItemSubmode() else <.div ("Режим покупок")
      )
  }

  val createMode = ReactComponentB[Unit]("ItemMode")
    .initialState(ItemsModeState(idSubmode = 0))
    .renderBackend[Backend]
    .build

  val createItemsMode = <.div(createMode())
}
