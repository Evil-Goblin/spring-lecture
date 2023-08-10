package memory;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryFinder {

    public Memory get() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();

        long used = totalMemory - freeMemory;

        return new Memory(used, maxMemory);
    }

    @PostConstruct
    public void init() {
        log.info("init MemoryFinder");
    }
}
