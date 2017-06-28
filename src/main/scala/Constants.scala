/**
  * Created by jeae on 18/06/2017.
  */
object Constants {

  val MAX_GENS: Int=500
  val POP_SIZE: Int=10
  val NUM_CHROMOS: Int=3
  val MAX_DEPTH_CHROMOS: Int= 3
  val PACKAGE= "sampleex"

  val PROB_ACTION: Double= 0.4
  val PROB_TERMINAL: Double= 0.3
  val PATH: String="C:\\robocode\\robots\\sampleex"
  val JARS: String= "C:\\robocode\\libs\\robocode.jar;"

  val ROUNDS: Int=10

  val PROB_CROSSOVER: Double = 0.8
  val PROB_SWAP_ENTIRE_CHROMO = 0.3
  val PROB_JUMP_GENOMES = 0.05
  val PROB_CROSS_TERMINAL = 0.1

  val listaAccionesNormal :Vector[(String,(Double, Double))]=Vector(
    ("turnGunLeft",(0.0, 360.0)),
    ("turnGunRight",(0.0, 360.0)),
    ("ahead",(0.0, 600.0)),
    ("back",(0.0, 600.0)),
    ("turnLeft",(0.0, 360.0)),
    ("turnRight",(0.0, 360.0)) )/*,
    Vector("turnGunLeft", "turnGunRight", "fire", "fireBullet","setAhead", "setBack", "setMaxTurnRate", "setMaxVelocity"),
    Vector("turnGunLeft", "turnGunRight", "fire", "fireBullet","setAhead", "setBack", "setMaxTurnRate", "setMaxVelocity")*/
  //)

  val listaAccionesScan :Vector[(String,(Double, Double))]=Vector(
    ("turnGunLeft",(0.0, 360.0)),
    ("turnGunRight",(0.0, 360.0)),
    ("fire",(1.0, 3.0)),
    ("turnLeft",(0.0, 360.0)),
    ("turnRight",(0.0, 360.0)) )

  val listaScanNumNormal: Vector[(String,(Double, Double))]= Vector(
    ("getGunHeading", (0.0, 360.0)),
    ("getGunHeat", (0.0, 3.0)),
    ("getRadarHeading", (0.0, 360.0)),
    ("getVelocity", (0.0, 8.0)))

  val listaScanNumScaner: Vector[(String,(Double, Double))]= Vector(
    ("e.getBearing",(0.0, 180.0)),
    ("e.getDistance", (0.0, 800.0)),
    ("e.getEnergy", (0.0, 100.0)),
    ("e.getHeading", (0.0, 360.0)),
    ("e.getVelocity", (0.0, 8.0)),
    ("getGunHeading", (0.0, 360.0)),
    ("getGunHeat", (0.0, 3.0)),
    ("getRadarHeading", (0.0, 360.0)),
    ("getVelocity", (0.0, 8.0)))

  val sourceCodeBase: String=
      "package "+PACKAGE+";" +
        "\nimport robocode.*;\n" +
        "\nimport java.util.*;\n" +
     //   "\nimport java.awt.*;\n" +
        "\n" +
        "\npublic class --botname-- extends Robot {" +
        "\nRandom rand= new Random();" +
        "\n" +
        "\n	public void run() {" +
     /*   "\n" +
        "\n setAdjustGunForRobotTurn(true);" +*/
        "\n" +
   //     "\n		setColors(Color.red,Color.blue,Color.green);" +
        "\n		while(true) {"+
        "\n			--normal--"+
        "\n		}" +
        "\n" +
        "\n	}" +
        "\n	public void onScannedRobot(ScannedRobotEvent e) {" +
        "\n  --scanned--" +
      "\n	}" +
        "\n	public void onHitWall(HitWallEvent e) {" +
        "\n  turnRight(60+ rand.nextInt(120));" +
        "\n	}" +
/*        "\n	public void onBulletHit(BulletHitEvent e) {" +
        "\n  --bulletHit--" +
        "\n	}" +
        "\n	public void onHitByBullet(HitByBulletEvent e) {" +
        "\n  --hitByBullet--" +
        "\n	}" +*/
      "\n" +
      "\n}"

  val ENEMIES_LIST = List(//"sample.SuperCrazy",
    //"sample.SuperTracker"
    //"sample.SuperTrackFire",
    "sample.RamFire") //"ary.micro.Weak 1.2"
  //"sheldor.nano.Sabreur_1.1.1"
  //"sample.Sabreur"
  //"mld.LittleBlackBook_1.69e"
  //"mld.Moebius_2.9.3"

}
