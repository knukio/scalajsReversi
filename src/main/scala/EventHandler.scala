import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.MouseEvent
import rx._

class EventHandler(render: Render, game: Game) {
  implicit val ctx: Ctx.Owner = Ctx.Owner.safe()

  private val mouseEvent = Var[MouseEvent](null)

  private val pos = Rx {
    val mouseAxis = getMouseAxis(mouseEvent())
    render.axisToPos(mouseAxis._1, mouseAxis._2)
  }

  def setEvents(canvas: Canvas): Unit = {
    canvas.onclick = e => eventTemplate(e, clickOnBoard)
    canvas.onmousemove = e => eventTemplate(e, {
      render.loop(game)
      render.effect
    })
  }

  def eventTemplate(e: MouseEvent, f: (Int, Int) => Unit): Unit = {
    mouseEvent() = e
    pos.now match {
      case Some((x, y)) => f(x, y)
      case None =>
    }
  }

  private def clickOnBoard(x: Int, y: Int): Unit = {
    val flipPos = game.getFlipPos(x, y)
    if (game.getCell(x, y) == game.Empty && flipPos.nonEmpty) {
      putCells(flipPos :+ (x, y))
      game.changeTurn()
      if (game.getLegalMoves.nonEmpty) {
        render.clearMessage()
        render.commonMessage(game)
      } else {
        game.changeTurn()
        if (game.getLegalMoves.nonEmpty) skipTurn() else endGame()
      }
      render.loop(game)
    }
  }

  private def putCells(cells: Seq[(Int, Int)]) = {
    for ((x, y) <- cells) {
      game.setCell(x, y, game.turn)
    }
  }

  private def endGame() = {
    render.showMessage(s"${game.result()}です")
    render.appendState(game)
  }

  private def skipTurn() = {
    render.showMessage(s"${game.turn.reverse.name}は置けるところがありません")
    render.commonMessage(game)
  }

  private def getMouseAxis(e: MouseEvent): (Double, Double) = {
    val rect = e.srcElement.getBoundingClientRect()
    (e.clientX - rect.left, e.clientY - rect.top)
  }

}
