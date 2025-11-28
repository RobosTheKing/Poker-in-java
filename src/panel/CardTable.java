package panel;

import java.util.ArrayList;
import java.awt.event.*;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.RenderingHints;

public class CardTable extends JPanel {
    private java.util.List<CardObj> cards = new ArrayList<>();
    private java.util.List<Text> textOnScreen = new ArrayList<>();
    private CardObj hovered = null;
    private CardObj lastHovered = null;

    public CardTable() {
        setBackground(new Color(0, 120, 10));
        addMouseListener(new MouseAdapter() {

            // animace pro to když je karta vybraná
            @Override
            public void mousePressed(MouseEvent e) {
                int duration = 40;
                hovered = getCardAt(e.getX(), e.getY());
                if (hovered == null) return;
                if (!hovered.isPicked() && !hovered.isMoving() && hovered.isPickable()) {
                    hovered.setPicked(true);

                    hovered.setAngleOffsetBefore(hovered.getAngleOffset());

                    moveObjTo(hovered.getX(), hovered.getY() - hovered.getWidth() / 8, hovered, duration);

                    rotateObj(-hovered.getRealAngle(), hovered, duration, 0, false);

                } else if (!hovered.isMoving() && hovered.isPickable()) {
                    hovered.setPicked(false);

                    moveObjTo(hovered.getX(), hovered.getY() + hovered.getWidth() / 8, hovered, duration);

                    rotateObj(hovered.getAngleOffsetBefore(), hovered, duration, 0, false);
                }
                lastHovered = hovered;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            // animace pro hover
            @Override
            public void mouseMoved(MouseEvent e) {
                hovered = getCardAt(e.getX(), e.getY());
                if (hovered != null) {
                    if (hovered != lastHovered && hovered.isPickable()) {
                        lastHovered = hovered;
                        viggle(1, 2, hovered, 25, 0);
                    }
                } else {
                    lastHovered = null;
                }
            }
        });
    }

    public void add(CardObj card) {
        cards.add(card);
        repaint();
    }

    public CardObj getCard(int i) {
        return cards.get(i);
    }

    // dostane první kartu na pozizi myši
    public CardObj getCardAt(int mouseX, int mouseY) {
        CardObj card = cards.stream()
            .sorted((a, b) -> b.getZPosition() - a.getZPosition())
            .filter(c -> c.isPointOn(mouseX, mouseY))
            .findFirst()
            .orElse(null);

        return card;
    }

    public void resetCards() {
        cards.removeAll(cards);
    }

    public void add(Text text) {
        textOnScreen.add(text);
        repaint();
    }

    public Text getText(int i) {
        return textOnScreen.get(i);
    } 

    public void moveObjTo(Coordinates coordinates, CardObj obj, int durationMs) {
        moveObjTo(coordinates.getX(), coordinates.getY(), obj, durationMs);
    }

    public void moveObjTo(double targetX, double targetY, CardObj obj, int durationMs) {
        if (obj.isMoving()) return;
        obj.setMoving(true);

        final double startX = obj.getX();
        final double startY = obj.getY();

        final int fps = 120;
        final int delay = 1000 / fps;
        final int steps = Math.max(1, durationMs / delay);

        final int[] step = {0};

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            step[0]++;

            double percentageDone = (double) step[0] / steps;

            double steepness = 10.0;
            double logistic = 1.0 / (1.0 + Math.exp(-steepness * (percentageDone - 0.5)));
            double logisticStart = 1.0 / (1.0 + Math.exp(-steepness * (0 - 0.5)));
            double logisticEnd = 1.0 / (1.0 + Math.exp(-steepness * (1 - 0.5)));

            double percentageMoved = (logistic - logisticStart) / (logisticEnd - logisticStart);

            double newX = startX + (targetX - startX) * percentageMoved;
            double newY = startY + (targetY - startY) * percentageMoved;

            obj.setXY(newX, newY);
            repaint();

            if (step[0] >= steps) {
                obj.setXY(targetX, targetY);
                obj.setMoving(false);
                repaint();
                ((Timer)e.getSource()).stop();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }

    public void rotateObj(double targetAngle, CardObj obj, int durationMs, int initialDelay, boolean modifyRealAngle) {

        final double startAngle = modifyRealAngle
                ? obj.getRealAngle()
                : obj.getAngleOffset();

        final int fps = 120;
        final int delay = 1000 / fps;
        final int steps = Math.max(1, durationMs / delay);

        final int[] step = {0};

        if (modifyRealAngle) {
            if (obj.isRotating()) return;
            obj.setRotating(true);
        } else {
            if (obj.isOffsetRotating()) return;
            obj.setOffsetRotating(true);
        }

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            step[0]++;
            double t = (double) step[0] / steps;

            double steepness = 10.0;
            double logistic = 1.0 / (1.0 + Math.exp(-steepness * (t - 0.5)));
            double logisticStart = 1.0 / (1.0 + Math.exp(-steepness * (0 - 0.5)));
            double logisticEnd   = 1.0 / (1.0 + Math.exp(-steepness * (1 - 0.5)));

            double smoothT = (logistic - logisticStart) / (logisticEnd - logisticStart);

            double newAngle = startAngle + (targetAngle - startAngle) * smoothT;

         if (modifyRealAngle)
                obj.setRealAngle(newAngle);
            else
                obj.setAngleOffset(newAngle);

            repaint();

            if (step[0] >= steps) {
                if (modifyRealAngle)
                    obj.setRealAngle(targetAngle);
                else
                    obj.setAngleOffset(targetAngle);

                if (modifyRealAngle)
                    obj.setRotating(false);
                else
                    obj.setOffsetRotating(false);

                repaint();
                ((Timer) e.getSource()).stop();
            }
        });

        timer.setInitialDelay(initialDelay);
        timer.start();
    }

    public void flipCard(CardObj obj, int durationMs, int initialDelay) {
        final int fps = 120;
        final int delay = 1000 / fps;
        final int steps = Math.max(1, durationMs / delay / 2);
        final int initialWidth = obj.getWidth();

        final int[] step = {0};

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            step[0]++;
            if (step[0] < steps) {
                double percentageDone =  1 - (double) step[0] / (steps * 1.0);

                int newWidth = (int) Math.round(initialWidth * percentageDone);

                obj.setWidth(newWidth);
                repaint();
            } else if (step[0] == steps) {
                obj.setWidth(0);
                repaint();
                if (obj.isFliped()) {
                    obj.loadBackOfCard();
                    obj.setFliped(false);
                } else {
                    obj.loadImage();
                    obj.setFliped(true);
                }
            } else {
                double percentageDone = (double) step[0] / (steps * 1.0) / 2;

                int newWidth = (int) Math.round(initialWidth * percentageDone);

                obj.setWidth(newWidth);
                repaint();
            }

            if (step[0] >= steps * 2) {
                obj.setWidth(initialWidth);
                repaint();
                ((Timer)e.getSource()).stop();
            }
        });

        timer.setInitialDelay(initialDelay);
        timer.start();
    }

    public void viggle(int wiggleCount, int maxRotate, CardObj obj, int durationMs, int initialDelay) {
        if (obj.isViggling()) return;
        obj.setViggling(true);

        final double initialAngleOffset = obj.getAngleOffset();

        final int fps = 120;
        final int delay = 1000 / fps;

        final int totalSteps = Math.max(1, durationMs * wiggleCount / delay);
        final int[] step = {0};

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            step[0]++;

            double t = (double) step[0] / totalSteps;
            double cycles = wiggleCount;

            double angleOffset = Math.sin(t * Math.PI * 2 * cycles) * maxRotate;

            obj.setAngleOffset(initialAngleOffset - angleOffset);
            repaint();

            if (step[0] >= totalSteps) {
                obj.setAngleOffset(initialAngleOffset);
                obj.setViggling(false);
                repaint();
                ((Timer)e.getSource()).stop();
            }

            if (obj.isMoving()) {
                obj.setViggling(false);
                obj.setAngleOffset(initialAngleOffset);
                ((Timer)e.getSource()).stop();
                return;
            }
        });

        timer.setInitialDelay(initialDelay);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        cards.stream()
            .sorted((a, b) ->  a.getZPosition() - b.getZPosition())
            .forEach(c -> c.draw(g2));

        textOnScreen.stream()
            .forEach(t -> t.draw(g2));
    }
}