package ShoppingList

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document

object SLApp extends JSApp {

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }
                                            
  def main(): Unit = {
    appendPar(document.body, "Hello World its Shopping List on scalajs-react!")
  }
}