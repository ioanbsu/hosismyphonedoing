package com.artigile.howismyphonedoing.client.canvas;


/**
 * User: ioanbsu
 * Date: 3/11/11
 * Time: 6:02 PM
 */
public class SpringObject {
  public static final double springStrength = 0.1;
  public static final double friction = 0.8;
  public Vector pos, vel, goal;

  public SpringObject(Vector start) {
    this.pos = new Vector(start);
    this.vel = new Vector(0, 0);
    this.goal = new Vector(start);
  }

  public void update() {
    Vector d = Vector.sub(goal, pos);
    d.mult(springStrength);
    vel.add(d);
    vel.mult(friction);
    pos.add(vel);
  }
}