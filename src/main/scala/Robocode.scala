import java.io.File

import robocode.BattleResults
import robocode.control._
import robocode.control.events._
import Constants._

/**
  * Created by jeae on 19/06/2017.
  */
case class Robocode() {
  var engine = new RobocodeEngine(new File("C:/Robocode"))
  var battleObserver = new MyBattleAdaptor()
  engine.addBattleListener(battleObserver)
  engine.setVisible(false)
  var battlefield = new BattlefieldSpecification(800, 600)

  def runBatchWithSamples(bots: List[Bot], samples: List[String], rounds: Int): List[(Bot, (Double, Double, Double))] ={
    val res:List[(Bot,(Int, Int, Int))]=bots.map(b=>(b,samples.map(s=>battle(b.fileName,s, rounds)).reduce((a,b)=>(a._1+b._1, a._2+b._2, a._3+b._3))))
    res.map(b=>(b._1,(b._2._1.toDouble/samples.length, b._2._2.toDouble/samples.length, b._2._3.toDouble/samples.length)))
  }

  def battle(bot: String, oponent: String, rounds: Int)={
    val selectedBots = engine.getLocalRepository(bot + ", " + oponent)
    val battleSpec = new BattleSpecification(rounds, battlefield, selectedBots)
    engine.runBattle(battleSpec, true)
    val results=battleObserver.getResults
    val indexBot=if (results(0).getTeamLeaderName == bot) 0 else 1
    val indexOp= 1-indexBot
    val botScore = results(indexBot).getScore
    val totalScore = botScore + results(indexOp).getScore
    val finalScore= botScore / totalScore
    val botNormalScore = (results(indexBot).getLastSurvivorBonus+results(indexBot).getSurvival)/2+100*finalScore
    val botScanScore = (results(indexBot).getBulletDamage+results(indexBot).getBulletDamageBonus)/2+100*finalScore
    (botNormalScore, botScanScore, finalScore)
  }
}

case class MyBattleAdaptor() extends BattleAdaptor{
  var results: Array[BattleResults] = null

  override def onBattleCompleted(e: BattleCompletedEvent): Unit = {
    results = e.getIndexedResults
  }

  override def onBattleError(e: BattleErrorEvent): Unit = {
    System.out.println("Error running battle: " + e.getError)
  }

  def getResults: Array[BattleResults] = results
}
