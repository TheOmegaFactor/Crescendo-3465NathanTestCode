// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  HttpCamera httpCamera = new HttpCamera("CoprocessorCamera", "http://10.34.65.11:5800/stream.mjpg"); 
  NetworkTableInstance mNetworkTable = NetworkTableInstance.getDefault();

  private NetworkTableEntry tv, tx, ty, ta;

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();

    CameraServer.addCamera(httpCamera);
    Shuffleboard.getTab("Tab").add(httpCamera);

    tv = mNetworkTable.getTable("limelight").getEntry("tv");
    tx = mNetworkTable.getTable("limelight").getEntry("tx");
    ty = mNetworkTable.getTable("limelight").getEntry("ty");
    ta = mNetworkTable.getTable("limelight").getEntry("ta");

  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    double targetVisible = tv.getDouble(0.00);
    double targetX = tx.getDouble(0.00);
    double targetY = ty.getDouble(0.00);
    double targetArea = ta.getDouble(0.00);

    SmartDashboard.putNumber("Target X", targetX);
    SmartDashboard.putNumber("Target Y", targetY);
    SmartDashboard.putNumber("Target Area", targetArea);

    if (targetVisible == 1) {
      SmartDashboard.putBoolean("I see an AprilTag", true);
    } else {
      SmartDashboard.putBoolean("I see an AprilTag", false);
    }
    
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    m_robotContainer.autoInit();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    m_robotContainer.teleopInit();
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
