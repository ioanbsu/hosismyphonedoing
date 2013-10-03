package com.artigile.howismyphonedoing.client.canvas;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.media.client.Video;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * User: ioanbsu
 * Date: 3/11/11
 * Time: 5:34 PM
 */
public class CanvasAnimation extends Composite {
    //timer refresh rate, in milliseconds
    static final int refreshRate = 25;
    private static final int WIDTH = 380;
    private static final int HEIGHT = 240;
    private static CanvasTestUiBunder uiBinder = GWT.create(CanvasTestUiBunder.class);
    final CssColor redrawColor = CssColor.make("rgba(255,255,255,0.9)");
    @UiField
    FlowPanel mainPanel;
//    @UiField
//    FlowPanel coord;
    BallGroup ballGroup;
    Context2d context;
    Context2d backBufferContext;
    // mouse positions relative to canvas
    int mouseX, mouseY;

    public CanvasAnimation() {
        mouseX = Integer.MAX_VALUE;
        mouseY = Integer.MAX_VALUE;
        initWidget(uiBinder.createAndBindUi(this));
        Canvas canvas = Canvas.createIfSupported();
        Canvas backBuffer = Canvas.createIfSupported();
        mainPanel.add(canvas);
        Video video = Video.createIfSupported();
        video.getVideoElement().setSrc("");

        canvas.setWidth(WIDTH + "px");
        canvas.setHeight(HEIGHT + "px");
        canvas.setCoordinateSpaceWidth(WIDTH);
        canvas.setCoordinateSpaceHeight(HEIGHT);

        backBuffer.setCoordinateSpaceWidth(WIDTH);
        backBuffer.setCoordinateSpaceHeight(HEIGHT);

        context = canvas.getContext2d();
        backBufferContext = canvas.getContext2d();
        ballGroup = new BallGroup(WIDTH, HEIGHT);
        ballGroup.draw(context);

        new Timer() {

            @Override
            public void run() {
                doUpdate();
            }
        }.scheduleRepeating(refreshRate);
 /*       canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                coord.add(new Label("ballsArrayList.add(new Ball(" + event.getX() + "," + event.getY() + ", 0, getRandNumber(),  randomColor()));"));
            }
        });*/
        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                mouseX = event.getX();
                mouseY = event.getY();
            }
        });
        canvas.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                mouseX = Integer.MAX_VALUE;
                mouseY = Integer.MAX_VALUE;
            }
        });
    }

    void doUpdate() {
        // update the back canvas
        backBufferContext.setFillStyle(redrawColor);
        backBufferContext.fillRect(0, 0, WIDTH, HEIGHT);
        ballGroup.update(mouseX, mouseY);
        ballGroup.draw(backBufferContext);

    }

    interface CanvasTestUiBunder extends UiBinder<FlowPanel, CanvasAnimation> {
    }

}
