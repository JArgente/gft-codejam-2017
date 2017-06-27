import scala.util.Random
import Constants._

import scala.annotation.tailrec

/**
  * Created by jeae on 15/06/2017.
  */
  sealed trait GenExp{
    def parseToCode():String
    def getSubtree(terminal: Boolean):GenExp
    def getSubtreeDepth(depth: Int, actualDepth: Int):GenExp
  }
  case class CondExpress(boolExp: Boolexp, exp1: GenExp, exp2:GenExp) extends GenExp {
    override def parseToCode(): String = "if("+boolExp.parseToCode()+"){"+exp1.parseToCode()+"}else{"+exp2.parseToCode()+"};"

    override def getSubtree(terminal: Boolean): GenExp = {
      val subTree=if(Math.random()<=0.5) exp1 else exp2
      if(terminal)
        subTree.getSubtree(true)
      else
        subTree.getSubtreeDepth(Random.nextInt(MAX_DEPTH_CHROMOS), 1)
    }

    override def getSubtreeDepth(depth: Int, actualDepth: Int): GenExp = {
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) exp1 else exp2).getSubtreeDepth(depth, actualDepth+1)

    }
  }
  case class Action(nameAction: String, parameter: NumExp) extends GenExp {
    override def parseToCode(): String = nameAction + "(" + parameter.parseToCode() + ");"

    override def getSubtree(terminal: Boolean): GenExp =
      if (terminal)
        new Action(nameAction, parameter.getTerminal())
      else
        new Action(nameAction, parameter.getSubtreeDepth(Random.nextInt(MAX_DEPTH_CHROMOS), 1))

    override def getSubtreeDepth(depth: Int, actualDepth: Int): GenExp =
      if (depth == actualDepth)
        this
      else
        new Action(nameAction, parameter.getSubtreeDepth(depth, actualDepth + 1))
  }

    case class Null() extends GenExp {
      override def parseToCode(): String = ";"

      override def getSubtree(terminal: Boolean): GenExp =this

      override def getSubtreeDepth(depth: Int, actualDepth: Int): GenExp =this
  }

  //----------------------------------------------------------------------------------------

  sealed trait NumExp {
    def parseToCode(): String
    def getTerminal():NumExp
    def getSubtreeDepth(depth: Int, actualDepth: Int):NumExp
  }

  case class Constant(n: Double) extends NumExp{
    override def parseToCode(): String = n.toString

    override def getTerminal(): NumExp = this

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp = this
  }
  case class Add(n1:NumExp, n2:NumExp) extends NumExp{
    override def parseToCode(): String = "("+n1.parseToCode()+"+"+n2.parseToCode()+")"

    override def getTerminal(): NumExp =
      (if(Math.random()<=0.5) n1 else n2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) n1 else n2).getSubtreeDepth(depth, actualDepth+1)
  }
  case class Sub(n1:NumExp, n2:NumExp) extends NumExp{
    override def parseToCode(): String = "("+n1.parseToCode()+"-"+n2.parseToCode()+")"

    override def getTerminal(): NumExp =
      (if(Math.random()<=0.5) n1 else n2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) n1 else n2).getSubtreeDepth(depth, actualDepth+1)
  }
  case class Mult(n1:NumExp, n2:NumExp) extends NumExp{
    override def parseToCode(): String = "("+n1.parseToCode()+"*"+n2.parseToCode()+")"

    override def getTerminal(): NumExp =
      (if(Math.random()<=0.5) n1 else n2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) n1 else n2).getSubtreeDepth(depth, actualDepth+1)
  }
  case class Max(n1:NumExp, n2:NumExp) extends NumExp{
    override def parseToCode(): String = "Math.max("+n1.parseToCode()+","+n2.parseToCode()+")"
    override def getTerminal(): NumExp =
      (if(Math.random()<=0.5) n1 else n2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) n1 else n2).getSubtreeDepth(depth, actualDepth+1)

  }
  case class Min(n1:NumExp, n2:NumExp) extends NumExp{
    override def parseToCode(): String = "Math.min("+n1.parseToCode()+","+n2.parseToCode()+")"

    override def getTerminal(): NumExp =
      (if(Math.random()<=0.5) n1 else n2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) n1 else n2).getSubtreeDepth(depth, actualDepth+1)
  }
  case class NumScan(nombre:String) extends NumExp{
    override def parseToCode(): String = nombre+"()"

    override def getTerminal(): NumExp = this

    override def getSubtreeDepth(depth: Int, actualDepth: Int): NumExp = this
  }


  //-----------------------------------------------------------------------------------------------

  sealed trait Boolexp {
    def parseToCode(): String
    def getTerminal():Boolexp
    def getSubtreeDepth(depth: Int, actualDepth: Int):Boolexp
  }

  case class And(b1: Boolexp, b2: Boolexp)extends Boolexp{
    override def parseToCode(): String = b1.parseToCode()+" && "+b2.parseToCode()

    override def getTerminal(): Boolexp =
      (if(Math.random()<=0.5) b1 else b2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) b1 else b2).getSubtreeDepth(depth, actualDepth+1)
  }
  case class Or(b1: Boolexp, b2: Boolexp)extends Boolexp{
    override def parseToCode(): String = b1.parseToCode()+" || "+b2.parseToCode()

    override def getTerminal(): Boolexp =
      (if(Math.random()<=0.5) b1 else b2).getTerminal()

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
        (if(Math.random()<=0.5) b1 else b2).getSubtreeDepth(depth, actualDepth+1)
  }

  case class Gt(n1: NumExp, n2: NumExp)extends Boolexp{
    override def parseToCode(): String = n1.parseToCode()+" > "+n2.parseToCode()

    override def getTerminal(): Boolexp =
        new Gt(n1.getTerminal(), n2.getTerminal())

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
        if(Math.random()<=0.5)
          new Gt(n1.getSubtreeDepth(depth, actualDepth+1), n2)
        else
          new Gt(n1, n2.getSubtreeDepth(depth, actualDepth+1))
  }
  case class Lt(n1: NumExp, n2: NumExp)extends Boolexp{
    override def parseToCode(): String = n1.parseToCode()+" < "+n2.parseToCode()

    override def getTerminal(): Boolexp =
      new Lt(n1.getTerminal(), n2.getTerminal())

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
      if(Math.random()<=0.5)
        new Lt(n1.getSubtreeDepth(depth, actualDepth+1), n2)
      else
        new Lt(n1, n2.getSubtreeDepth(depth, actualDepth+1))
  }
  case class Gte(n1: NumExp, n2: NumExp)extends Boolexp{
    override def parseToCode(): String = n1.parseToCode()+" >= "+n2.parseToCode()

    override def getTerminal(): Boolexp =
      new Gte(n1.getTerminal(), n2.getTerminal())

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
      if(Math.random()<=0.5)
        new Gte(n1.getSubtreeDepth(depth, actualDepth+1), n2)
      else
        new Gte(n1, n2.getSubtreeDepth(depth, actualDepth+1))
  }
  case class Lte(n1: NumExp, n2: NumExp)extends Boolexp{
    override def parseToCode(): String = n1.parseToCode()+" <= "+n2.parseToCode()

    override def getTerminal(): Boolexp =
      new Lte(n1.getTerminal(), n2.getTerminal())

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
      if(Math.random()<=0.5)
        new Lte(n1.getSubtreeDepth(depth, actualDepth+1), n2)
      else
        new Lte(n1, n2.getSubtreeDepth(depth, actualDepth+1))
  }
  case class Eq(n1: NumExp, n2: NumExp)extends Boolexp{
    override def parseToCode(): String = n1.parseToCode()+" == "+n2.parseToCode()

    override def getTerminal(): Boolexp =
      new Eq(n1.getTerminal(), n2.getTerminal())

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
      if(Math.random()<=0.5)
        new Eq(n1.getSubtreeDepth(depth, actualDepth+1), n2)
      else
        new Eq(n1, n2.getSubtreeDepth(depth, actualDepth+1))
  }
  case class Ne(n1: NumExp, n2: NumExp)extends Boolexp{
    override def parseToCode(): String = n1.parseToCode()+" != "+n2.parseToCode()

    override def getTerminal(): Boolexp =
      new Ne(n1.getTerminal(), n2.getTerminal())

    override def getSubtreeDepth(depth: Int, actualDepth: Int): Boolexp =
      if(depth==actualDepth)
        this
      else
      if(Math.random()<=0.5)
        new Ne(n1.getSubtreeDepth(depth, actualDepth+1), n2)
      else
        new Ne(n1, n2.getSubtreeDepth(depth, actualDepth+1))
  }

object Functions {
  def getRandomGenExp(maxDepth: Int):GenExp={
    maxDepth match {
      case MAX_DEPTH_CHROMOS =>
        if(Math.random()<=0.3)
          new Null
        else {
          val size: Int = listaAcciones.length
          new Action(listaAcciones(Random.nextInt(size)), getRandomNumExp(maxDepth))
        }
      case n =>
        if (Math.random() > PROB_ACTION)
          new CondExpress(getRandomBoolExp(maxDepth + 1), getRandomGenExp(maxDepth + 1), getRandomGenExp(maxDepth + 1))
        else
          getRandomGenExp(MAX_DEPTH_CHROMOS)
    }
  }

  def getRandomBoolExp(maxDepth: Int):Boolexp={
    maxDepth match {
      case MAX_DEPTH_CHROMOS =>
        Random.nextInt(6) match {
          case 0 => new Gt(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 1 => new Lt(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 2 => new Gte(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 3 => new Lte(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 4 => new Eq(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 5 => new Ne(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
        }
      case n => if(Math.random()<PROB_TERMINAL)
        getRandomBoolExp(MAX_DEPTH_CHROMOS)
      else
        Random.nextInt(8) match {
          case 0 => new And(getRandomBoolExp(MAX_DEPTH_CHROMOS), getRandomBoolExp(MAX_DEPTH_CHROMOS))
          case 1 => new Or(getRandomBoolExp(MAX_DEPTH_CHROMOS), getRandomBoolExp(MAX_DEPTH_CHROMOS))
          case 2 => new Gt(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 3 => new Lt(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 4 => new Gte(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 5 => new Lte(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 6 => new Eq(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))
          case 7 => new Ne(getRandomNumExp(MAX_DEPTH_CHROMOS), getRandomNumExp(MAX_DEPTH_CHROMOS))

        }

    }
  }

  def getRandomNumExp(maxDepth: Int):NumExp={
    maxDepth match {
      case MAX_DEPTH_CHROMOS =>
        if(Random.nextInt(2)>1)new Constant(Random.nextDouble())
        else new NumScan(listaScanNum(Random.nextInt(listaScanNum.length)))
      case n => if(Math.random()<PROB_TERMINAL)
        getRandomNumExp(MAX_DEPTH_CHROMOS)
      else
        Random.nextInt(5) match {
          case 0 => new Add(getRandomNumExp(maxDepth+1), getRandomNumExp(maxDepth+1))
          case 1 => new Sub(getRandomNumExp(maxDepth+1), getRandomNumExp(maxDepth+1))
          case 2 => new Mult(getRandomNumExp(maxDepth+1), getRandomNumExp(maxDepth+1))
          case 3 => new Max(getRandomNumExp(maxDepth+1), getRandomNumExp(maxDepth+1))
          case 4 => new Min(getRandomNumExp(maxDepth+1), getRandomNumExp(maxDepth+1))
        }

    }
  }
}
