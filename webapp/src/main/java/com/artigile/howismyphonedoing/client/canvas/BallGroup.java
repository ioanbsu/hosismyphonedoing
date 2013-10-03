package com.artigile.howismyphonedoing.client.canvas;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

import java.util.ArrayList;

/**
 * User: ioanbsu
 * Date: 3/11/11
 * Time: 6:01 PM
 */

public class BallGroup {
    final double width;
    final double height;
    Ball[] balls;

    public BallGroup(double width, double height) {
        this.width = width;
        this.height = height;
        ArrayList<Ball> ballsArrayList = new ArrayList<Ball>(0);

        // init balls (values from Google's homepage)
        //W
        ballsArrayList.add(new Ball(16, 15, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(21, 26, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(25, 36, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(28, 46, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(30, 58, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(34, 65, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(39, 59, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(40, 53, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(43, 39, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(46, 35, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(48, 27, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(50, 16, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(56, 24, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(27, 30, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(60, 38, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(62, 49, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(63, 52, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(64, 59, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(70, 66, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(75, 52, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(77, 47, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(79, 39, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(80, 31, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(84, 23, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(86, 15, 0, getRandNumber(), randomColor()));

        //e
        ballsArrayList.add(new Ball(125, 63, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(118, 67, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(102, 64, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(112, 68, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(98, 56, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(98, 51, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(98, 38, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(103, 31, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(111, 27, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(120, 30, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(127, 40, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(117, 47, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(109, 47, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(103, 46, 0, getRandNumber(), randomColor()));

        //l
        ballsArrayList.add(new Ball(144, 66, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(144, 52, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(144, 39, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(144, 28, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(144, 20, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(144, 11, 0, getRandNumber(), randomColor()));

        //c
        ballsArrayList.add(new Ball(187, 35, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(180, 29, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(173, 30, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(163, 33, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(161, 42, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(161, 53, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(165, 63, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(175, 67, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(180, 65, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(184, 62, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(187, 57, 0, getRandNumber(), randomColor()));

        //o
        ballsArrayList.add(new Ball(224, 29, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(231, 38, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(230, 44, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(232, 51, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(232, 52, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(227, 56, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(226, 64, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(218, 67, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(212, 66, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(205, 63, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(203, 60, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(202, 57, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(201, 52, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(202, 45, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(201, 38, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(205, 33, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(214, 27, 0, getRandNumber(), randomColor()));

        //m
        ballsArrayList.add(new Ball(248, 67, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(247, 60, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(247, 56, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(247, 51, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(247, 40, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(246, 36, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(247, 28, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(255, 31, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(261, 27, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(267, 31, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(272, 35, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(274, 42, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(273, 51, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(273, 59, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(273, 66, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(279, 33, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(284, 30, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(289, 29, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(295, 33, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(296, 36, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(297, 45, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(297, 48, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(297, 54, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(296, 60, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(297, 67, 0, getRandNumber(), randomColor()));

        //e
        ballsArrayList.add(new Ball(343, 60, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(339, 62, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(336, 65, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(331, 66, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(322, 66, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(317, 63, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(315, 57, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(314, 49, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(313, 42, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(315, 34, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(322, 30, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(328, 29, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(335, 28, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(340, 33, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(343, 38, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(344, 45, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(337, 47, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(331, 47, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(327, 47, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(323, 48, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(317, 47, 0, getRandNumber(), randomColor()));

        //!
        ballsArrayList.add(new Ball(361, 16, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 21, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 26, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 29, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 31, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 35, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(361, 37, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(363, 42, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 45, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 50, 0, getRandNumber(), randomColor()));
        ballsArrayList.add(new Ball(362, 66, 0, getRandNumber(), randomColor()));


        // adjust sizes for this demo
        double scale = 0.70f;
        for (int i = ballsArrayList.size() - 1; i >= 0; i--) {
            Ball ball = ballsArrayList.get(i);
            ball.pos.x = width / 2 - scale * 180 + scale * ball.pos.x;
            ball.pos.y = height / 2 - scale * 65 + scale * ball.pos.y - 50;
            ball.radius = scale * ball.radius;
            ball.startRadius = scale * ball.startRadius;
            ball.startPos.x = ball.pos.x;
            ball.startPos.y = ball.pos.y;
        }

        // add balls to array
        balls = new Ball[ballsArrayList.size()];
        for (int i = ballsArrayList.size() - 1; i >= 0; i--) {
            Ball ball = ballsArrayList.get(i);
            balls[i] = ball;
        }
    }

    private String randomColor() {
        return CssColor.make(randomRedBlue(), randomGreen(), randomRedBlue()).value();
    }

    private int randomGreen() {
        return (int) (Math.random() * 100 + 100);
    }

    private int randomRedBlue() {
        return (int) (Math.random() * 100);
    }

    private double getRandNumber() {
        return Math.random() * 2 + 4;
    }

    public void update(double mouseX, double mouseY) {
        Vector d = new Vector(0, 0);
        for (int i = balls.length - 1; i >= 0; i--) {
            Ball ball = balls[i];
            d.x = mouseX - ball.pos.x;
            d.y = mouseY - ball.pos.y;
            if (d.magSquared() < 100 * 100) {
                ball.goal = Vector.sub(ball.pos, d);
            } else {
                ball.goal.set(ball.startPos);
            }

            ball.update();
        }
    }

    public void draw(Context2d context) {
        for (int i = balls.length - 1; i >= 0; i--) {
            balls[i].draw(context);
        }
    }
}
