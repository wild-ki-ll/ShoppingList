/**
  * Created by kirill on 23.01.2017.
  */
package ShoppingList

// items for modes
case class Mode (val id: Int = 0, val name: String = "")

case class Item(name: String, company: Int, unit: Int, quantity: Double, importance: Boolean)
case class Shop(id: Int, name: String, category: Int, address: String)
case class Ref(id: Int, name: String)
case class SettingItem(name: String, value: String)

// common State
case class State(modes: List[Mode], activeMode: Int)
