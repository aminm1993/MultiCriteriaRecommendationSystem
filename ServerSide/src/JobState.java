/**
 * @author Amin Mohammadpour
 */
public class JobState {
    private boolean completed;
    public JobState(){
        completed = false;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
