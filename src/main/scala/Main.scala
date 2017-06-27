import java.io.File

import robocode.control.{BattleSpecification, RobocodeEngine}
import Constants._

import scala.annotation.tailrec
import scala.util.Random
/**
  * Created by jeae on 14/06/2017.
  */
object Main extends App{
  val bestOfBests=MainEx.generationLoop(0, List.empty, POP_SIZE, new Robocode(), (null, -1.0))
  System.out.println("AMONG "+ POP_SIZE+" bots in each generation, after "+MAX_GENS+" generations the best bot is: "+bestOfBests.botName)
  bestOfBests.compile(bestOfBests.construct())
}

object MainEx {

  @tailrec
  final def generationLoop(numGens: Int, genAnt: List[Bot], popSize: Int, roboCodeEngine: Robocode, bestAllTime: (Bot, Double)): Bot = {
    numGens match {
      case MAX_GENS => bestAllTime._1
      case n =>
        val listaBots: List[Bot] =
          genAnt match {
            case List() => {
              (1 to popSize).map(new Bot(0, _,
                   // List(new Action("turnRadarRight", new Constant(360))),
                   // (1 to NUM_CHROMOS).map(i => Functions.getRandomGenExp(0, 0)).toList,
                    (1 to NUM_CHROMOS).map(i => Functions.getRandomGenExp(0)).toList/*,
                    (1 to NUM_CHROMOS).map(i => Functions.getRandomGenExp(0, 2)).toList,
                    (1 to NUM_CHROMOS).map(i => Functions.getRandomGenExp(0, 3)).toList*/)).toList
            }
            case xs => xs
          }
        listaBots.foreach(p => p.compile(p.construct()))
        if(numGens>0)
          (0 to POP_SIZE).map(i=> PATH + "\\" + "X_GPbot_" + (numGens-1) + "_" + i).foreach(i=>deleteFile(i))
        val botFitnessList = roboCodeEngine.runBatchWithSamples(listaBots, ENEMIES_LIST, ROUNDS)
        val bestInGen: (Bot, Double) = botFitnessList.maxBy(_._2)
        val bestInGenCopy = new Bot(numGens+1, 0, bestInGen._1.chromosOnScan/*, bestInGen._1.chromosOnBullet, bestInGen._1.chromosOnHit*/)
        val nextGen: List[Bot] = (1 to popSize - 1).map(i => generateBot(i, botFitnessList, Math.random(), numGens+1)).toList
        val newBestAllTime = if (bestInGen._2 > bestAllTime._2) bestInGen else bestAllTime
        generationLoop(numGens + 1, bestInGenCopy :: nextGen, popSize, roboCodeEngine, newBestAllTime)
    }
  }

  def generateBot(indice: Int, listaBots: List[(Bot, Double)], prob: Double, actualGen: Int): Bot = {
    val bestAdapted = listaBots.sortBy(_._2).reverse.take((listaBots.length * 0.9).toInt)
    prob match {
      case p if p < PROB_CROSSOVER => {
        val selected = selectTournament(bestAdapted.toArray, 2)
        crossOver(selected(0), selected(1), indice, actualGen + 1)
      }
      case p => {
        val selected = selectTournament(bestAdapted.toArray, 1)
        mutation(selected(0), indice, actualGen + 1)
      }
    }
  }

  def deleteFile(name:String)={
    val oldJava = new File(name+".java");
    val oldClass = new File(name+".class");
    oldJava.delete();
    oldClass.delete();
  }

  def selectTournament(lista: Array[(Bot, Double)], number: Int): Array[Bot] = {
    Stream.from(1).map(i => Random.nextInt(lista.length)).distinct.take(number).map(lista(_)._1).toArray
  }

  def crossOver(parent1: Bot, parent2: Bot, pos: Int, gen: Int): Bot = {
   /* val mapChromosNormal1 = (1 to NUM_CHROMOS).map(i => (i, parent1.chromosNormal(i))).toMap
    val mapChromosNormal2 = (1 to NUM_CHROMOS).map(i => (i, parent2.chromosNormal(i))).toMap*/
    val mapChromosOnScan1 = (0 to NUM_CHROMOS-1).map(i => (i, parent1.chromosOnScan(i))).toMap
    val mapChromosOnScan2 = (0 to NUM_CHROMOS-1).map(i => (i, parent2.chromosOnScan(i))).toMap
    /*val mapChromosOnBullet1 = (1 to NUM_CHROMOS).map(i => (i, parent1.chromosOnBullet(i))).toMap
    val mapChromosOnBullet2 = (1 to NUM_CHROMOS).map(i => (i, parent2.chromosOnBullet(i))).toMap
    val mapChromosOnHit1 = (1 to NUM_CHROMOS).map(i => (i, parent1.chromosOnHit(i))).toMap
    val mapChromosOnHit2 = (1 to NUM_CHROMOS).map(i => (i, parent2.chromosOnHit(i))).toMap*/

  /*  val probOfMutation = List(1, 0.3)
    val probability: Double = probOfMutation(Random.nextInt(probOfMutation.length))

    if (Math.random() < probability) {
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.take(2)
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        if (Math.random < PROB_JUMP_GENOMES)
          mapChromosNormal1.updated(indexChromosSelected(0), mapChromosNormal2.get(indexChromosSelected(1)))
        else
          mapChromosNormal1.updated(indexChromosSelected(0), mapChromosNormal2.get(indexChromosSelected(0)))
      else {
        mapChromosNormal1.updated(indexChromosSelected(0), mapChromosNormal2.get(indexChromosSelected(0)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
        mapChromosNormal1.updated(indexChromosSelected(1), mapChromosNormal2.get(indexChromosSelected(1)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
      }
 }
    val childChromosNormal = mapChromosNormal1

    val probOfMutation2 = probOfMutation.filter(n => n != probability)
    val probability2 = probOfMutation2(Random.nextInt(probOfMutation2.length))

    if (Math.random() < probability2) {*/
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.take(2)
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        if (Math.random < PROB_JUMP_GENOMES)
          mapChromosOnScan1.updated(indexChromosSelected(0), mapChromosOnScan2.get(indexChromosSelected(1)))
        else
          mapChromosOnScan1.updated(indexChromosSelected(0), mapChromosOnScan2.get(indexChromosSelected(0)))
      else {
        mapChromosOnScan1.updated(indexChromosSelected(0), mapChromosOnScan2.get(indexChromosSelected(0)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
        mapChromosOnScan1.updated(indexChromosSelected(1), mapChromosOnScan2.get(indexChromosSelected(1)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
      }
   // }
    val childChromosScan = mapChromosOnScan1

  /*  val probOfMutation2 = probOfMutation.filter(n => n != probability)
    val probability2 = probOfMutation2(Random.nextInt(probOfMutation2.length))

    if (Math.random() < probability2) {
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.take(2)
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        if (Math.random < PROB_JUMP_GENOMES)
          mapChromosOnBullet1.updated(indexChromosSelected(0), mapChromosOnBullet2.get(indexChromosSelected(1)))
        else
          mapChromosOnBullet1.updated(indexChromosSelected(0), mapChromosOnBullet2.get(indexChromosSelected(0)))
      else {
        mapChromosOnBullet1.updated(indexChromosSelected(0), mapChromosOnBullet2.get(indexChromosSelected(0)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
        mapChromosOnBullet1.updated(indexChromosSelected(1), mapChromosOnBullet2.get(indexChromosSelected(1)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
      }
    }
    val childChromosBullet = mapChromosOnBullet1

    val probOfMutation4 = probOfMutation3.filter(n => n != probability3)
    val probability4 = probOfMutation4(Random.nextInt(probOfMutation4.length))

    if (Math.random() < probability4) {
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.take(2)
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        if (Math.random < PROB_JUMP_GENOMES)
          mapChromosOnHit1.updated(indexChromosSelected(0), mapChromosOnHit2.get(indexChromosSelected(1)))
        else
          mapChromosOnHit1.updated(indexChromosSelected(0), mapChromosOnHit2.get(indexChromosSelected(0)))
      else {
        mapChromosOnHit1.updated(indexChromosSelected(0), mapChromosOnHit2.get(indexChromosSelected(0)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
        mapChromosOnHit1.updated(indexChromosSelected(1), mapChromosOnHit2.get(indexChromosSelected(1)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
      }
    }
    val childChromosHit = mapChromosOnHit1*/

    new Bot(gen, pos, /*childChromosNormal.values.toList,*/ childChromosScan.values.toList/*, childChromosBullet.values.toList, childChromosHit.values.toList*/)
  }

  def mutation(parent1: Bot, pos: Int, gen: Int): Bot = {
//    val mapChromosNormal1 = (1 to NUM_CHROMOS).map(i => (i, parent1.chromosNormal(i))).toMap
   // val mapChromosOnBullet1 = (1 to NUM_CHROMOS).map(i => (i, parent1.chromosOnBullet(i))).toMap
    val mapChromosOnScan1 = (0 to NUM_CHROMOS-1).map(i => (i, parent1.chromosOnScan(i))).toMap
    //val mapChromosOnHit1 = (1 to NUM_CHROMOS).map(i => (i, parent1.chromosOnHit(i))).toMap

    //val probOfMutation = List(1, 0.3)
    //val probability: Double = probOfMutation(Random.nextInt(probOfMutation.length))

  /*  if (Math.random() < probability) {
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.head
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        mapChromosNormal1.updated(indexChromosSelected, Functions.getRandomGenExp(Random.nextInt(MAX_DEPTH_CHROMOS), 0))
      else
        mapChromosNormal1.updated(indexChromosSelected, mapChromosNormal1.get(indexChromosSelected).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
  }
    val childChromosNormal = mapChromosNormal1*/

 /*   val probOfMutation2 = probOfMutation.filter(n => n != probability)
    val probability2 = probOfMutation2(Random.nextInt(probOfMutation2.length))

    if (Math.random() < probability2) {*/
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.head
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        mapChromosOnScan1.updated(indexChromosSelected, Functions.getRandomGenExp(Random.nextInt(MAX_DEPTH_CHROMOS)))
      else
        mapChromosOnScan1.updated(indexChromosSelected, mapChromosOnScan1.get(indexChromosSelected).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
  //  }
    val childChromosScan = mapChromosOnScan1

    /*val probOfMutation2 = probOfMutation.filter(n => n != probability)
    val probability2 = probOfMutation2(Random.nextInt(probOfMutation2.length))

    if (Math.random() < probability2) {
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.head
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        mapChromosOnBullet1.updated(indexChromosSelected, Functions.getRandomGenExp(Random.nextInt(MAX_DEPTH_CHROMOS), 1))
      else
        mapChromosOnBullet1.updated(indexChromosSelected, mapChromosOnBullet1.get(indexChromosSelected).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
    }
    val childChromosBullet = mapChromosOnBullet1

    val probOfMutation4 = probOfMutation3.filter(n => n != probability3)
    val probability4 = probOfMutation4(Random.nextInt(probOfMutation4.length))

    if (Math.random() < probability4) {
      val indexChromosSelected = Stream.from(1).map(i => Random.nextInt(NUM_CHROMOS)).distinct.head
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        mapChromosOnHit1.updated(indexChromosSelected, Functions.getRandomGenExp(Random.nextInt(MAX_DEPTH_CHROMOS), 1))
      else
        mapChromosOnHit1.updated(indexChromosSelected, mapChromosOnHit1.get(indexChromosSelected).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
    }
    val childChromosHit = mapChromosOnHit1*/

    new Bot(gen, pos, /*childChromosNormal.values.toList,*/ childChromosScan.values.toList/*, childChromosBullet.values.toList, childChromosHit.values.toList*/)
  }
}