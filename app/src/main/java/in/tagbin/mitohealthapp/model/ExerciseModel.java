package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 4/10/16.
 */

public class ExerciseModel {
    int id,METS_LI_BMR,METS_MI_BMR,METS_HI_BMR;
    String name,METS_RMR;
    ExerciseCategory exercise_category;
    ExerciseSubCategory exercise_sub_category;
    List<ExerciseMuscleGroup> exercise_muscle_group;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ExerciseCategory getExercise_category() {
        return exercise_category;
    }

    public List<ExerciseMuscleGroup> getExercise_muscle_group() {
        return exercise_muscle_group;
    }

    public ExerciseSubCategory getExercise_sub_category() {
        return exercise_sub_category;
    }

    public int getMETS_HI_BMR() {
        return METS_HI_BMR;
    }

    public int getMETS_LI_BMR() {
        return METS_LI_BMR;
    }

    public int getMETS_MI_BMR() {
        return METS_MI_BMR;
    }

    public String getMETS_RMR() {
        return METS_RMR;
    }

    public ExerciseModel(){
        exercise_category = new ExerciseCategory();
        exercise_sub_category = new ExerciseSubCategory();
        exercise_muscle_group = new ArrayList<ExerciseMuscleGroup>();
    }
    public class ExerciseCategory{
        int id;
        String name;
        ArrayList<Integer> exercise_subcategory;
        public ExerciseCategory(){
            exercise_subcategory = new ArrayList<Integer>();
        }

        public int getId() {
            return id;
        }

        public ArrayList<Integer> getExercise_subcategory() {
            return exercise_subcategory;
        }

        public String getName() {
            return name;
        }
    }
    public class ExerciseSubCategory{
        int id;
        String name;
        ArrayList<Integer> muscle_group;
        public ExerciseSubCategory(){
            muscle_group = new ArrayList<Integer>();
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Integer> getMuscle_group() {
            return muscle_group;
        }
    }
    public class ExerciseMuscleGroup{
        int id;
        String name;

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
