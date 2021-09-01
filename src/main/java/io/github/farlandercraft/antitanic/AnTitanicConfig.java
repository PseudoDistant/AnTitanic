package io.github.farlandercraft.antitanic;

public class AnTitanicConfig {
    public boolean boatsDealDamage;
    public boolean volatileBoats;
    public boolean iceBreaksBoats;
    public AnTitanicConfig(boolean bDD, boolean vB, boolean antiantitanic) {
        this.boatsDealDamage = bDD;
        this.volatileBoats = vB;
        this.iceBreaksBoats = antiantitanic;
    }
}
