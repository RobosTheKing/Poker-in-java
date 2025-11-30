package panel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import java.io.IOException;

import javax.imageio.ImageIO;


import cardRelated.Card;

public class CardObj {
    private String imgDirectory = "";
    private final Card representingCard;
    private BufferedImage imgOfCard;
    private int zPosition;
    private double x, y;
    private int width, height;
    private double scaledWidth, scaledHeight;
    private double angle, rads;
    private double angleOffset, radsOffset, angleOffsetBefore;
    private boolean picked = false, fliped = false;
    private boolean humans = false, viggling = false, rotating = false, moving = false, offsetRotating = false;
    private boolean pickable = false;

    public CardObj(Card card, double x, double y, int width, int height, double angle) {
        this.representingCard = card;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.rads = Math.toRadians(angle);
        this.scaledWidth = height * Math.sin(rads) + width * Math.cos(rads);
        this.scaledHeight = height * Math.cos(rads) + width * Math.sin(rads);
        this.imgDirectory = "/assets/card_sprites/" + card.getCardName() + ".png";
        loadBackOfCard();
    }

    public CardObj(Card card, Coordinates coordinates, int width, int height, double angle) {
        this(card, coordinates.getX(), coordinates.getY(), width, height, angle);
    }

    public void loadImage() {
        try {
            this.imgOfCard = ImageIO.read(getClass().getResource(imgDirectory));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load card image: " + representingCard.getCardName());
        }
    }

    public void loadBackOfCard() {
        try {
            this.imgOfCard = ImageIO.read(getClass().getResource("/assets/card_sprites/back_of_card.png"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load card image: " + representingCard.getCardName());
        }
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public boolean isFliped() {
        return fliped;
    }

    public void setFliped(boolean fliped) {
        this.fliped = fliped;
    }

    public boolean isHumans() {
        return humans;
    }

    public void setHumans(boolean humans) {
        this.humans = humans;
    }

    public boolean isViggling() {
        return viggling;
    }

    public void setViggling(boolean viggling) {
        this.viggling = viggling;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(boolean rotating) {
        this.rotating = rotating;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isOffsetRotating() {
        return offsetRotating;
    }

    public void setOffsetRotating(boolean offsetRotating) {
        this.offsetRotating = offsetRotating;
    }

    public boolean isPickable() {
        return pickable;
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public int getZPosition() {
        return zPosition;
    }

    public void setZPosition(int zPosition) {
        this.zPosition = zPosition;
    }

    public String getImgDirectory() {
        return imgDirectory;
    }

    public Card getRepresentingCard() {
        return representingCard;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        updateSizes();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        updateSizes();
    }

    public double getRealAngle() {
        return angle;
    }

    public void setRealAngle(double angle) {
        this.angle = angle;
        this.rads = Math.toRadians(angle);
        updateSizes();
    }

    public double getAngle() {
        return angle + angleOffset;
    }

    public double getRads() {
        return rads;
    }

    public void setRads(double rads) {
        this.rads = rads;
        this.angle = Math.toDegrees(rads);
        updateSizes();
    }

    public double getAngleOffset() {
        return angleOffset;
    }

    public void setAngleOffset(double angleOffset) {
        this.angleOffset = angleOffset;
        this.radsOffset = Math.toRadians(angleOffset);
        updateSizes();
    }

    public double getRadsOffset() {
        return radsOffset;
    }

    public void setRadsOffset(double radsOffset) {
        this.radsOffset = radsOffset;
        this.angleOffset = Math.toDegrees(radsOffset);
        updateSizes();
    }

    public double getAngleOffsetBefore() {
        return angleOffsetBefore;
    }

    public void setAngleOffsetBefore(double angleOffsetBefore) {
        this.angleOffsetBefore = angleOffsetBefore;
    }

    public void updateSizes() {
        this.scaledWidth = height * Math.sin(rads + radsOffset) + width * Math.cos(rads + radsOffset);
        this.scaledHeight = height * Math.cos(rads + radsOffset) + width * Math.sin(rads + radsOffset);
    }

    public double getScaledWidth() {
        return scaledWidth;
    }

    public double getScaledHeight() {
        return scaledHeight;
    }

    public boolean isPointOn(double mouseX, double mouseY) {
        
        double objX = mouseX - x;
        double objY = mouseY - y;

        double relativeX = objX * Math.cos(-(rads + radsOffset)) + objY * Math.sin(-(rads + radsOffset));
        double relativeY = objX * Math.sin(-(rads + radsOffset)) + objY * Math.cos(-(rads + radsOffset));

        return Math.abs(relativeX) <= width / 2.0 &&
               Math.abs(relativeY) <= height / 2.0;
    }

    public void draw(Graphics2D g2) {
        if (imgOfCard == null) return;
        
        // scalenout cartu
        AffineTransform old = g2.getTransform();

        g2.translate(x, y);
        
        g2.rotate(Math.toRadians(this.getAngle()));

        g2.drawImage(imgOfCard, -width / 2, -height / 2, width, height, null);

        g2.setTransform(old);
    }
}
