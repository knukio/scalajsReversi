import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.MouseEvent

class EventHandler(render: Render, game: Game) {

  def setEvents(canvas: Canvas): Unit = {
    canvas.onclick = e => clickEvent(e)
    canvas.onmousemove = e => mouseMoveEvent(e)
  }

  def clickEvent(e: MouseEvent): Unit = {
    val (mouseX, mouseY) = getMouseAxis(e)
    render.axisToPos(mouseX, mouseY) match {
      case Some((x, y)) => clickOnBoard(x, y)
      case None =>
    }
  }

  def mouseMoveEvent(e: MouseEvent): Unit = {
    val (mouseX, mouseY) = getMouseAxis(e)
    render.axisToPos(mouseX, mouseY) match {
      case Some((x, y)) =>
        render.loop(game)
        render.effect(x, y)
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
