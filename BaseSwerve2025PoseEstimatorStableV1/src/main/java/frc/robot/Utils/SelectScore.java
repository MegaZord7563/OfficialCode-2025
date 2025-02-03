// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Utils.TouchScreenInterface.VirtualButton;

/** Add your docs here. */
public class SelectScore 
{

    public static Pose2d poseToScore(TouchScreenInterface touch){
        Pose2d poseToScore;
        var alliance2 = DriverStation.getAlliance();
        if (alliance2.get() == DriverStation.Alliance.Red) 
        {
            // Red Alliance Values
            if(touch.getButtonValue(VirtualButton.kA)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kB)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kC)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kD)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kE)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kF)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kG)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kH)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kI)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kJ)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kK)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kL)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else{
                poseToScore = new Pose2d();
            }
        }else{
            // Blue Alliance Values

            if(touch.getButtonValue(VirtualButton.kA)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(3));
            }
            else if (touch.getButtonValue(VirtualButton.kB)){
                poseToScore = new Pose2d(7,8,Rotation2d.fromDegrees(9));
            }
            else if (touch.getButtonValue(VirtualButton.kC)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kD)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kE)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kF)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kG)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kH)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kI)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kJ)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kK)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else if (touch.getButtonValue(VirtualButton.kL)){
                poseToScore = new Pose2d(1,2,Rotation2d.fromDegrees(0));
            }
            else{
                poseToScore = new Pose2d();
            }
        }



        
        
        return poseToScore;
    }

    public static int levelToScore(){
        return 0;
    }

}
