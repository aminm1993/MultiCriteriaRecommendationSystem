import java.util.ArrayList;
import java.util.Objects;

/**
 * This defines the edge's attribute class. It consists of a name and preference function type properties and
 * maintains a weights list where each item is related to a certain time slot of the day equivalent to their indices
 * @author Amin Mohammadpour
 */
public class Attribute {
    private String name;
    private float[] weights;
    private Type type;
    private int next_index;

    public Attribute(String name, int time_slots) {
        this.name = name;
        this.weights = new float[time_slots];
        next_index = 0;
    }

    public String getName() {
        return name;
    }

    public float[] getWeights() {
        return weights;
    }

    public float get_weight(int index){
        return weights[index];
    }

    public void setWeights(float[] weights) {
        this.weights = weights;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Add a new weight value to this attribute
     * @param w the weight to be added
     */
    public void add_weight(float w){
        weights[next_index] = w;
        next_index++;
    }
}
