/**
  * Created by kirill on 23.01.2017.
  */
package ShoppingList
import ShoppingList.ShopsMode.Shop

// items for modes
case class Mode (val id: Int = 0, val name: String = "")

case class Item(
  name: String = "",
  category: Int = 0,
  subcategory: Int = 0,
  quantity: Double = 0,
  unit: Int = 0,
  importance: Boolean = false,
  mark: String = "",
  comment: String = "",
  img: String = "",
  shops: List[Shop] = List[Shop]()
)
case class Ref(id: Int, name: String)
case class SettingItem(name: String, value: String)

// common State
case class State(modes: List[Mode], activeMode: Int)
