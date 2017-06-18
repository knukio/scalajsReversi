import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import org.scalajs.dom.html.{Canvas, Div}

import scalatags.Text.all._

object Main extends JSApp {
  override def main(): Unit = {
    document.body.innerHTML = content
    val canvas = document.getElementById("canvas").asInstanceOf[Canvas]
    val message = document.getElementById("message").asInstanceOf[Div]
    val render = new Render(canvas, message)
    val game = new Game()
    val handler = new EventHandler(render, game)
    handler.setEvents(canvas)
    render.commonMessage(game)
    render.loop(game)
  }

  val content: String = {
    div(
      canvas(id := "canvas", widthA := 450, heightA := 450),
      p(id := "message", width := 400, height := 100, padding := 10, border := "solid 2px")
    ).render
  }
}
