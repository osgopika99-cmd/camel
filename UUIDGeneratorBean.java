@Component("UUIDGeneratorBean")
public class UUIDGeneratorBean {

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
    // MD5-based deterministic UUID (v3 equivalent)
    public String generateUUID(@Body String input) {
        if (input == null) input = UUID.randomUUID().toString();
        return UUID.nameUUIDFromBytes(input.getBytes()).toString();
    }

}
