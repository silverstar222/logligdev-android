package loglig.models;

/**
 * Created by is_uptown4 on 02/06/16.
 */
public class SportType implements Comparable<SportType> {
    private String id;
    private String name;
    private int imageResource;

    public SportType() {
        this.id = "";
        this.name = "";
        this.imageResource = 0;
    }

    public SportType(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public SportType(String id, String name, int imageResource) {
        this(id, name);
        this.imageResource = imageResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toStringStatisticType() {
        String str = "id: " + this.id + ", name; " + this.name;
        return str;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public int compareTo(SportType statistic) {
        int compare = this.name.compareTo(statistic.name);
        return compare;
    }

}