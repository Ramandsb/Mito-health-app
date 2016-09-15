package in.tagbin.mitohealthapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasaqt on 10/9/16.
 */
public class RecommendationModel {
    String meal_time;
    MealTypeModel meal_type;
    List<MealsModel> meals;
    public RecommendationModel(){
        meal_type = new MealTypeModel();
        meals = new ArrayList<MealsModel>();
    }
    public class MealTypeModel{
        int id;
        String food_time;

        public int getId() {
            return id;
        }

        public String getFood_time() {
            return food_time;
        }
    }

    public List<MealsModel> getMeals() {
        return meals;
    }

    public MealTypeModel getMeal_type() {
        return meal_type;
    }

    public String getMeal_time() {
        return meal_time;
    }

    public class MealsModel {
        ComponentModel component;
        int amount, id,meal_id,flag;
        String type, time;

        public MealsModel() {
            component = new ComponentModel();
        }

        public int getMeal_id() {
            return meal_id;
        }

        public int getId() {
            return id;
        }

        public int getFlag() {
            return flag;
        }

        public ComponentModel getComponent() {
            return component;
        }

        public int getAmount() {
            return amount;
        }

        public String getTime() {
            return time;
        }

        public String getType() {
            return type;
        }

        public class ComponentModel {
            int id, food_type, preparation_time;
            float total_protein, total_carbohydrate, total_fat, serving_unit, total_energy;
            String name, recipe, image;
            ServingType serving_type;
            List<FoodTimeModel> food_time;
            List<CuisineModel> cuisine;
            FoodGroupModel food_group;

            public ComponentModel() {
                serving_type = new ServingType();
                food_group = new FoodGroupModel();
                food_time = new ArrayList<FoodTimeModel>();
                cuisine = new ArrayList<CuisineModel>();
            }

            public int getId() {
                return id;
            }

            public int getFood_type() {
                return food_type;
            }

            public int getPreparation_time() {
                return preparation_time;
            }

            public float getServing_unit() {
                return serving_unit;
            }

            public float getTotal_carbohydrate() {
                return total_carbohydrate;
            }

            public float getTotal_energy() {
                return total_energy;
            }

            public float getTotal_fat() {
                return total_fat;
            }

            public float getTotal_protein() {
                return total_protein;
            }

            public FoodGroupModel getFood_group() {
                return food_group;
            }

            public List<CuisineModel> getCuisine() {
                return cuisine;
            }

            public List<FoodTimeModel> getFood_time() {
                return food_time;
            }

            public ServingType getServing_type() {
                return serving_type;
            }

            public String getImage() {
                return image;
            }

            public String getName() {
                return name;
            }

            public String getRecipe() {
                return recipe;
            }

            public class ServingType {
                int id;
                String serving_type;

                public int getId() {
                    return id;
                }

                public String getServing_type() {
                    return serving_type;
                }
            }

            public class FoodTimeModel {
                int id;
                String food_time;

                public int getId() {
                    return id;
                }

                public String getFood_time() {
                    return food_time;
                }
            }

            public class FoodGroupModel {
                int id;
                String name;

                public int getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }
}
