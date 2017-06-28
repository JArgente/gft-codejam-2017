import java.io.File

import robocode.control.{BattleSpecification, RobocodeEngine}
import Constants._

import scala.annotation.tailrec
import scala.util.Random
/**
  * Created by jeae on 14/06/2017.
  */
object Main extends App{
  val bestOfBests=MainEx.generationLoop(0, List.empty, POP_SIZE, new Robocode(), (null, (-1.0, -1.0, -1.0)))
  System.out.println("AMONG "+ POP_SIZE+" bots in each generation, after "+MAX_GENS+" generations the best bot is: "+bestOfBests.botName)
  bestOfBests.compile(bestOfBests.construct())
}

object MainEx {

  @tailrec
  final def generationLoop(numGens: Int, genAnt: List[Bot], popSize: Int, roboCodeEngine: Robocode, bestAllTime: (Bot, (Double, Double, Double))): Bot = {
    numGens match {
      case MAX_GENS => bestAllTime._1
      case n =>
        val listaBots: List[Bot] =
          genAnt match {
            case List() => {
              (1 to popSize).map(new Bot(0, _,
                    (1 to Random.nextInt(NUM_CHROMOS)+1).map(i => Functions.getRandomGenExp(0, listaAccionesNormal, listaScanNumNormal)).toList,
                    (1 to Random.nextInt(NUM_CHROMOS)+1).map(i => Functions.getRandomGenExp(0, listaAccionesScan, listaScanNumScaner)).toList)).toList
            }
            case xs => xs
          }
        listaBots.foreach(p => p.compile(p.construct()))
        if(numGens>0)
          (0 to POP_SIZE).map(i=> PATH + "\\" + "X_GPbot_" + (numGens-1) + "_" + i).foreach(i=>deleteFile(i))
        val botFitnessList = roboCodeEngine.runBatchWithSamples(listaBots, ENEMIES_LIST, ROUNDS)
        val bestInGen: (Bot, (Double, Double, Double)) = botFitnessList.maxBy(_._2._3)
        val bestInGenCopy = new Bot(numGens+1, 0,bestInGen._1.chromosNormal, bestInGen._1.chromosOnScan/*, bestInGen._1.chromosOnBullet, bestInGen._1.chromosOnHit*/)
        val nextGenNormal: List[Bot] = (1 to popSize - 1).map(i => generateBotNormal(i, botFitnessList, Math.random(), numGens+1)).toList
        val nextGenScan: List[Bot] = (1 to popSize - 1).map(i => generateBotScan(i, botFitnessList, Math.random(), numGens+1)).toList
        val nextGen: List[Bot] = mergeGens(nextGenNormal.toArray, nextGenScan.toArray)
        val newBestAllTime = if (bestInGen._2._3 > bestAllTime._2._3) bestInGen else bestAllTime
        generationLoop(numGens + 1, bestInGenCopy :: nextGen, popSize, roboCodeEngine, newBestAllTime)
    }
  }

  def mergeGens(nextGenNormal: Array[Bot], nextGenScan: Array[Bot]): List[Bot] ={
    (1 to nextGenNormal.length).map(i=>new Bot(nextGenNormal(i).numGen, nextGenNormal(i).idBot, nextGenNormal(i).chromosNormal, nextGenScan(i).chromosOnScan)).toList
  }

  def generateBotNormal(indice: Int, listaBots: List[(Bot, (Double, Double, Double))], prob: Double, actualGen: Int): Bot = {
    val bestAdapted = listaBots.sortBy(_._2._1).reverse.take((listaBots.length * 0.7).toInt)
    prob match {
      case p if p < PROB_CROSSOVER => {
        val selected = selectTournament(bestAdapted.toArray, 2)
        val chromos= crossOverGens(selected(0).chromosNormal, selected(1).chromosNormal)
        new Bot(actualGen + 1, indice, chromos, selected(0).chromosOnScan)
      }
      case p => {
        val selected = selectTournament(bestAdapted.toArray, 1)
        val chromos=mutation(selected(0).chromosNormal, listaAccionesNormal, listaScanNumNormal)
        new Bot(actualGen + 1, indice, chromos, selected(0).chromosOnScan)
      }
    }
  }

  def generateBotScan(indice: Int, listaBots: List[(Bot, (Double, Double, Double))], prob: Double, actualGen: Int): Bot = {
    val bestAdapted = listaBots.sortBy(_._2._2).reverse.take((listaBots.length * 0.7).toInt)
    prob match {
      case p if p < PROB_CROSSOVER => {
        val selected = selectTournament(bestAdapted.toArray, 2)
        val chromos= crossOverGens(selected(0).chromosOnScan, selected(1).chromosOnScan)
        new Bot(actualGen + 1, indice, selected(0).chromosNormal, chromos)
      }
      case p => {
        val selected = selectTournament(bestAdapted.toArray, 1)
        val chromos=mutation(selected(0).chromosOnScan, listaAccionesScan, listaScanNumScaner)
        new Bot(actualGen + 1, indice, selected(0).chromosNormal, chromos)
      }
    }
  }

  def deleteFile(name:String)={
    val oldJava = new File(name+".java");
    val oldClass = new File(name+".class");
    oldJava.delete();
    oldClass.delete();
  }

  def selectTournament(lista: Array[(Bot, (Double,Double,Double))], number: Int): Array[Bot] = {
    Stream.from(1).map(i => Random.nextInt(lista.length)).distinct.take(number).map(lista(_)._1).toArray
  }

  def crossOverGens(parent1: List[GenExp], parent2: List[GenExp]): List[GenExp] = {
    val mapChromos1 = (0 to parent1.length-1).map(i => (i, parent1(i))).toMap
    val mapChromos2 = (0 to parent2.length-1).map(i => (i, parent2(i))).toMap

      val indexChromosSelected1 = Stream.from(1).map(i => Random.nextInt(parent1.length)).distinct.take(2)
      val indexChromosSelected2 = Stream.from(1).map(i => Random.nextInt(parent2.length)).distinct.take(2)
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        if (Math.random < PROB_JUMP_GENOMES)
          mapChromos1.updated(indexChromosSelected1(0), mapChromos2.get(indexChromosSelected2(1)))
        else
          mapChromos1.updated(indexChromosSelected1(0), mapChromos2.get(indexChromosSelected2(0)))
      else {
        mapChromos1.updated(indexChromosSelected1(0), mapChromos2.get(indexChromosSelected2(0)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
        mapChromos1.updated(indexChromosSelected1(1), mapChromos2.get(indexChromosSelected2(1)).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))
      }

    mapChromos1.values.toList
  }

  def mutation(parent1: List[GenExp], listaAcciones: Vector[(String, (Double, Double))], listaScan: Vector[(String, (Double, Double))]): List[GenExp] = {
    val mapChromos1 = (0 to parent1.length-1).map(i => (i, parent1(i))).toMap

      val indexChromosSelected = Random.nextInt(parent1.length)
      if (Math.random < PROB_SWAP_ENTIRE_CHROMO)
        mapChromos1.updated(indexChromosSelected, Functions.getRandomGenExp(Random.nextInt(MAX_DEPTH_CHROMOS), listaAcciones, listaScan))
      else
        mapChromos1.updated(indexChromosSelected, mapChromos1.get(indexChromosSelected).get.getSubtree(Random.nextDouble() < PROB_CROSS_TERMINAL))

    mapChromos1.values.toList
  }
}