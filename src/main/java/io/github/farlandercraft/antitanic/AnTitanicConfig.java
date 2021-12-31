package io.github.farlandercraft.antitanic;

public class AnTitanicConfig {
    public boolean boatsDealDamage;
    public boolean volatileBoats;
    public boolean iceBreaksBoats;
    public double speedMultiplier;
    public AnTitanicConfig(boolean bDD, boolean vB, boolean antiantitanic, double boatSpeed) {
        this.boatsDealDamage = bDD;
        this.volatileBoats = vB;
        this.iceBreaksBoats = antiantitanic;
        this.speedMultiplier = boatSpeed;
    }
}
