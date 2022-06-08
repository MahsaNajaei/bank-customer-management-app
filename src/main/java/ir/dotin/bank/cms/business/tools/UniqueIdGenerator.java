package ir.dotin.bank.cms.business.tools;

public class UniqueIdGenerator {

    private long digitLimit;
    private static long lastGeneratedId = 0;

    public UniqueIdGenerator(int numberOfDigits) {
        digitLimit = Math.round(Math.pow(10, numberOfDigits));
    }

    public long generateNumberId() {
        long id = System.currentTimeMillis() % digitLimit;
        if (id <= lastGeneratedId) {
            long diff = lastGeneratedId - id;
            id = (id + diff + 1) % digitLimit;
        }

        return lastGeneratedId = id;
    }
}
