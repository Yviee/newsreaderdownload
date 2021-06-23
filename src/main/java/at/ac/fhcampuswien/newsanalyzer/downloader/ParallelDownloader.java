package at.ac.fhcampuswien.newsanalyzer.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDownloader extends Downloader{

    @Override
    public int process(List<String> urls) {
        int count= 0;
        // Returns the number of processors available to the Java virtual machine
        int processors = Runtime.getRuntime().availableProcessors();
        // Creates a thread pool that reuses a fixed number of threads
        ExecutorService pool = Executors.newFixedThreadPool(processors);

        List<Callable<String>> callables = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) { //dynamically created tasks
            int idx = i;
            Callable<String> task = () -> saveUrl2File(urls.get(idx)); //pass async function as lambda
            callables.add(task);
        }

        try {
            List<Future<String>> allFutures = pool.invokeAll(callables);
            for(Future<String> f: allFutures) {
                String result = f.get();
                if (result != null) {
                    count++;
                }
            }
        } catch (Exception except) {
            System.out.println("Error");
        }
        pool.shutdown();
        return count;
    }
}
