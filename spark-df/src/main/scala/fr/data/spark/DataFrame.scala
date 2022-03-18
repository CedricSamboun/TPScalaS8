package fr.data.spark

import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._




object DataFrame {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("job1")
                                      .master("local[*]")
                                      .getOrCreate()

val df = spark.read.format("csv").option("header","true")
                                .option("delimiter", ";")
                                .option("inferSchema", "true")
                                .load("src/main/resources/codesPostaux.csv")
df.show()

df.printSchema

df.select(countDistinct("Code_commune_INSEE"))
  .show()

df.filter(col("Ligne_5")
  .isNotNull)
  .select(countDistinct("Code_commune_INSEE"))
  .show()



val df2 = df.withColumn("Departement", col("Code_commune_INSEE").substr(1,2))
df2.show()

df2.sort("Code_postal").write.option("header",true).csv("Nom_commune.csv")

println("SAUT \n")
df2.filter("Departement = 2").show()


println(" \n Departement avec le plus de communes \n")
df2.groupBy("Departement").count().sort(desc("count")).show()
  }
}