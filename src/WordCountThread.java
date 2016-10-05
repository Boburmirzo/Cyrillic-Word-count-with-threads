import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordCountThread extends Thread
{

    private Chunk chunk;
    private TreeMap<String, Integer> words;

    /***
     * Thread Constructor that takes in a Chunk object.
     * @param tasks
     */
    public WordCountThread(Chunk tasks)
    {
        this.chunk = tasks;
        words = new TreeMap<>();
    }

    /***
     * Overridden run method.
     */
    public void run()
    {
        getWords();
        MyWriter.getInstance().createChunkFile(words.entrySet(), chunk.getFilename());
    }

    /***
     * Private method to get the words from a chunk.
     */
    private void getWords()
    {
        for (String line : chunk.getTasks())
        {
            String[] temp = line.split(" ");
            for(String word : temp)
            {
                word = word.toLowerCase();
                word = word.trim();
                Pattern p = Pattern.compile("^\\w+$", Pattern.UNICODE_CHARACTER_CLASS);
                Matcher m = p.matcher(word);
                if(m.find());
                {
                    if(words.containsKey(word))
                    {
                        words.replace(word, words.get(word), words.get(word) +1);
                    }
                    else
                    {
                        words.put(word, 1);
                    }
                }
            }
        }
    }
}
