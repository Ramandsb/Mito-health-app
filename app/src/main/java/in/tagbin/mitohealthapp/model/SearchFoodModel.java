package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 15/9/16.
 */
public class SearchFoodModel {
    int took;
    boolean timed_out;
    ShardsModel _shards;
    HitsModel hits;
    public SearchFoodModel(){
        hits = new HitsModel();
        _shards = new ShardsModel();
    }
    public HitsModel getHits() {
        return hits;
    }
    public int getTook() {
        return took;
    }

    public boolean isTimed_out() {
        return timed_out;
    }
    public class ShardsModel{
        int total,successful,failed;

        public int getTotal() {
            return total;
        }

        public int getFailed() {
            return failed;
        }

        public int getSuccessful() {
            return successful;
        }
    }

    public ShardsModel get_shards() {
        return _shards;
    }

    public class HitsModel{
        int total;
        float max_score;
        List<HitsArrayModel> hits;
        public HitsModel(){
            hits = new ArrayList<HitsArrayModel>();
        }
        public float getMax_score() {
            return max_score;
        }

        public int getTotal() {
            return total;
        }

        public List<HitsArrayModel> getHits() {
            return hits;
        }
    }
}
