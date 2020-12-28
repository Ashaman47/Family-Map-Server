package Service;

import java.util.UUID;

public class UUIDGenerator {
    public String UUIDGenerator() {
        String uuid =
                UUID.randomUUID().toString();
        return uuid;
    }
}