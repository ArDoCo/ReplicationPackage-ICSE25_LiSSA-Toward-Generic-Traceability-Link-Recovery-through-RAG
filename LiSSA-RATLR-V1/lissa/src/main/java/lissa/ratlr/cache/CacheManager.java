package lissa.ratlr.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class CacheManager {
    private static final String DEFAULT_CACHE_DIR = "cache";
    private static final Path DEFAULT_CACHE_DIR_PATH = Path.of(DEFAULT_CACHE_DIR);
    private static CacheManager defaultInstance = new CacheManager();
    private final Path cacheDir;
    private final Map<String, Cache> caches = new HashMap<>();

    /**
     * Creates a new instance using the specified cache directory.
     * As this constructor is designed to provide a cache manager for different cache sources, the directory is expected to already exist.
     * Use {@link CacheManager#getDefaultInstance()} to access the default cache directory.
     * 
     * @param cacheDir the cache directory to use
     * @throws IOException
     * @throws IllegalArgumentException if the default cache directory is provided or cacheDir is not a directory
     */
    public CacheManager(Path cacheDir) throws IOException {
        if (isSameAsDefaultCache(cacheDir)) {
            throw new IllegalArgumentException("accessing the default cache directory this way is permitted");
        }
        if (!Files.isDirectory(cacheDir)) {
            throw new IllegalArgumentException("path is not a directory: " + cacheDir);
        }
        this.cacheDir = cacheDir;
    }

    /**
     * Creates a new instance using {@link #DEFAULT_CACHE_DIR} as cache directory.
     * The directory is created if it doesn't exist.
     */
    private CacheManager() {
        this.cacheDir = DEFAULT_CACHE_DIR_PATH;
        if (Files.notExists(cacheDir)) {
            try {
                Files.createDirectory(cacheDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CacheManager getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new CacheManager();
        }
        return defaultInstance;
    }

    /**
     * Returns the cache for the name. Designed for model intern purposes.
     * 
     * @param name the name of the cache without file ending
     * @return the cache for the name
     */
    public Cache getCache(String name) {
        return getCache(name, true);
    }

    private Cache getCache(String name, boolean appendEnding) {
        name = name.replace(":", "__");

        if (caches.containsKey(name)) {
            return caches.get(name);
        }

        Cache cache = new Cache(cacheDir + "/" + name + (appendEnding ? ".json" : ""));
        caches.put(name, cache);
        return cache;
    }

    /**
     * Returns the cache for an existing file.
     * 
     * @param path   the path pointing to the existing cache file
     * @param create whether the cache file should be created if it doesn't exist
     * @return the cache for an existing file
     * @throws IllegalArgumentException if the file does not exist or is a directory
     */
    public Cache getCache(Path path, boolean create) {
        path = cacheDir.resolve(path.getFileName());
        if ((!create && Files.notExists(path)) || Files.isDirectory(path)) {
            throw new IllegalArgumentException("file does not exist or is a directory: " + path);
        }
        return getCache(path.getFileName().toString(), false);
    }

    public Path getCacheDir() {
        return cacheDir;
    }

    /**
     * Returns whether the path points to the default cache directory.
     * 
     * @param cacheDir the cache path to check
     * @return whether the path points to the default cache directory
     * @throws IOException
     */
    public static boolean isSameAsDefaultCache(Path cacheDir) throws IOException {
        return Files.isSameFile(DEFAULT_CACHE_DIR_PATH, cacheDir);
    }
}
