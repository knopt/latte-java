package Latte.Backend;

import Latte.Backend.Instructions.Label;

public class LabelsGenerator {
    private static int NONCE = 0;

    public static Label getNonceLabel(String labelName) {
        return new Label(labelName + "_" + NONCE++);
    }
}
