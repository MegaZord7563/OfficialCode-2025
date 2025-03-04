package frc.robot.commands.AutonomousCmd;



//WPI Imports
import edu.wpi.first.wpilibj2.command.Command;

//Robot imports
import frc.robot.RobotContainer;
import frc.robot.subsystems.LedControl;
import frc.robot.subsystems.Coral.ArticuladorCoralSubsystem;
import frc.robot.subsystems.Swerve.SwerveSubsystem;
import frc.robot.subsystems.lift.LiftSubsystem;

public class LiftAutoCmd extends Command
{
   // Declare the boolean variables   
    private boolean l1 = false;
    private boolean l2 = false;
    private boolean l3 = false;
    private boolean l4 = false;
   
    private boolean finish = false;

    //Declare doubles
    private double encoderPosition = 0;
    private double tolerance = 0.05;//5%

    //setpoint in meters
   

    

    private double l1Setpoint = 0.00; //l1SetpointClear/LiftConstants.kLiftGearRatio;
    private double l2Setpoint = 0.45; //l2SetpointClear/LiftConstants.kLiftGearRatio;
    private double l3Setpoint = 0.85; //l3SetpointClear/LiftConstants.kLiftGearRatio;
    private double l4Setpoint = 1.45; //l4SetpointClear/LiftConstants.kLiftGearRatio;

    
    

  // Create the subsystem
  private final LiftSubsystem liftSubsystem = RobotContainer.liftSubsystem;
  private final ArticuladorCoralSubsystem artCoralSubsystem = RobotContainer.articuladorCoralSubsystem;
  private final LedControl ledControl = RobotContainer.ledControl;
 

  /** Creates a new AngleCmd. */
  public LiftAutoCmd(boolean l1, boolean l2, boolean l3, boolean l4) 
  {
    this.l1 = l1;
    this.l2 = l2;
    this.l3 = l3;
    this.l4 = l4;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(liftSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() 
  {
    // Print the initialization of the command
    System.out.println("Lift Command Initialized");

    finish = false;
    // Reset the encoder
    //liftSubsystem.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() 
  {
    // Get the encoder position
    encoderPosition = liftSubsystem.getPosition();

    // Move the lift
    if (l1)
    {
      System.out.println("L1 SELECTED");
      //liftSubsystem.runMotor(l1Setpoint, false);
      
      //if (l1Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l1Setpoint * (1+tolerance)) finish=true;
      finish =true;
      if(!artCoralSubsystem.sensorCoral())ledControl.setWhiteLed();
    }
    if (l2)
    {
      System.out.println("L2 SELECTED");
      if(!artCoralSubsystem.sensorCoral())ledControl.setBlueLed();
      liftSubsystem.runMotor(l2Setpoint, false);
      
      if (l2Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l2Setpoint * (1+tolerance)) finish=true;
     

    }
    if (l3)
    {
      System.out.println("L3 SELECTED");
      if(!artCoralSubsystem.sensorCoral())ledControl.setGreenLed();
      liftSubsystem.runMotor(l3Setpoint, false);
      if (l3Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l3Setpoint * (1+tolerance)) finish=true;
      
    }
    if (l4 && artCoralSubsystem.sensorCoral())
    {
      if(!artCoralSubsystem.sensorCoral())ledControl.setRedLed();
      System.out.println("L4 SELECTED");
      liftSubsystem.runMotor(l4Setpoint , false);
      if (l4Setpoint * (1-tolerance) < encoderPosition && encoderPosition < l4Setpoint * (1+tolerance)) finish=true;
      
    }

    if(!l1 && !l2 && !l3 && !l4)finish=true;
    if(RobotContainer.driverJoystick.start().getAsBoolean())finish=true;

  }    

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) 
  {
    // Stop the lift
    //liftSubsystem.stopMotor();
    // Print the end of the command
    ledControl.setBlackLed();
    
    System.out.println("Lift Command Done!");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() 
  {
    return finish ;
  }

}

