/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dependencies.RobotInfo;


/**
 *  For notifications
 * @author sasinda
 */

public class Hit{
    private RobotInfo attacker;
    private RobotInfo victim;

    public Hit(RobotInfo attacker, RobotInfo victim) {
        this.attacker = attacker;
        this.victim = victim;
    }
    
    public RobotInfo getAttacker() {
        return attacker;
    }

    public RobotInfo getVictim() {
        return victim;
    }
    
    
}
