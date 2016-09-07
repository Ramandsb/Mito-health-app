package in.tagbin.mitohealthapp.Pojo;

import in.tagbin.mitohealthapp.StickyHeaders.exposed.StickyHeader;

/**
 * Created by hp on 9/5/2016.
 */
public class FoodHeaders extends DataItems implements StickyHeader {


    String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
