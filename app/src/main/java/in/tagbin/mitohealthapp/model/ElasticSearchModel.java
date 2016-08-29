package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 29/8/16.
 */
public class ElasticSearchModel {
    QueryModel query;
    public ElasticSearchModel(){
        query = new QueryModel();
    }

    public QueryModel getQuery() {
        return query;
    }

    public void setQuery(QueryModel query) {
        this.query = query;
    }

    public class QueryModel{
        BoolModel bool;
        public QueryModel(){
            bool = new BoolModel();
        }

        public BoolModel getBool() {
            return bool;
        }

        public void setBool(BoolModel bool) {
            this.bool = bool;
        }

        public class BoolModel{
            List<MustModel> must;
            public BoolModel(){
                must = new ArrayList<MustModel>();
            }

            public List<MustModel> getMust() {
                return must;
            }

            public void setMust(List<MustModel> must) {
                this.must = must;
            }

        }
    }
}
