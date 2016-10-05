import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordCount
{
    public static void main(String[] args)
    {
        String directory, chunkSize, threadNum;
        if(args.length == 3)
        {
            directory = args[0];
            chunkSize = args[1];
            threadNum = args[2];

            if((Integer.valueOf(chunkSize) >= 10 && Integer.valueOf(chunkSize) <= 5000) &&
                    (Integer.valueOf(threadNum) >= 1 && Integer.valueOf(threadNum) <= 100))
            {
                threadExecutor(directory, Integer.valueOf(chunkSize), Integer.valueOf(threadNum));
            }
            System.out.println("Success!");
        }
        else
        {
            threadExecutor("C:/Users/Lion/Desktop/kereDokument/Threading-master/Threading-master/src/wikipediaAA", 20, 20); //debug only
            System.out.println("Usage: java WordCount <file|directory> <chunk size 10-5000> <# of threads 1-100>");
            System.out.println("Exiting program...");
        }
    }

    /***
     * Method to execute and organize threads.
     * @param directory directory to execute in
     * @param chunkSize size of chuncks to give threads
     * @param threadNum number of threads to create.
     */
    public static void threadExecutor (String directory, int chunkSize, int threadNum)
    {
        MyWriter writer = MyWriter.getInstance();

        ExecutorService pool = Executors.newFixedThreadPool(threadNum);
        ArrayList<Chunk> chunks = chunkCreator(directory, chunkSize);
        for (int task = 0; task < chunks.size(); task++)
        {
            pool.execute(new WordCountThread(chunks.get(task)));
        }
        pool.shutdown();
        try
        {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        writer.createResultsFile(resultCreator("output").entrySet());
    }

    public static ArrayList<Chunk> chunkCreator(String directory, int chunkSize)
    {
        File folder = new File(directory);
        BufferedReader bufferedReader = null;
        ArrayList<Chunk> result = new ArrayList<>();
        Chunk chunk;

        File[] files = folder.listFiles();
        for (File fileEntry : files)
        {
            chunk = new Chunk();
            if (fileEntry.isDirectory())
            {
                //Recurse Directory Not Needed...
            }
            else if (fileEntry.isFile())
            {
                try
                {
                    String sCurrentLine;

                    bufferedReader = new BufferedReader(new FileReader(fileEntry));
                    chunk.setFilename(fileEntry.getName());
                    while ((sCurrentLine = bufferedReader.readLine()) != null)
                    {
                        chunk.addTask(sCurrentLine.toString());
                        if(chunk.getNumberOfTasks() == chunkSize)
                        {
                            result.add(chunk);
                            chunk = new Chunk();
                            chunk.setFilename(fileEntry.getName());
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        if (bufferedReader != null) bufferedReader.close();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            else
            {
                System.out.println("No such file/directory: <the directory that the user had specified>");
            }
        }
        return result;
    }

    public static HashMap<String, Integer> resultCreator(String directory)
    {
        File folder = new File(directory);
        BufferedReader bufferedReader = null;
        HashMap<String, Integer> result = new HashMap<>();

        File[] files = folder.listFiles();
        for (final File fileEntry : files)
        {
            if (fileEntry.isDirectory())
            {
                //Recurse Directory
            }
            else if (fileEntry.isFile())
            {
                try
                {
                    String sCurrentLine;

                    bufferedReader = new BufferedReader(new FileReader(fileEntry));
                    while ((sCurrentLine = bufferedReader.readLine()) != null)
                    {
                        String[] temp = sCurrentLine.split(" ");
                        if(result.containsKey(temp[0]))
                        {
                            if(temp.length == 5)
                            {
                                result.replace(temp[0], result.get(temp[0]), result.get(temp[0]) + Integer.valueOf(temp[4]));
                            }
                            else if (temp.length == 1)
                            {
                                result.replace(temp[0], result.get(temp[0]), result.get(temp[0]) + 1);
                            }
                            else
                            {
                                System.out.println("Empty String Skipping.");
                            }
                        }
                        else
                        {
                            if(temp.length == 5)
                            {
                                result.put(temp[0], Integer.valueOf(temp[4]));
                            }
                            else if (temp.length == 1)
                            {
                                result.put(temp[0], 1);
                            }
                            else
                            {
                                System.out.println("Empty String Skipping.");
                            }
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        if (bufferedReader != null) bufferedReader.close();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
            else
            {
                System.out.println("Unknown File type... moving on.");
            }
        }
        return result;
    }
}
