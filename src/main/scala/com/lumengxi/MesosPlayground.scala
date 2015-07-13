package com.lumengxi

import com.typesafe.config.ConfigFactory
import org.apache.mesos.MesosSchedulerDriver
import org.apache.mesos.Protos.{ FrameworkInfo, FrameworkID }

import com.lumengxi.scheduler.MesosPlaygroundScheduler

object MesosPlayground extends Logging {

  val config = ConfigFactory.load.getConfig("com.lumengxi")

  val normalizedName = "mesos-playground"
  val failoverTimeout = 600000 // in milliseconds (10 minutes)
  val mesosMaster = config.getString("mesos.master")

  val frameworkId = FrameworkID.newBuilder.setValue(normalizedName)

  val frameworkInfo = FrameworkInfo.newBuilder()
    .setId(frameworkId)
    .setName(normalizedName)
    .setFailoverTimeout(failoverTimeout)
    .setUser("") // let Mesos assign the user
    .setCheckpoint(true)
    .build

  // Execution entry point
  def main(args: Array[String]): Unit = {
    log.info("Hello from framework [{}]!", normalizedName)

    val scheduler = new MesosPlaygroundScheduler

    val dummyTasks = (1 to 100).toList

    scheduler.submitTasks(dummyTasks)

    val driver = new MesosSchedulerDriver(
      scheduler,
      frameworkInfo,
      mesosMaster
    )

    driver.run()
  }

}