import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.optimization.L1Updater
import org.apache.spark.mllib.regression.LabeledPoint
import org.slf4j.LoggerFactory

object SimpleApp {
  val logger = LoggerFactory.getLogger("SimpleApp")

  def main(args: Array[String]) {
    val sc = new SparkContext("local", "Simple App", "/Users/user/tools/spark-1.4.0-bin-hadoop2.6",
      List("target/scala-2.10/target/scala-2.10/scala-2-10_2.10-0.1-SNAPSHOT.jar"))

    val data = sc.textFile("data/armsbbc.txt")
    val splits = data.randomSplit(Array(0.6, 0.4), seed = 15l)

    val training = splits(0).map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts.tail.map(x => x.toDouble)))
    }.cache()

    val test = splits(1).map { line =>
      val parts = line.split(',')
      LabeledPoint(parts(0).toDouble, Vectors.dense(parts.tail.map(x => x.toDouble)))
    }

    // Run training algorithm
    val numIterations = 50
    val alg = new SVMWithSGD()
    alg.optimizer.
      setNumIterations(numIterations)//.
//      setRegParam(1)

    val model = alg.run(training)

    // Evaluate model on training examples and compute training error
    val labelAndPreds = test.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / test.count
    println("Training Error = " + trainErr)

//    model.save(sc, "data/model.svm")
  }
}
