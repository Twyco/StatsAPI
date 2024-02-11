package de.twyco.statsapi.calculator;

public enum Operator {

    ADD,
    SUB,
    MUL,
    DIV;

    public double performAction(double fstOperand, double sndOperand){
        switch (this){
            case ADD -> {
                return fstOperand + sndOperand;
            }
            case SUB -> {
                return fstOperand - sndOperand;
            }
            case MUL -> {
                return fstOperand * sndOperand;
            }
            case DIV -> {
                return fstOperand / sndOperand;
            }
            default -> {
                return 0;
            }
        }
    }
}
