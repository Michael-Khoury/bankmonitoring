package com.michael.spark;

import org.apache.spark.sql.*;

public class FraudAnalysis {
    public static void main(String[] args) {
        System.setProperty("spark.driver.host", "127.0.0.1");

        // 1. Start Spark session
        SparkSession spark = SparkSession.builder()
                .appName("Fraud Detection Job")
                .master("local[*]") // Use all available CPU cores

                .getOrCreate();

        // 2. Read the transactions table from PostgreSQL
        Dataset<Row> transactions = spark.read()
                .format("jdbc")

               /* .option("url", "jdbc:postgresql://localhost:5432/bank_monitoring")
                .option("dbtable", "transaction")
                .option("user", "postgres")
                .option("password", "Canada@2022") // Replace this

                */

                .option("url", "jdbc:postgresql://bankmonitoring-db.cbgs0wgg82l4.us-east-2.rds.amazonaws.com:5432/bank_monitoring")
                .option("dbtable", "transaction")
                .option("user", "postgres")
                .option("password", "REALMADRIDfootball33")
                .option("driver", "org.postgresql.Driver")

                .load();

        // 3. Show schema and contents
        transactions.printSchema();
        transactions.show();

        // 4. Apply simple fraud logic (e.g. high value transactions)
        Dataset<Row> suspicious = transactions.filter("amount > 1000");

        // 5. Display and save the suspicious ones
        suspicious.show();
        /*
        suspicious.write()
                .mode(SaveMode.Overwrite)
                .option("header", true)
                .csv("output/suspicious_transactions");
        */
        // 6. Stop Spark session
        spark.stop();
    }
}
