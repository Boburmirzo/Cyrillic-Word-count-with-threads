import java.io.*;
import java.util.*;


public class MyWriter<T, R>
{
    private static MyWriter instance;
    private int count;
    private static final Object  lock = new Object();

    /***
     * MyWriter Private Constructor due to Singleton Pattern.
     */
    private MyWriter()
    {
        count = 0;
        createDirectory();
    }

    /***
     * Static get instance method that uses the DCL Pattern and is
     * thread safe.
     * @return this
     */
    public static MyWriter getInstance()
    {
        if (instance == null)
        {
            synchronized (lock)
            {
                if(instance == null)
                {
                    instance = new MyWriter<>();
                }
            }
        }
        return instance;
    }

    /***
     * This is a thread safe method that will take requests from the chunk threads and write their
     * word computations out to the disk.
     * @param entry Iterable Map Set
     * @param fileName String chunk file name.
     * @return this
     */
    synchronized public MyWriter createChunkFile(Set<Map.Entry<T,R>> entry, String fileName)
    {
        try
        {
            Writer writer = new FileWriter("output/" + fileName + "_" + count++ + ".chunk" );
            PrintWriter printer = new PrintWriter(writer);

            for(Map.Entry pair :entry)
            {
                printer.println(pair.getKey().toString() + "    " + pair.getValue().toString());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return this;
    }

    /***
     * Creates the output directory
     */
    private void createDirectory()
    {
        File chunkDirectory = new File("output");
        if(chunkDirectory.exists() && count == 0)
        {
            deleteDirectory(chunkDirectory);
        }
        chunkDirectory.mkdir();
        assert chunkDirectory.exists() : "Cannot create output directory, please try again.";
    }

    /***
     * Deletes the a directory and files associated with it.
     * @param file File to delete.
     */
    private void deleteDirectory(File file)
    {
        if(file.isDirectory())
        {
            if(file.list().length == 0) //Empty Directory
            {
                file.delete();
            }else
            {
                String files[] = file.list();
                for (String temp : files)
                {
                    File fileDelete = new File(file, temp);
                    deleteDirectory(fileDelete); //Calls Delete on all files in the Directory.
                }

                if(file.list().length==0)
                {
                    file.delete();
                }
            }
        }else //If File
        {
            file.delete();
        }
    }

    /***
     * Creates a results file after all the chunks have been successfully creaked.
     * @param results Iterable Map Set
     */
    public void createResultsFile(Set<Map.Entry<T, R>> results)
    {
        Map<String, Integer> temp = new HashMap<>();
        for(Map.Entry pair : results)
        {
            temp.put(pair.getKey().toString(), Integer.valueOf(pair.getValue().toString()));
        }
        sortByValues(temp);
        try
        {
            Writer writer = new FileWriter("output/results.txt");
            PrintWriter printer = new PrintWriter(writer);

            for(Map.Entry<String, Integer> pair : sortByValues(temp).entrySet())
            {
                printer.println(pair.getKey().toString() + "    " + pair.getValue().toString());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***
     * Method to sortByValues in the Map. This method will use a tree Map.
     * All values will be in descending order.
     * @return Map Object of sorted values.
     */
    private <K, V extends Comparable<V>> Map<K, V> sortByValues(Map<K, V> map) {
        Comparator<K> valueComparator =  new Comparator<K>()
        {
            public int compare(K k1, K k2) {
                int compare = map.get(k2).compareTo(map.get(k1));
                if (compare == 0) return 1;
                else return compare;
            }
        };
        Map<K, V> sortedByValues = new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
}
