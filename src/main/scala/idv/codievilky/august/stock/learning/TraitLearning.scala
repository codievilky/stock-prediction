package idv.codievilky.august.stock.learning

/**
 * @auther Codievilky August
 * @since 2020/9/11
 */
class TraitLearning extends AbstractLogged {
  println(2)

  def withD(abc: String): Unit = {
    log(abc)
  }

  def withX(abc:String) = {
    println(s"x$abc")
  }

  override def log(msg: String): Unit = {
    super.log(s"t$msg")
  }
}

trait ConsoledLogged extends Logged {
  this: TraitLearning =>

  println(4)

  val august = 10

  abstract override def log(msg: String): Unit = {
    withX(msg)
    println(super.log("b" + msg))
  }
}

trait AbstractLogged extends Logged {
  println(3)

  def log(msg: String): Unit = {
    println(s"a$msg")
  }
}


trait Logged {
  println(1)

  def log(msg: String): Unit
}

object TraitLearning extends App {
  val abc = new TraitLearning  with ConsoledLogged with AbstractLogged

  abc.withD("0913")
}


