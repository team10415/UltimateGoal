package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.team10415.ftc_recorder.Controller;
import com.team10415.ftc_recorder.Record;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOpRecorder extends com.team10415.ftc_recorder.Recorder {
    Controller controller = new TeleOpController();

    @NotNull
    @Override
    public Controller getController() {
        return controller;
    }

    @NotNull
    @Override
    public String getName() {
        return "teleop1";
    }

    @NotNull
    @Override
    public List<Record.Input> getInputs() {
        List list = Arrays.asList(
                Record.Input.BUTTONS,
                Record.Input.JOYSTICKS,
                Record.Input.TRIGGERS,
                Record.Input.DPAD
        );
        return list;
    }
}
