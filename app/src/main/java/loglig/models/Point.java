package loglig.models;

import io.realm.RealmObject;

/**
 * Created by is_uptown4 on 26/07/16.
 */
public class Point extends RealmObject {
    private float x;
    private float y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }
}
