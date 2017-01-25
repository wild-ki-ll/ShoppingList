/**
  * Created by kirill on 23.01.2017.
  */

package ShoppingList

import japgolly.scalajs.react.{BackendScope, Callback, ReactComponentB, ReactEventI}
import japgolly.scalajs.react.vdom.prefix_<^.{<, ^, _}

object ItemsMode {

  // состояние режима "Покупки"
  case class ItemsModeState(
    showItemForm: Boolean = false,        // показывать форму товара
    currentItem: Item     = Item(),       // текущий товар
    list: List[Item]      = List[Item]()  // список товаров
  )

  class Backend($: BackendScope[Unit, ItemsModeState]) {

    // Обработчики
    def showItemForm = $.modState(_.copy(showItemForm = true))
    def hideItemForm = $.modState(_.copy(showItemForm = false))
    def addItem = $.modState(s => s.copy(showItemForm = false, list = s.list :+ s.currentItem, currentItem = Item()))

    def onChangeName(e: ReactEventI) = {
      val newVal = e.target.value
      $.modState(s => s.copy(
        currentItem = Item(newVal, s.currentItem.category, s.currentItem.subcategory,
          s.currentItem.quantity, s.currentItem.unit, s.currentItem.importance,
          s.currentItem.mark, s.currentItem.comment, s.currentItem.img, s.currentItem.shops
        )))
    }
    def onChangeCategory(e: ReactEventI) = {
      println(e.target)
      val newVal = e.target.value.toInt
      $.modState(s => s.copy(
        currentItem = Item(s.currentItem.name, newVal, s.currentItem.subcategory,
          s.currentItem.quantity, s.currentItem.unit, s.currentItem.importance,
          s.currentItem.mark, s.currentItem.comment, s.currentItem.img, s.currentItem.shops
        )))
    }
    def onChangeSubcategory(e: ReactEventI) = {
      val newVal = e.target.value.toInt
      $.modState(s => s.copy(
        currentItem = Item(s.currentItem.name, s.currentItem.category, newVal,
          s.currentItem.quantity, s.currentItem.unit, s.currentItem.importance,
          s.currentItem.mark, s.currentItem.comment, s.currentItem.img, s.currentItem.shops
        )))
    }
    def onChangeQuantity(e: ReactEventI) = {
      val newVal = e.target.value.toDouble
      $.modState(s => s.copy(
        currentItem = Item(s.currentItem.name, s.currentItem.category, s.currentItem.subcategory,
          newVal, s.currentItem.unit, s.currentItem.importance,
          s.currentItem.mark, s.currentItem.comment, s.currentItem.img, s.currentItem.shops
        )))
    }
    def onChangeUnit(e: ReactEventI) = {
      val newVal = e.target.value.toInt
      $.modState(s => s.copy(
        currentItem = Item(s.currentItem.name, s.currentItem.category, s.currentItem.subcategory,
          s.currentItem.quantity, newVal, s.currentItem.importance,
          s.currentItem.mark, s.currentItem.comment, s.currentItem.img, s.currentItem.shops
        )))
    }

    def onChangeImportance(e: ReactEventI) = {
      val newVal = e.target.checked
      $.modState(s => s.copy(
        currentItem = Item(s.currentItem.name, s.currentItem.category, s.currentItem.subcategory,
          s.currentItem.quantity, s.currentItem.unit, newVal,
          s.currentItem.mark, s.currentItem.comment, s.currentItem.img, s.currentItem.shops
        )))
    }

    def print = println($.toString)

    // Форма товара
    def createItemForm(i: Item) =
      <.div(
        <.div(
          <.label("Наименование"),
          <.input(^.`type` := "text", ^.value := i.name, ^.onChange ==> onChangeName),
          <.select(
            <.option("Продукты питания", ^.value := "0"),
            <.option("Бытовая химия", ^.value := "1"),
            <.option("Канцелярия", ^.value := "2"),
            <.option("Парфюмерия и косметика", ^.value := "3"),
            <.option("Книги", ^.value := "4"),
            <.option("Прочее", ^.value := "5"),
            ^.selected := i.category,
            ^.onChange ==> onChangeCategory
          ),
          <.select(
            <.option("Молочные продукты", ^.value := "0"),
            <.option("Крупы", ^.value := "1"),
            <.option("Хлебо-булочные изделия", ^.value := "2"),
            <.option("Мясо", ^.value := "3"),
            <.option("Полуфабрикаты", ^.value := "4"),
            <.option("Прочее", ^.value := "5"),
            ^.value := i.subcategory,
            ^.onChange ==> onChangeSubcategory
          )
        ),
        <.div(
          <.label("Количество"),
          <.input(^.`type` := "number", ^.value := i.quantity, ^.onChange ==> onChangeQuantity),
          <.select(
            <.option("шт", ^.value := "0"),
            <.option("кг", ^.value := "1"),
            <.option("л",  ^.value := "2"),
            <.option("г", ^.value := "3"),
            ^.value := i.unit,
            ^.onChange ==> onChangeUnit
          ),
          <.label(^.float := "none", <.input.checkbox(^.checked := i.importance, ^.onChange ==> onChangeImportance), "Важное!")
        ),
        <.button("Сохранить", ^.onClick --> addItem)
      )

    // Список товаров
    def createTable(s: List[Item]) = {
      <.div(
        if (s.length > 0)
          <.table(
            <.caption("Спиок товаров"),
            <.thead(
              <.tr(
                <.td("Название"),
                <.td("Категория"),
                <.td("Подкатегория"),
                <.td("Количество"),
                <.td("Ед.изм"),
                <.td("Важность")
              )
            ),
            <.tbody(
              s.map(item => {
                <.tr(
                  <.td(item.name),
                  <.td(item.category),
                  <.td(item.subcategory),
                  <.td(item.quantity),
                  <.td(item.unit),
                  <.td(item.importance.toString)
                )
              })
            )
          )
        else <.div(),
          <.div ("Всего строк в списке: " + s.length.toString)
        )
    }

    // Меню
    def createMenu =
      <.menu(
        <.button("Добавить",      ^.onClick --> showItemForm),
        <.button("Редактировать", ^.onClick --> showItemForm),
        <.button("Удалить",       ^.onClick --> hideItemForm),
        <.button("Фильтр",        ^.onClick --> hideItemForm)
      )

    def render(s: ItemsModeState) =
      <.div (
        createMenu(),
        if (s.showItemForm) createItemForm(s.currentItem) else createTable(s.list)
      )
  }

  val createMode = ReactComponentB[Unit]("ItemMode")
    .initialState(ItemsModeState())
    .renderBackend[Backend]
    .build

  val createItemsMode = <.div(createMode())
}
