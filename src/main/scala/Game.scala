

class Game() {
  type Board = Seq[Seq[Int]]

  private var board_ : Board = Seq(
    Seq(0, 0, 0, 0, 0, 0, 0, 0),
    Seq(0, 0, 0, 0, 0, 0, 0, 0),
    Seq(0, 0, 0, 0, 0, 0, 0, 0),
    Seq(0, 0, 0, -1, 1, 0, 0, 0),
    Seq(0, 0, 0, 1, -1, 0, 0, 0),
    Seq(0, 0, 0, 0, 0, 0, 0, 0),
    Seq(0, 0, 0, 0, 0, 0, 0, 0),
    Seq(0, 0, 0, 0, 0, 0, 0, 0))

  private var turn_ : Piece = Black

  def board: Board = board_

  def turn: Piece = turn_

  def changeTurn(): Unit = turn_ = turn.reverse

  sealed trait Cell {
    def toNum: Int
  }

  sealed trait Piece extends Cell {
    def reverse: Piece

    val name: String
  }

  object Black extends Piece {
    override def toNum: Int = 1

    override def reverse: Piece = White

    override val name: String = "黒"
  }

  object White extends Piece {
    override def toNum: Int = -1

    override def reverse: Piece = Black

    override val name: String = "白"
  }

  object Empty extends Cell {
    override def toNum: Int = 0
  }

  def getCell(x: Int, y: Int): Cell = {
    board(y)(x) match {
      case 0 => Empty
      case 1 => Black
      case -1 => White
      case _ => sys.error("board state error")
    }
  }

  def setCell(x: Int, y: Int, p: Piece): Unit = board_ = board.updated(y, board(y).updated(x, p.toNum))

  def count(p: Piece): Int = {
    board.flatten.count(_ == p.toNum)
  }

  def result(): String = {
    if (count(Black) > count(White)) "黒の勝ち"
    else if (count(Black) < count(White)) "白の勝ち"
    else "引き分け"
  }

  def getFlipPos(x: Int, y: Int): Seq[(Int, Int)] = {
    (for (i <- -1 to 1; j <- -1 to 1 if !(i == 0 && j == 0)) yield getFlipPosDir(x, y, i, j, Seq.empty)).flatten
  }

  private def getFlipPosDir(x: Int, y: Int, i: Int, j: Int, result: Seq[(Int, Int)]): Seq[(Int, Int)] = {
    val (nx, ny) = (x + i, y + j)
    if (!(0 to 7 contains nx) || !(0 to 7 contains ny)) {
      Seq.empty
    } else if (getCell(nx, ny) == turn) {
      result
    } else if (getCell(nx, ny) == turn.reverse) {
      val next = result :+ (nx, ny)
      getFlipPosDir(nx, ny, i, j, next)
    } else {
      Seq.empty
    }
  }

  def getLegalMoves: Seq[(Int, Int)] =
    (for (x <- 0 to 7; y <- 0 to 7) yield (x, y)).filter { case (x, y) =>
      getCell(x, y) == Empty && getFlipPos(x, y).nonEmpty
    }

}
