package ShoppingList

import japgolly.scalajs.react.Callback

/**
  * Created by kirill on 23.01.2017.
  */

// service class
case class Menu(menuItems: List[String])

// items for modes
case class Item(name: String, company: Int, unit: Int, quantity: Double, importance: Boolean)
case class Shop(name: String, category: Int, address: String)
case class Ref(id: Int, name: String)
case class SettingItem(name: String, value: String)

// modes
case class Items(menu: Menu, itemsList: List[Item])
case class Shops(menu: Menu, shopsList: List[Shop])
case class References(menu: Menu, refsList: List[Ref])
case class Settings(menu: Menu, settingList: List[SettingItem])

// common State
case class State(mainMenu: Menu, items: Items, shops: Shops, refs: References, settings: Settings, txt: String)
