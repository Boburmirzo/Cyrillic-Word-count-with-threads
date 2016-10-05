import java.util.ArrayList;

public class Chunk
{
    private String filename;
    ArrayList<String> tasks = new ArrayList<>();

    /***
     * Sets the Chunk Filename
     * @param filename String filename
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    /***
     * Gets the File name.
     * @return String filename
     */
    public String getFilename()
    {
        return filename;
    }

    /***
     * Method to add a single task.
     * @param task String task.
     */
    public void addTask(String task)
    {
        tasks.add(task);
    }

    /***
     * Gets the tasks
     * @return ArrayList of Task
     */
    public ArrayList<String> getTasks()
    {
        return tasks;
    }

    /***
     * Gets the Number Tasks currently in the chunk.
     * @return Integer size
     */
    public int getNumberOfTasks()
    {
        return tasks.size();
    }
}
