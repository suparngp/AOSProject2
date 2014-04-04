package utils;

/**
 * Logging Class offering debug, log and error log levels.
 * Created by suparngupta on 4/4/14.
 */
public class Logger {

    /*
    * Logs data at LOG Level
    * */
    public static void log(Object... messages) {
        System.out.println("*********************LOG Begins********************");
        for (Object o : messages) {
            System.out.println(o);
        }
        System.out.println("*********************LOG Ends********************");
    }

    /*
    * Logs data at DEBUG Level
    * */
    public static void debug(Object... messages) {
        System.out.println("*********************DEBUG LOG Begins********************");
        for (Object o : messages) {
            System.out.println(o);
        }
        System.out.println("*********************DEBUG LOG Ends********************");
    }

    /*
    * Logs data at ERROR Level
    * */
    public static void error(Object... messages) {
        System.out.println("*********************ERROR LOG Begins********************");
        for (Object o : messages) {
            System.err.println(o);
        }
        System.out.println("*********************ERROR LOG Ends********************");
    }
}
