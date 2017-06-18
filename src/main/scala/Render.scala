import org.scalajs.dom.html.{Canvas, Div, Document}
import org.scalajs.dom.raw.CanvasRenderingContext2D

class Render(canvas: Canvas, message: Div) {
  type Ctx = CanvasRenderingContext2D
  private val boardSize = 402
  private val cellWidth = (boardSize - 2) / 8
  private val cellCenter = cellWidth / 2
  private val ctx: Ctx = canvas.getContext("2d").asInstanceOf[Ctx]

  def loop(game: Game): Unit = {
    ctx.clearRect(0, 0, boardSize, boardSize)
    initBoard()
    cells()
    pieces(game)
    legalMoves(game)
  }

  def commonMessage(game: Game): Unit = {
    appendTurn(game)
    appendState(game)
  }

  def clearMessage(): Unit = {
    message.innerHTML = ""
  }

  def showMessage(s: String): Unit = {
    message.innerHTML = s + "<BR>"
  }

  def appendMessage(s: String): Unit = {
    message.innerHTML += s + "<BR>"
  }

  def appendState(game: Game): Unit = appendMessage(s"黒: ${game.count(game.Black)}  白: ${game.count(game.White)}")

  def appendTurn(game: Game): Unit = appendMessage(s"${game.turn.name}の手番です")

  def axisToPos(mouseX: Double, mouseY: Double): Option[(Int, Int)] = {
    (for (x <- 0 to 7; y <- 0 to 7) yield (x, y)).find { case (x, y) =>
      val (xAxis, yAxis) = getBaseAxis(x, y)
      yAxis < mouseY && mouseY < yAxis + cellWidth && xAxis < mouseX && mouseX < xAxis + cellWidth
    }
  }

  def effect(x: Int, y: Int): Unit = {
    val (xAxis, yAxis) = getBaseAxis(x, y)
    ctx.globalAlpha = 0.5
    ctx.fillStyle = Color.white
    ctx.fillRect(xAxis, yAxis, cellWidth, cellWidth)
  }

  private def initBoard(): Unit = {
    ctx.globalAlpha = 1.0
    ctx.fillStyle = Color.darkGreen
    ctx.fillRect(0, 0, boardSize, boardSize)
  }

  private def cells(): Unit = {
    ctx.strokeStyle = Color.white
    for (x <- 0 to 7; y <- 0 to 7) {
      val (xAxis, yAxis) = getBaseAxis(x, y)
      ctx.strokeRect(xAxis + 1, yAxis + 1, cellWidth, cellWidth)
    }
  }

  private def pieces(game: Game): Unit = {
    for (x <- 0 to 7; y <- 0 to 7) {
      val (xAxis, yAxis) = getBaseAxis(x, y)
      game.getCell(x, y) match {
        case game.Black => blackPiece(xAxis, yAxis)
        case game.White => whitePiece(xAxis, yAxis)
        case game.Empty =>
      }
    }
  }

  private def whitePiece(x: Int, y: Int): Unit = {
    fillArc(x + cellCenter + 1, y + cellCenter + 1, cellCenter - 3, Color.black)
    fillArc(x + cellCenter - 1, y + cellCenter - 1, cellCenter - 3, Color.white)
  }

  private def blackPiece(x: Int, y: Int): Unit = {
    fillArc(x + cellCenter + 1, y + cellCenter + 1, cellCenter - 3, Color.white)
    fillArc(x + cellCenter - 1, y + cellCenter - 1, cellCenter - 3, Color.black)
  }

  private def getBaseAxis(x: Int, y: Int): (Int, Int) = (x * cellWidth, y * cellWidth)

  private def fillArc(x: Int, y: Int, r: Int, color: String) = {
    ctx.globalAlpha = 1.0
    ctx.beginPath()
    ctx.arc(x, y, r, 0, 2 * Math.PI, false)
    ctx.fillStyle = color
    ctx.fill()
  }

  private def legalMoves(game: Game): Unit = {
    for ((x, y) <- game.getLegalMoves) {
      val (xAxis, yAxis) = getBaseAxis(x, y)
      legalMove(xAxis, yAxis)
    }
  }

  private def legalMove(x: Int, y: Int): Unit = {
    ctx.globalAlpha = 1.0
    ctx.fillStyle = Color.blue
    ctx.fillRect(x + 20, y + 20, 10, 10)
  }
}
