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

            public class MustModel{
                MatchPhrase match_phrase_prefix;

                public void setMatch_phrase_prefix(MatchPhrase match_phrase_prefix) {
                    this.match_phrase_prefix = match_phrase_prefix;
                }

                public  MustModel(){
                    match_phrase_prefix = new MatchPhrase();
                }

                public MatchPhrase getMatch_phrase_prefix() {
                    return match_phrase_prefix;
                }

                public class MatchPhrase{
                    String recipe_name;

                    public void setRecipe_name(String recipe_name) {
                        this.recipe_name = recipe_name;
                    }

                    public String getRecipe_name() {
                        return recipe_name;
                    }
                }
            }
        }
    }
}
