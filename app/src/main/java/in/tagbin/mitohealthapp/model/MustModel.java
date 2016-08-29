package in.tagbin.mitohealthapp.model;

/**
 * Created by hp on 8/29/2016.
 */
public class MustModel {
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
