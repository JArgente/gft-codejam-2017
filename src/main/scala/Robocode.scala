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

  def runBatchWithSamples(bots: List[Bot], samples: List[String], rounds: Int): List[(Bot, Double)] =
    bots.map(b=>(b,samples.map(s=>battle(b.fileName,s, rounds)).sum.toDouble/samples.length))


  def battle(bot: String, oponent: String, rounds: Int)={
    val selectedBots = engine.getLocalRepository(bot + ", " + oponent)
    val battleSpec = new BattleSpecification(rounds, battlefield, selectedBots)
    engine.runBattle(battleSpec, true)
    val results=battleObserver.getResults
    val indexBot=if (results(0).getTeamLeaderName == bot) 0 else 1
    val indexOp= 1-indexBot
    val botScore = results(indexBot).getScore
    val totalScore = botScore + results(indexOp).getScore
    (botScore + BATTLE_HANDICAP) / (totalScore + BATTLE_HANDICAP)
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
