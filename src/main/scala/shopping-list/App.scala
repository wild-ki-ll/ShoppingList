package ShoppingList

import japgolly.scalajs.react.{Callback,ReactDOM}

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import japgolly.scalajs.react.vdom.prefix_<^._
import ShoppingList.Menu.MainMenu



object App extends JSApp {

  type State = Vector[(String, Callback)]

  def Document =
    <.div (
      MainMenu()
    )

  def main(): Unit = {
    ReactDOM.render(Document(), document.getElementById("app"))
  }
}

