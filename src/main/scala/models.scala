/**
  * Created by jeae on 14/06/2017.
  */
import java.io.{BufferedWriter, FileWriter}

import Constants._


case class Bot(numGen: Int, idBot: Int/*, chromosNormal: List[GenExp]*/, chromosOnScan: List[GenExp]/*, chromosOnBullet: List[GenExp], chromosOnHit: List[GenExp]*/){
  val botName: String = "X_GPbot_"+numGen+"_"+idBot
  val fileName = PACKAGE+"."+botName;

  def construct():String={
    //val programNormal:String=chromosNormal.foldLeft("")((a,b)=>a+b.parseToCode()+";")
    val programOnScan:String=chromosOnScan.foldLeft("")((a,b)=>a+b.parseToCode()+";")
   /* val programOnBullet:String=chromosOnBullet.foldLeft("")((a,b)=>a+b.parseToCode()+";")
    val programOnHit:String=chromosOnHit.foldLeft("")((a,b)=>a+b.parseToCode()+";")*/
    sourceCodeBase.replace("--botname--", botName).replace("--scanned--", programOnScan)/*.replace("--bulletHit--", programOnBullet).replace("--hitByBullet--", programOnHit)*/
  }

  def compile(source: String):String={
        val fstream = new FileWriter(PATH+"\\"+botName+".java");
        val out = new BufferedWriter(fstream);
        out.write(source);
        out.close();
        execute("javac -cp " + JARS + " " + PATH + "\\" + botName + ".java");

      return (PATH+"\\"+botName+".class");
  }

  def execute(command: String)={
    val process = Runtime.getRuntime().exec(command);
    process.waitFor();
    if(process.exitValue() != 0)
      System.out.println(command + "exited with value " + process.exitValue());
  }

}
