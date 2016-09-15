package in.tagbin.mitohealthapp.model;

/**
 * Created by aasaqt on 15/9/16.
 */
public class HitsArrayModel {
    String _index,_type,_id;
    float _score;
    SourceModel _source;

    public float getScore() {
        return _score;
    }

    public SourceModel get_source() {
        return _source;
    }

    public String get_index() {
        return _index;
    }

    public String get_type() {
        return _type;
    }

    public String getId() {
        return _id;
    }
    public class SourceModel{
        int recipe_id;
        String name;

        public String getName() {
            return name;
        }

        public int getRecipe_id() {
            return recipe_id;
        }
    }
}
