package ngordnet.ngrams;

import java.util.*;

/*
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;




    //private TreeMap<Integer, Double> timeSeries;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        //System.out.println((ts.subMap(startYear, endYear + 1)));
        for (int i = startYear; i < endYear + 1; i++) {
            if (ts != null && ts.containsKey(i)) {
                this.put(i, ts.get(i));
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> lstyears = new ArrayList<>();

        Collection keys = this.keySet();
        Iterator itr = keys.iterator();
        //iterate through TreeMap values iterator
        while (itr.hasNext()) {
            lstyears.add((Integer) itr.next());
        }
        return lstyears;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> lstdata = new ArrayList<>();

        Collection keys = this.values();
        Iterator itr = keys.iterator();
        //iterate through TreeMap values iterator
        while (itr.hasNext()) {
            lstdata.add((Double) itr.next());
        }
        return lstdata;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries rv = new TimeSeries();
        if (ts == null && this == null) {
            return rv;
        }
        if (ts == null) {
            int startyear = this.years().get(0);
            int endyear = this.years().get(this.size() - 1);
            return new TimeSeries(this, startyear, endyear);
        }
        if (this == null) {
            int startyear = ts.years().get(0);
            int endyear = ts.years().get(ts.size() - 1);
            return new TimeSeries(ts, startyear, endyear);
        }
        List years = this.years();
        List tsyears = ts.years();
        //remove
        while (years.size() != 0 && tsyears.size() != 0) {
            if (years.get(0).equals(tsyears.get(0))) {
                rv.put((Integer) years.get(0), (ts.get(years.get(0)) + this.get(years.get(0))));
                years.remove(0);
                tsyears.remove(0);
            } else if ((Integer) years.get(0) > (Integer) tsyears.get(0)) {
                rv.put((Integer) tsyears.get(0), ts.get(tsyears.get(0)));
                tsyears.remove(0);
            } else {
                rv.put((Integer) years.get(0), this.get(years.get(0)));
                years.remove(0);
            }
        }
        while (years.size() != 0) {
            rv.put((Integer) years.get(0), this.get(years.get(0)));
            years.remove(0);
        }
        while (tsyears.size() != 0) {
            rv.put((Integer) tsyears.get(0), ts.get(tsyears.get(0)));
            tsyears.remove(0);
        }
        return rv;
    }



    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries rv = new TimeSeries();
        if (ts.years() == null && this.years() == null) {
            return rv;
        }
        List years = this.years();
        List tsyears = ts.years();
        //remove
        while (years.size() != 0 && tsyears.size() != 0) {
            if (!ts.containsKey(years.get(0))) {
                throw new IllegalArgumentException("Missing Year!");
            }
            if (!this.containsKey(tsyears.get(0))) {
                tsyears.remove(0);
            }
            if (years.get(0).equals(tsyears.get(0))) {
                rv.put((Integer) years.get(0), (this.get(years.get(0)) / ts.get(years.get(0))));
                tsyears.remove(0);
                years.remove(0);
            } else if ((Integer) years.get(0) > (Integer) tsyears.get(0)) {
                tsyears.remove(0);
            }
        }
        return rv;
    }
}
