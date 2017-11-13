/**
 * Read web server data and analyse hourly access patterns.
 * Updated to analyze daily and monthly patterns
 * The LogEntry and LogfileCreator classes have also been modified
 * 
 * @author Matthew Schilling
 * @version 2017.11.13
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;
    // Where to calculate the day access counts.
    private int[] dayCounts;
    // Where to calculate the month access counts.
    private int[] monthCounts;
    // Where to calculate the year access counts.
    private int[] yearCounts;

    /**
     * Create an object to analyze all web accesses.
     */
    public LogAnalyzer()
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        // I am not sure why, but the only way to avoid errors was
        // to pad with an extra entry
        dayCounts = new int[29];
        monthCounts = new int[13];
        yearCounts = new int[2016];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }

    /**
     * Analyze the all of the access data from the log file.
     * Add values to arrays that each contain the number of results for a timeframe.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            //use the reader to interpret the file, then populate the arrays
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
            int day = entry.getDay();
            dayCounts[day]++;
            int month =entry.getMonth();
            monthCounts[month]++;
            int year =entry.getYear();
            yearCounts[year]++;
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
    /**
      * Return the total count of entries in the log file
      * @return a count of items in an array
      */
    public int numberOfAccesses()
    {
        int total = 0;
        for(int i =0; i < hourCounts.length; i++)
        {
            total += hourCounts[i];
        }
        return total;
    }
    /**
     * Calculate the total entries for a given month
     * @param a int between 1-12 to represent the month
     * @return the value in that array position, representing 
     * the number of accesses that month.
     */
    public int totalAccessesPerMonth(int selectMonth){
        int month = selectMonth;
        int value =0;
        value = monthCounts[month];
        return value;
        }
        /**
         * Calculate the average visits the site gets a month
         * by totaling the entries for each month, and averaging.
         * @return the result of dividing the total by 12
         */
    public int averageAccessesPerMonth(){
        int total =0;
        int average =0;
        for(int i=0; i<monthCounts.length; i++)
            total +=monthCounts[i];
        average = total/12;
        return average;
    }
    /**
     * Look for the largest value in the array of hour entries
     * store that value, and compare the next value, replacing 
     * if it is larger.
     * @return the index value of the largest value, representing
     * the hour, between 0 for 12am, and 23 for 11pm.
     */
    public int busiestHour()
    {
        int highest =0;
        int position =0;
        for(int i =0; i < hourCounts.length; i++)
        if (hourCounts[i]>highest)
        {
           highest=hourCounts[i];
           position=i;
        }
        return position;
    }
    /**
     * Use the same logic as busiestHour, but searching the array
     * of day entries instead
     * @return the index position of the largest value, between
     * 1 and capped at 28 for simplicity
     */
    public int busiestDay(){
        int highest =0;
        int position =0;
        for(int i=0; i<dayCounts.length; i++)
        if(dayCounts[i]>highest){
            highest=dayCounts[i];
            position=i;
        }
        return position;
    }
    /**
     * More of the same, just with the month array this time
     * @return the index, between 1 and 12, for each month
     */
    public int busiestMonth(){
        int highest =0;
        int position =0;
        for(int i=0; i<monthCounts.length; i++)
        if(monthCounts[i]>highest){
            highest=monthCounts[i];
            position=i;
        }
        return position;
    }
    /**
     * Calculate the hour with the least traffic by inversing the logic
     * check for the busiest methods
     * @ return the index of hour array 
     */
    public int quietestHour()
    {
        int lowest =hourCounts[0];
        int position =0;
        for(int i=0; i <hourCounts.length; i++)
        //Make sure that we dont allow zeroes
            if(hourCounts[i]>0 && hourCounts[i]<lowest){
            lowest=hourCounts[i];
            position=i;
        }
        return position;
    }
    /**
     * Mimic the quietestHour method, searching the day array
     * @return index of dayCounts with the lowest count, 1-28
     */
    public int quietestDay()
    {
        int lowest =dayCounts[1];
        int position =0;
        for(int i=0; i <dayCounts.length; i++)
            if(dayCounts[i]>0 && dayCounts[i]<lowest){
            lowest=dayCounts[i];
            position=i;
        }
        return position;
    }
    /**
     * Mimic the previous methods and search the month array
     * @return index of monthCounts, 1-12
     */
    public int quietestMonth()
    {
        int lowest =monthCounts[1];
        int position =0;
        for(int i=1; i <monthCounts.length; i++)
            if(monthCounts[i]>0 && monthCounts[i]<lowest){
            lowest=monthCounts[i];
            position=i;
        }
        return position;
    }
    /**
     * Determine which successive hours have the most traffic
     * by looking at entries two at a time with a nested loop
     * @return the index location of first hour in the pair
     */
    public int busiestTwoHour(){
        int hold=0;
        int position =0;
        int busy =0;
        int busiest =0;
        for(int i =0; i<hourCounts.length; i++){
            //force outer loop to run once,as we are only comparing 1&2, 3&4
            //not 1&2, 1&3, 1&4, etc. 
            for(int j= i+1; j<hourCounts.length; j+=24){
            busy = hold + hourCounts[j];
            if(busy>busiest){
                busiest = busy;
                position = i;
            }
            }
        }
            return position;
        }
}
